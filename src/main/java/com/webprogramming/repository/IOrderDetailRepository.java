package com.webprogramming.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.webprogramming.entity.OrderDetail;

@Repository
public interface IOrderDetailRepository extends JpaRepository<OrderDetail, Integer> {
	@org.springframework.data.jpa.repository.Query("SELECT d.product.productId, SUM(d.quantity), SUM(d.quantity * d.price) FROM OrderDetail d WHERE d.order.status = 'COMPLETED' GROUP BY d.product.productId")
	List<Object[]> completedProductSales();
	List<OrderDetail> findByOrder_OrderId(int orderId);

	// Dung de kiem tra truoc khi xoa san pham: san pham da tung nam trong 1 don hang nao
	// chua. Neu co roi thi khong duoc xoa cung (se vi pham khoa ngoai o DB), phai bao
	// cho nhan vien biet bang thong bao than thien thay vi de loi 500 xuat hien.
	boolean existsByProduct_ProductId(String productId);
}
