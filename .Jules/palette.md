## 2026-06-30 - Temporary Pom Downgrade for Tests
**Learning:** The project is configured with Java 25 in pom.xml, but the execution environment runs Java 21, causing local test builds to fail unless temporarily changed.
**Action:** When running tests as a pre-commit step, use a temporary patch to change `<java.version>` to 21 in `pom.xml`, run tests, and immediately restore `pom.xml`. Do NOT commit the Java version downgrade.
