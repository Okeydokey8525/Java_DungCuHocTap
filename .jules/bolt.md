## 2026-06-17 - Resolving N+1 database queries
**Learning:** Checking whether a user has purchased a product using in-memory loops over nested `OneToMany`/`ManyToOne` properties led to an N+1 problem (fetching user orders, then details per order, then product IDs).
**Action:** Replace nested loops that retrieve JPA entities with direct JPQL `COUNT(e) > 0` or `EXISTS` database queries passing required criteria.
