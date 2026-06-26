## 2026-06-26 - [Avoid in-memory loops for JPA relations]
**Learning:** Checking whether a user has purchased a product was previously done by fetching all orders for the user, iterating through them, and performing another query for each order's details (`N+1` problem), then doing string comparison and looping in memory (an `O(N*M)` operation). This scales terribly when the user has many orders and order details.
**Action:** Always delegate these relation checks to the database using `EXISTS` or `COUNT(...) > 0` queries in JPQL instead of loading complete entities and filtering in memory.
