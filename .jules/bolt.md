## 2026-06-19 - [Optimize hasUserPurchasedProduct query]
**Learning:** Found an anti-pattern in `DonHangService.hasUserPurchasedProduct` where all user orders and order details were loaded into memory for manual O(n^2) looping and filtering just to check if the user had purchased a specific product.
**Action:** Replace manual loop filtering with `COUNT` or `EXISTS` aggregate queries in JPA repositories to delegate the condition check efficiently to the database, ensuring O(1) retrieval instead of O(n^2).
