## 2026-06-18 - [Fix N+1 query and OOM on home page]
**Learning:** Calling `findAll()` to load all records into memory to render a view (especially with `@ManyToOne` relationships without `@EntityGraph` or `JOIN FETCH`) can lead to N+1 queries and memory exhaustion on the home page as the data scales.
**Action:** When displaying a limited set of items (e.g., top products, recent sales), use `Pageable` in repository queries with `@EntityGraph` to fetch exactly the required limit of elements without triggering additional queries for lazy-loaded associations.
