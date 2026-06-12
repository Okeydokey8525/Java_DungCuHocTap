## 2026-06-12 - Fix N+1 queries in hasUserPurchasedProduct
**Learning:** Found an N+1 and O(n^2) nested loop performance issue in `DonHangService.hasUserPurchasedProduct`. To check if a user purchased a product, the code fetched all orders of the user, iterated over each order, and then iterated over all details of each order to find a matching product id.
**Action:** Replaced the Java-side nested looping and DB fetches with a single targeted database query using `@Query` on `ChiTietDonHangRepository.existsByUserAndProductAndStatusCompleted`.
