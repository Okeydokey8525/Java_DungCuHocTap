
## 2024-05-30 - N+1 Query in Filtering Logic
**Learning:** Checking for complex relationships (like "has a user purchased a specific product and completed the order") in the application layer by loading all user orders and iterating over their details can cause severe N+1 query problems and high memory usage (O(N*M)).
**Action:** Push filtering and existence checks down to the database level using efficient single JPQL queries (`CASE WHEN COUNT(c) > 0 THEN true ELSE false END`) rather than fetching multiple nested collections to filter in memory.
