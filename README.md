# Mai Roo — FoodService

Spring Boot 4 REST API for a small food-ordering app: menu with sizes and prices, place an order, shop order list.
Frontend: [food-order-frontend](https://github.com/JohnBooce963/food-order-frontend).

## Run locally

Needs Java 21+ and a Postgres database (Neon works).

```bash
cp .env.example .env   # then set DATABASE_URL (JDBC URL, user/password as query params)
./mvnw spring-boot:run # http://localhost:8080, API docs at /swagger-ui.html
./mvnw verify          # tests run on in-memory H2, no database needed
```

| Variable       | Required | Notes                                                        |
| -------------- | -------- | ------------------------------------------------------------ |
| `DATABASE_URL` | yes      | `jdbc:postgresql://<host>/<db>?user=…&password=…&sslmode=require` |
| `CORS_ORIGINS` | no       | Frontend origin(s), comma separated. Defaults to `*` (no cookies are used) |

The menu tables (`food`, `food_size`, `food_price`) must already exist. The order tables
(`food_order`, `food_order_item`) are created on first start.

## API

| Method | Path                | Body                                   | Notes                                 |
| ------ | ------------------- | -------------------------------------- | ------------------------------------- |
| GET    | `/foods`            |                                        | menu: each food with its sizes/prices |
| POST   | `/orders`           | `{ customerName, items: [{ priceId, qty }] }` | total is computed from DB prices |
| GET    | `/orders`           |                                        | latest 100 orders, newest first       |
| POST   | `/orders/{id}/done` |                                        | mark an order as done                 |

Errors come back as problem JSON; the reason is in `detail`.

**Not yet:** shop login (anyone can see and close orders), menu editing through the API.
