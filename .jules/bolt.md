## 2023-10-24 - [N+1 Query Anti-Pattern in JPA Collections]
**Learning:** Found a severe anti-pattern in `DonHangService.hasUserPurchasedProduct` where nested loops over JPA entities (fetching all orders, then all details for each order) were used to check for product existence. This causes massive N+1 queries and memory bloat.
**Action:** Replace manual in-memory filtering with optimized JPQL `EXISTS` or `COUNT` queries (e.g., in `DonHangRepository`) to delegate the computation to the database.
