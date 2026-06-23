## 2026-06-23 - Optimize hasUserPurchasedProduct
**Learning:** The previous implementation of `hasUserPurchasedProduct` fetched all orders for a user, then fetched details for completed orders in a nested loop to check for a product. This is a classic N+1 issue combined with an expensive memory operation. Using a single JPQL query with `COUNT` or `EXISTS` directly on `ChiTietDonHang` pushes the work to the DB.
**Action:** Always look for loops filtering data manually. Push the logic down to a custom JPA query using `COUNT` or `EXISTS`.
