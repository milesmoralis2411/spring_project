# Minimal E-Commerce Backend API

This project contains a minimal e-commerce backend implementation using Spring Boot and MongoDB.

## Features
- **Product Management:** Create and List products.
- **Cart Management:** Add to cart, view cart, clear cart.
- **Order Management:** Place orders from cart, view order details and status.
- **Payment Integration:** Mock payment service with simulated async webhook callback.

## Prerequisites
- Java 17 or higher
- Maven
- MongoDB (running on localhost:27017)

## Setup & Run
1. Ensure MongoDB is running.
2. Open a terminal in the project root.
3. Run the application:
   ```bash
   mvn spring-boot:run
   ```
4. The server will start on `http://localhost:8080`.

## Testing
You can test the APIs using the provided Postman Collection: `ecommerce_postman_collection.json`.

### Testing Flow
1. **Create Product**: Use `POST /api/products` to create a product (e.g. Laptop). Copy the returned `id`.
2. **Add to Cart**: Use `POST /api/cart/add` with the `productId` and a `userId` (e.g. "user1").
3. **View Cart**: Use `GET /api/cart/user1` to see your items.
4. **Create Order**: Use `POST /api/orders` with `userId` to create an order. Copy the returned `id` (OrderId).
5. **Check Order Status**: Use `GET /api/orders/{orderId}`. Status should be `CREATED`.
6. **Make Payment**: Use `POST /api/payments/create` with the `orderId` and amount.
   - The API returns a Payment with `PENDING` status.
   - **Wait 3 seconds**: The system simulates a webhook callback in the background.
7. **Verify Payment**: Call `GET /api/orders/{orderId}` again. Order status should now be `PAID` and Payment status `SUCCESS`.

## Architecture
- **Controller**: REST Endpoints.
- **Service**: Business Logic.
- **Repository**: Data Access (MongoDB).
- **Model**: Domain Entities.
