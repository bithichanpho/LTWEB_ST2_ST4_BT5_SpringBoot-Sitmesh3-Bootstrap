package com.webprogramming.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.webprogramming.entity.Order;

@Repository
public interface IOrderRepository extends JpaRepository<Order, Integer> {

	List<Order> findByUser_UserIdOrderByOrderDateDesc(int userId);

	// Page<Order> findAll(Pageable pageable) da co san tu JpaRepository, khong can khai bao lai.
	// Luu y: KHONG dat them "OrderByOrderDateDesc" vao ten method ben duoi - Pageable duoc tao
	// o OrderService.findAll(...) da mang san Sort(order_date desc); neu ten method cung tu sinh
	// them 1 lan ORDER BY nua thi Spring Data se cong don 2 lan sort tren cung 1 cot, sinh SQL loi
	// "column has been specified more than once in the order by list".
	Page<Order> findByStatus(String status, Pageable pageable);

	long countByStatus(String status);

	// Tong doanh thu cua cac don hang da hoan thanh (dung cho thong ke, khong tinh don bi huy)
	@Query("SELECT COALESCE(SUM(o.totalAmount), 0) FROM OrderEntity o WHERE o.status = 'COMPLETED'")
	double sumRevenueOfCompletedOrders();
}
