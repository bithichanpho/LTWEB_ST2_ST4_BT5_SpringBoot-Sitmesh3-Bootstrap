package com.webprogramming.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.webprogramming.entity.Cart;
import com.webprogramming.entity.CartItem;
import com.webprogramming.entity.Product;
import com.webprogramming.entity.User;
import com.webprogramming.repository.ICartItemRepository;
import com.webprogramming.repository.ICartRepository;
import com.webprogramming.repository.IProductRepository;

@Service
public class CartService {

	@Autowired
	private ICartRepository cartRepository;

	@Autowired
	private ICartItemRepository cartItemRepository;

	@Autowired
	private IProductRepository productRepository;

	// Lay gio hang cua user, neu chua co thi tao moi
	@Transactional
	public Cart getOrCreateCart(User user) {
		return cartRepository.findByUser_UserId(user.getUserId())
				.orElseGet(() -> {
					Cart cart = new Cart();
					cart.setUser(user);
					return cartRepository.save(cart);
				});
	}

	public List<CartItem> getItems(User user) {
		Cart cart = getOrCreateCart(user);
		return cartItemRepository.findByCart_CartId(cart.getCartId());
	}

	// Tinh tong tien cua gio hang (dung gia hien tai cua san pham)
	public double calculateCartTotal(User user) {
		return getItems(user).stream()
				.mapToDouble(CartItem::getSubtotal)
				.sum();
	}

	public int countItems(User user) {
		return getItems(user).size();
	}

	// Them san pham vao gio hang. Neu san pham da co trong gio thi cong don so luong.
	@Transactional
	public void addToCart(User user, String productId, int quantity) {
		if (quantity <= 0) {
			throw new IllegalArgumentException("So luong phai lon hon 0");
		}

		Product product = productRepository.findById(productId)
				.orElseThrow(() -> new IllegalArgumentException("San pham khong ton tai"));

		Cart cart = getOrCreateCart(user);
		Optional<CartItem> existing = cartItemRepository
				.findByCart_CartIdAndProduct_ProductId(cart.getCartId(), productId);

		int newQuantity = quantity + existing.map(CartItem::getQuantity).orElse(0);
		if (newQuantity > product.getQuantity()) {
			throw new IllegalArgumentException(
					"So luong ton kho khong du (con lai " + product.getQuantity() + ")");
		}

		CartItem item = existing.orElseGet(() -> {
			CartItem newItem = new CartItem();
			newItem.setCart(cart);
			newItem.setProduct(product);
			newItem.setQuantity(0);
			return newItem;
		});
		item.setQuantity(newQuantity);
		cartItemRepository.save(item);
	}

	// Cap nhat so luong mot dong trong gio hang, thuoc ve dung user dang dang nhap
	@Transactional
	public void updateQuantity(User user, int cartItemId, int quantity) {
		CartItem item = requireOwnedItem(user, cartItemId);

		if (quantity <= 0) {
			cartItemRepository.delete(item);
			return;
		}
		if (quantity > item.getProduct().getQuantity()) {
			throw new IllegalArgumentException(
					"So luong ton kho khong du (con lai " + item.getProduct().getQuantity() + ")");
		}
		item.setQuantity(quantity);
		cartItemRepository.save(item);
	}

	@Transactional
	public void removeItem(User user, int cartItemId) {
		CartItem item = requireOwnedItem(user, cartItemId);
		cartItemRepository.delete(item);
	}

	@Transactional
	public void clearCart(User user) {
		Cart cart = getOrCreateCart(user);
		cartItemRepository.deleteByCart_CartId(cart.getCartId());
	}

	private CartItem requireOwnedItem(User user, int cartItemId) {
		CartItem item = cartItemRepository.findById(cartItemId)
				.orElseThrow(() -> new IllegalArgumentException("San pham trong gio khong ton tai"));
		if (item.getCart().getUser().getUserId() != user.getUserId()) {
			throw new IllegalArgumentException("Ban khong co quyen thao tac tren gio hang nay");
		}
		return item;
	}
}
