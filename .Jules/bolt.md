## 2026-06-30 - Delegate Cart Total Calculation to DB
**Learning:** Found an anti-pattern in `GioHangService.java` where the cart total price was calculated by fetching all items and streaming them in-memory, which is slow and memory intensive for larger carts or concurrent users.
**Action:** Created a JPQL `@Query` in `GioHangRepository` using `SUM(COALESCE(...))` to handle the fallback logic between discounted price and original price, shifting the computation burden fully to the database. Avoid loading JPA entities just for aggregation.
