package com.webprogramming.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Comparator;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.webprogramming.entity.CartItem;
import com.webprogramming.entity.Order;
import com.webprogramming.entity.OrderDetail;
import com.webprogramming.entity.Product;
import com.webprogramming.entity.User;
import com.webprogramming.repository.IOrderDetailRepository;
import com.webprogramming.repository.IOrderRepository;
import com.webprogramming.repository.IProductRepository;

@Service
public class OrderService {

	@Autowired
	private EntityManager entityManager;

	private void lockProducts(List<Product> products) {
		products.stream().collect(java.util.stream.Collectors.toMap(Product::getProductId, p -> p, (a, b) -> a))
			.values().stream().sorted(Comparator.comparing(Product::getProductId))
			.forEach(p -> { entityManager.lock(p, LockModeType.PESSIMISTIC_WRITE); entityManager.refresh(p); });
	}

	private void normalizeLegacyStock(Order order) {
		if (order.getStockDeducted() == null) {
			order.setStockDeducted(order.getPaymentStatus() == null
					&& !Order.STATUS_CANCELLED.equals(order.getStatus()));
		}
	}

	// Cac buoc chuyen trang thai hop le ma nhan vien duoc phep thao tac
	private static final List<String> STATUS_FLOW = List.of(
			Order.STATUS_PENDING, Order.STATUS_CONFIRMED, Order.STATUS_SHIPPING, Order.STATUS_COMPLETED);

	@Autowired
	private IOrderRepository orderRepository;

	@Autowired
	private IOrderDetailRepository orderDetailRepository;

	@Autowired
	private IProductRepository productRepository;

	@Autowired
	private CartService cartService;

	/**
	 * Tinh tong tien don hang: la tong cua (don gia * so luong) tren tung dong chi tiet don hang.
	 * Day la ham nghiep vu trung tam theo yeu cau de bai - moi noi can tong tien don hang
	 * (dat hang, xem lai don, admin xu ly don...) deu phai goi lai ham nay de dam bao
	 * ket qua thong nhat, khong tinh rai rac o nhieu noi.
	 */
	public double calculateTotalAmount(List<OrderDetail> orderDetails) {
		if (orderDetails == null) {
			return 0d;
		}
		return orderDetails.stream()
				.mapToDouble(OrderDetail::getSubtotal)
				.sum();
	}

	/**
	 * Dat hang: chuyen gio hang hien tai thanh 1 don hang.
	 * - Luon kiem tra ton kho truoc khi tao don.
	 * - Luu snapshot ten + gia san pham vao OrderDetail.
	 * - COD: sau khi tao don thi tru ton kho + xoa gio hang.
	 * - BANKING: CHUA tru ton kho va CHUA xoa gio hang; phai thanh toan thanh cong truoc.
	 */
	@Transactional
	public Order placeOrder(User user, String recipientName, String phone, String address, String note,
			String paymentMethod) {
		if (recipientName == null || recipientName.isBlank() || recipientName.length() > 255
				|| phone == null || !phone.matches("[+0-9 ()-]{7,20}")
				|| address == null || address.isBlank() || address.length() > 500
				|| (note != null && note.length() > 500)) {
			throw new IllegalArgumentException("Vui long kiem tra ho ten, so dien thoai va dia chi giao hang");
		}

		if (!Order.PAYMENT_COD.equals(paymentMethod) && !Order.PAYMENT_BANKING.equals(paymentMethod)) {
			throw new IllegalArgumentException("Phuong thuc thanh toan khong hop le");
		}

		List<CartItem> cartItems = cartService.getItems(user);
		if (cartItems.isEmpty()) {
			throw new IllegalStateException("Gio hang dang trong, khong the dat hang");
		}

		// Loop: kiem tra ton kho cho tung san pham trong gio.
		lockProducts(cartItems.stream().map(CartItem::getProduct).toList());
		validateCartStock(cartItems);

		Order order = new Order();
		order.setUser(user);
		order.setOrderDate(LocalDateTime.now());
		order.setStatus(Order.STATUS_PENDING);
		order.setPaymentMethod(paymentMethod);
		order.setPaid(false);
		order.setPaymentStatus(Order.PAYMENT_STATUS_UNPAID);
		order.setStockDeducted(false);
		order.setRecipientName(recipientName);
		order.setPhone(phone);
		order.setAddress(address);
		order.setNote(note);

		List<OrderDetail> details = new ArrayList<>();
		for (CartItem ci : cartItems) {
			Product product = ci.getProduct();

			OrderDetail detail = new OrderDetail();
			detail.setOrder(order);
			detail.setProduct(product);
			detail.setProductName(product.getProductName()); // snapshot ten tai thoi diem dat
			detail.setPrice(product.getPrice()); // snapshot gia tai thoi diem dat
			detail.setQuantity(ci.getQuantity());
			detail.setSourceCartItemId(ci.getCartItemId());
			details.add(detail);
		}

		order.setOrderDetails(details);
		order.setTotalAmount(calculateTotalAmount(details));

		Order saved = orderRepository.save(order);

		// COD: dat hang xong thi chot hang ngay, dung voi nhanh "Thanh toan khi nhan hang".
		if (Order.PAYMENT_COD.equals(paymentMethod)) {
			deductStock(saved);
			saved.setStockDeducted(true);
			orderRepository.save(saved);
			cartService.removePurchasedItems(user, details);
		}

		return saved;
	}

