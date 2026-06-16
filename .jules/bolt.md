## 2024-05-18 - [N+1 query problem in DonHangService]
**Learning:** Found N+1 query problem inside `hasUserPurchasedProduct` where for a user, it first queries all orders, and then inside a loop queries details for each completed order. This has poor performance if user has many orders.
**Action:** Can write a direct SQL query to count if a `ChiTietDonHang` exists for a given `SanPham` and a `DonHang` that belongs to the `NguoiDung` and is `Hoàn thành`.
