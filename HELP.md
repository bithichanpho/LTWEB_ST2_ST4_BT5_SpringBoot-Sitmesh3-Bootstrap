# SpringBootShop

Ứng dụng bán hàng quản lý sản phẩm theo danh mục (Male / Female / Accessories), có Dashboard thống kê doanh thu, sản phẩm bán chạy và hiệu quả theo danh mục.

## Công nghệ sử dụng

- Java 21, Spring Boot 4.1.1
- Spring MVC + JSP (JSTL) + SiteMesh 3 
- Spring Data JPA + SQL Server
- Giao diện: [Spark Admin (ThemeWagon)](https://themewagon.github.io/spark-admin/) - Bootstrap template

## Cấu hình database

Mở file `src/main/resources/application.properties`, sửa lại cho phù hợp với máy của bạn:

- `localhost:1433` → đổi thành host và port SQL Server đang chạy ở máy bạn.
- `databaseName=SpringBootShop` → tên database bạn đã tạo sẵn (tạo trước một database rỗng trong SQL Server Management Studio).
- `username` / `password` → tài khoản đăng nhập SQL Server của bạn.

## Tài khoản test (admin)

Email:    admin@shop.com
Password: admin123