	/**
	 * Xu ly ket qua thanh toan online.
	 * Thanh toan thanh cong -> tru ton kho, xoa gio hang, paid=true.
	 * Thanh toan that bai -> giu nguyen gio hang va ton kho, danh dau FAILED de retry.
	 */
	@Transactional
	public Order processOnlinePayment(int orderId, boolean success) {
		Order order = orderRepository.findById(orderId)
				.orElseThrow(() -> new IllegalArgumentException("Don hang khong ton tai"));
		entityManager.lock(order, LockModeType.PESSIMISTIC_WRITE);
		entityManager.refresh(order);
		normalizeLegacyStock(order);

		if (!Order.PAYMENT_BANKING.equals(order.getPaymentMethod())) {
			throw new IllegalStateException("Don hang nay khong su dung thanh toan online");
		}
		if (Order.STATUS_CANCELLED.equals(order.getStatus()) || Order.STATUS_COMPLETED.equals(order.getStatus())) {
			throw new IllegalStateException("Don hang da ket thuc, khong the thanh toan");
		}
		if (order.isPaid()) {
			return order;
		}

		if (!success) {
			order.setPaid(false);
			order.setPaymentStatus(Order.PAYMENT_STATUS_FAILED);
			return orderRepository.save(order);
		}

		// Re-check ton kho tai thoi diem chot thanh toan de tranh ban vuot kho.
		if (!order.isStockDeducted()) {
			lockProducts(order.getOrderDetails().stream().map(OrderDetail::getProduct).toList());
			ensureOrderStockAvailable(order);
			deductStock(order);
		}
		order.setStockDeducted(true);
		order.setPaid(true);
		order.setPaymentStatus(Order.PAYMENT_STATUS_PAID);
		Order saved = orderRepository.save(order);

		// Chi xoa gio hang sau khi thanh toan online thanh cong.
		cartService.removePurchasedItems(order.getUser(), order.getOrderDetails());
		return saved;
	}

	private void validateCartStock(List<CartItem> cartItems) {
		for (CartItem ci : cartItems) {
			if (ci.getQuantity() <= 0) {
				throw new IllegalStateException("So luong san pham trong gio khong hop le");
			}
			if (ci.getQuantity() > ci.getProduct().getQuantity()) {
				throw new IllegalStateException(
						"San pham '" + ci.getProduct().getProductName() + "' khong du hang (con lai "
								+ ci.getProduct().getQuantity() + ")");
			}
		}
	}

	private void ensureOrderStockAvailable(Order order) {
		for (OrderDetail detail : order.getOrderDetails()) {
			Product product = detail.getProduct();
			if (detail.getQuantity() > product.getQuantity()) {
				throw new IllegalStateException(
						"San pham '" + detail.getProductName() + "' vua het hang (con lai "
								+ product.getQuantity() + "). Vui long thu lai sau.");
			}
		}
	}

	private void deductStock(Order order) {
		for (OrderDetail detail : order.getOrderDetails()) {
			Product product = detail.getProduct();
			product.setQuantity(product.getQuantity() - detail.getQuantity());
			product.setSold(product.getSold() + detail.getQuantity());
			productRepository.save(product);
		}
	}

