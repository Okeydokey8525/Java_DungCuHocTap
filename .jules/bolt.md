## 2026-06-24 - [Optimize N+1 query]
**Learning:** Found an in-memory filtering logic in `DonHangService.hasUserPurchasedProduct` that iterates over orders and details to check for user purchases.
**Action:** Created a custom query `hasUserPurchasedProduct` in `DonHangRepository` using JPQL `EXISTS` pattern via `COUNT(c) > 0` to let DB optimize the filtering instead of fetching all orders and iterating.
