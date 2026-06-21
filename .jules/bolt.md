## 2026-06-21 - [N+1 Anti-Pattern in Order Verification]
**Learning:** Found a severe performance bottleneck where checking if a user purchased a product loaded all their orders and iterated through them, issuing a new query for each order's details (O(N) queries, O(N*M) memory).
**Action:** Always use JPQL `COUNT` or `EXISTS` queries across table joins rather than loading entire entity hierarchies into memory to check boolean conditions.
