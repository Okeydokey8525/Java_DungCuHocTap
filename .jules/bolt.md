## 2026-06-27 - Refactor N+1 problem and in-memory filtering in hasUserPurchasedProduct
**Learning:** Found a major performance bottleneck where JPA entities were manually fetched and filtered in memory (nested loops checking user orders and their details). This leads to O(N*M) time complexity and loads excessive data into the JVM when users have many orders.
**Action:** Always prefer delegating existence checks (`COUNT`, `EXISTS`) to the database using JPQL queries rather than looping through collections of related entities in the service layer.
