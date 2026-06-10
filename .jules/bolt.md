## 2026-06-10 - Optimizing N+1 Database Queries
**Learning:** Checking nested relationships by pulling everything into memory (e.g. nested for loops calling the database) creates massive N+1 queries.
**Action:** Replace iterative memory checks with direct SQL aggregation queries (`COUNT` or `EXISTS`) inside the repository interface using `@Query`.
