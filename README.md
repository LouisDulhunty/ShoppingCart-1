# ShoppingCart-1 Web Application

## Overview

The ShoppingBasket Web Application provides a web interface that allows authorized users to interact with the application through a HTTP interface. This project was developed as part of a university assignment to learn skills in web development, session management, and thorough testing methodologies.

## Features

1. **User Authentication**
    - Five pre-populated usernames (A, B, C, D, E) for login.
    - Unauthorized users receive a 401 Unauthorized response.

2. **Shopping Cart Management**
    - Each user has a personal cart initialized with items: apple, orange, pear, and banana (count = 0).
    - Users can view and update item counts in their cart.
    - Users can add new items with names and costs.
    - Users can remove items from their cart.
    - Users can update item names and costs.

3. **Session Management**
    - Users can log out, which deletes their session.

4. **Error Handling**
    - Invalid User Id errors for unauthorized access.
    - Prevents adding duplicate items and removing non-existent items.

## Getting Started

### Prerequisites

- Java Development Kit (JDK) 11 or higher
- Gradle

### Running the Application

1. **Start the application:**
    ```bash
    gradle build
    gradle bootRun
    ```

2. **Access the application:**
    - Open your browser and navigate to `http://localhost:8080`.

### Pre-Populated Usernames
- A
- B
- C
- D
- E

## Endpoints

1. **Login Screen**
    - URL: `/`
    - Description: Enter a pre-populated username to log in.

2. **Cart Page**
    - URL: `/cart`
    - Description: Displays the current items and their counts for the logged-in user.

3. **Add New Item**
    - URL: `/newname`
    - Description: Add a new item name and cost.

4. **Remove Item**
    - URL: `/delname`
    - Description: Remove items from the cart.

5. **Update Item**
    - URL: `/updatename`
    - Description: Update item names and costs.

6. **Logout**
    - URL: `/logout`
    - Description: Log out and delete the current session.

## Testing

### Unit Tests
- Ensures correct cost calculations for adding and removing items.

### System Tests
- Achieves 100% coverage using HTTP Client.

### Performance Benchmarks
- Implemented using JMH to test adding, removing, and updating items.
