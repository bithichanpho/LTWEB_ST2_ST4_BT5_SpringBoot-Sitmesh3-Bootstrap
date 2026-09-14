package com.webprogramming.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
	 * Dat hang: chuyen toan bo gio hang hien tai cua user thanh 1 don hang.
	 * - Kiem tra gio hang khong rong, kiem tra du ton kho tung san pham.
	 * - "Chup" lai ten + gia san pham tai thoi diem dat (khong bi anh huong neu sau nay
	 *   admin doi gia/ten san pham).
	 * - Tinh tong tien don hang bang calculateTotalAmount(...).
	 * - Tru ton kho, cong so luong da ban cua san pham.
	 * - Xoa gio hang sau khi dat thanh cong.
	 */
	@Transactional
	public Order placeOrder(User user, String recipientName, String phone, String address, String note,
			String paymentMethod) {

		List<CartItem> cartItems = cartService.getItems(user);
		if (cartItems.isEmpty()) {
			throw new IllegalStateException("Gio hang dang trong, khong the dat hang");
		}

		// Kiem tra ton kho truoc khi tao don, tranh ban vuot so luong hien co
		for (CartItem ci : cartItems) {
			if (ci.getQuantity() > ci.getProduct().getQuantity()) {
				throw new IllegalStateException(
						"San pham '" + ci.getProduct().getProductName() + "' khong du hang (con lai "
								+ ci.getProduct().getQuantity() + ")");
			}
		}

		Order order = new Order();
		order.setUser(user);
		order.setOrderDate(LocalDateTime.now());
		order.setStatus(Order.STATUS_PENDING);
		order.setPaymentMethod(paymentMethod != null ? paymentMethod : Order.PAYMENT_COD);
		order.setPaid(false);
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
			detail.setProductName(product.getProductName()); // chup lai ten tai thoi diem dat
			detail.setPrice(product.getPrice()); // chup lai gia tai thoi diem dat
			detail.setQuantity(ci.getQuantity());
			details.add(detail);

			// tru ton kho + cong so luong da ban
			product.setQuantity(product.getQuantity() - ci.getQuantity());
			product.setSold(product.getSold() + ci.getQuantity());
			productRepository.save(product);
		}

		order.setOrderDetails(details);
		order.setTotalAmount(calculateTotalAmount(details)); // tinh tong tien don hang

		Order saved = orderRepository.save(order);

		cartService.clearCart(user);

		return saved;
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
		if (newIdx < 0 || newIdx != currentIdx + 1) {
			throw new IllegalStateException("Chuyen trang thai khong hop le: " + current + " -> " + newStatus);
		}

		order.setStatus(newStatus);
		if (Order.STATUS_COMPLETED.equals(newStatus) && Order.PAYMENT_COD.equals(order.getPaymentMethod())) {
			order.setPaid(true); // COD: coi nhu da thu tien khi giao hang xong
		}
		return orderRepository.save(order);
	}

	// Thanh toan don hang (vd: thanh toan online/chuyen khoan)
	@Transactional
	public Order markPaid(int orderId) {
		Order order = orderRepository.findById(orderId)
				.orElseThrow(() -> new IllegalArgumentException("Don hang khong ton tai"));
		order.setPaid(true);
		return orderRepository.save(order);
	}

	// Hoan lai ton kho khi don hang bi huy
	private void restoreStock(Order order) {
		for (OrderDetail detail : order.getOrderDetails()) {
			Product product = detail.getProduct();
			product.setQuantity(product.getQuantity() + detail.getQuantity());
			product.setSold(Math.max(0, product.getSold() - detail.getQuantity()));
			productRepository.save(product);
		}
	}

	public double sumRevenueOfCompletedOrders() {
		return orderRepository.sumRevenueOfCompletedOrders();
	}

	public long countByStatus(String status) {
		return orderRepository.countByStatus(status);
	}
}