	public Optional<Order> findById(int orderId) {
		return orderRepository.findById(orderId);
	}

	// Lich su don hang cua 1 khach hang, moi nhat len truoc
	public List<Order> findByUser(User user) {
		return orderRepository.findByUser_UserIdOrderByOrderDateDesc(user.getUserId());
	}

	// Danh sach don hang cho nhan vien xu ly, co the loc theo trang thai
	public Page<Order> findAll(String status, int zeroBasedPage, int pageSize) {
		Pageable pageable = PageRequest.of(Math.max(zeroBasedPage, 0), pageSize,
				Sort.by(Sort.Direction.DESC, "orderDate"));
		if (status == null || status.isBlank()) {
			return orderRepository.findAll(pageable);
		}
		return orderRepository.findByStatus(status, pageable);
	}

	public List<OrderDetail> findDetails(int orderId) {
		return orderDetailRepository.findByOrder_OrderId(orderId);
	}

	/**
	 * Nhan vien cap nhat trang thai don hang. Chi cho phep di theo dung quy trinh:
	 * PENDING -> CONFIRMED -> SHIPPING -> COMPLETED, hoac huy (CANCELLED) khi don
	 * chua giao (chua o trang thai COMPLETED/CANCELLED).
	 */
	@Transactional
	public Order updateStatus(int orderId, String newStatus) {
		Order order = orderRepository.findById(orderId)
				.orElseThrow(() -> new IllegalArgumentException("Don hang khong ton tai"));
		entityManager.lock(order, LockModeType.PESSIMISTIC_WRITE);
		entityManager.refresh(order);
		normalizeLegacyStock(order);

		String current = order.getStatus();
		if (Order.STATUS_COMPLETED.equals(current) || Order.STATUS_CANCELLED.equals(current)) {
			throw new IllegalStateException("Don hang da ket thuc, khong the thay doi trang thai");
		}

		if (Order.STATUS_CANCELLED.equals(newStatus)) {
			restoreStock(order);
			order.setStatus(Order.STATUS_CANCELLED);
			return orderRepository.save(order);
		}

		int currentIdx = STATUS_FLOW.indexOf(current);
		int newIdx = STATUS_FLOW.indexOf(newStatus);
		if (currentIdx < 0 || newIdx < 0 || newIdx != currentIdx + 1) {
			throw new IllegalStateException("Chuyen trang thai khong hop le: " + current + " -> " + newStatus);
		}

		// BANKING phai thanh toan thanh cong truoc khi nhan vien xac nhan don.
		if (Order.STATUS_CONFIRMED.equals(newStatus)
				&& Order.PAYMENT_BANKING.equals(order.getPaymentMethod())
				&& !order.isPaid()) {
			throw new IllegalStateException("Don hang thanh toan chuyen khoan chua thanh toan thanh cong");
		}

		order.setStatus(newStatus);
		if (Order.STATUS_COMPLETED.equals(newStatus) && Order.PAYMENT_COD.equals(order.getPaymentMethod())) {
			order.setPaid(true); // COD: coi nhu da thu tien khi giao hang xong
			order.setPaymentStatus(Order.PAYMENT_STATUS_PAID);
		}
		return orderRepository.save(order);
	}

	// Hoan lai ton kho khi don hang bi huy, nhung chi neu don hang da tung tru kho.
	// Với don hang tao tu phien ban cu, paymentStatus/stockDeducted co the chua co gia tri;
	// he thong cu tru kho ngay khi dat nen coi cac don cu do la da tru kho.
	private void restoreStock(Order order) {
		if (!order.isStockDeducted()) {
			return;
		}
		lockProducts(order.getOrderDetails().stream().map(OrderDetail::getProduct).toList());

		for (OrderDetail detail : order.getOrderDetails()) {
			Product product = detail.getProduct();
			product.setQuantity(product.getQuantity() + detail.getQuantity());
			product.setSold(Math.max(0, product.getSold() - detail.getQuantity()));
			productRepository.save(product);
		}
		order.setStockDeducted(false);
	}

	public double sumRevenueOfCompletedOrders() {
		return orderRepository.sumRevenueOfCompletedOrders();
	}

	public long countByStatus(String status) {
		return orderRepository.countByStatus(status);
	}
}
