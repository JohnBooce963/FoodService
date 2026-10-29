# Mai Roo — FoodService

Spring Boot 4 REST API for a small food-ordering app: menu with sizes and prices, place an order, shop order list.
Frontend: [food-order-frontend](https://github.com/JohnBooce963/food-order-frontend).

## Run locally

Needs Java 21+ and a Postgres database (Neon works).

```bash
cp .env.example .env   # then paste Neon's connection string into DATABASE_URL
./mvnw spring-boot:run # http://localhost:8080, API docs at /swagger-ui.html
./mvnw verify          # tests run on in-memory H2, no database needed
```

| Variable       | Required | Notes                                                        |
| -------------- | -------- | ------------------------------------------------------------ |
| `DATABASE_URL` | yes      | Neon's connection string as is (`postgresql://user:pass@host/db?sslmode=require`), or a JDBC URL |
| `SHOP_PASSWORD`| yes      | Password for the shop's order screen, 8+ characters. Without it shop login is off |
| `CORS_ORIGINS` | no       | Frontend origin(s), comma separated. Defaults to `*` (no cookies are used) |

The menu tables (`food`, `food_size`, `food_price`) must already exist. The order tables
(`food_order`, `food_order_item`) are created on first start.

## API

| Method | Path                | Body                                   | Notes                                 |
| ------ | ------------------- | -------------------------------------- | ------------------------------------- |
| GET    | `/foods`            |                                        | menu: each food with category, sizes, prices |
| POST   | `/orders`           | `{ customerName, items: [{ priceId, qty }] }` | total is computed from DB prices |
| POST   | `/auth/login`       | `{ password }`                         | shop login → `{ token, expiresAt }` (12 h) |
| GET    | `/orders`           |                                        | shop only: latest 100 orders, newest first |
| POST   | `/orders/{id}/done` |                                        | shop only: mark an order as done      |

Shop-only calls need `Authorization: Bearer <token>`. Errors come back as problem JSON; the reason is in `detail`.

Categories come from the first two digits of `food_id` (10 ของทอด, 20 แกง, 30 ต้มยำ, 40 ข้าว, 41-44 ผัด, …);
the mapping is in `FoodService`.

**Not yet:** menu editing through the API, uploading food images.
