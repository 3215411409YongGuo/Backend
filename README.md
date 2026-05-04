# Book Management System — Backend

Spring Boot backend for the Book Management System (COMP3000 Final Year Project).

## Tech Stack

- **Framework:** Spring Boot
- **Persistence:** MyBatis
- **Database:** MySQL 8.0
- **Cache:** Redis
- **Build Tool:** Maven
- **AI Integration:** Coze API

## Project Structure

```
├── src/main/
│   ├── java/com/rabbiter/bms/
│   │   ├── config/       # CORS and interceptor configuration
│   │   ├── exception/    # Custom exceptions (NotEnoughException, OperationFailureException)
│   │   ├── interceptor/  # UserInterceptor, ReaderInterceptor
│   │   ├── mapper/       # MyBatis mapper interfaces
│   │   ├── model/        # Entity classes (BookInfo, BookType, Borrow, User)
│   │   ├── service/      # Service interfaces and implementations
│   │   ├── utils/        # TokenProcessor, MyResult, MyUtils
│   │   └── web/          # REST controllers
│   └── resources/
│       ├── mapper/       # MyBatis XML mapper files
│       └── application.properties
└── pom.xml
```

## API Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | /user/login | Login and receive token |
| POST | /user/register | Register new user |
| GET | /bookInfo/queryBookInfosByPage | Paginated book search |
| POST | /bookInfo/addBookInfo | Add a book (admin only) |
| PUT | /bookInfo/updateBookInfo | Update a book (admin only) |
| DELETE | /bookInfo/deleteBookInfo | Delete a book (admin only) |
| POST | /borrow/borrowBook | Borrow a book |
| POST | /borrow/returnBook | Return a book |
| POST | /update/updateImg | Upload cover image |

## Getting Started

### Prerequisites

- Java 8+
- MySQL 8.0
- Redis
- Maven

### Installation

1. **Database setup**
```bash
   mysql -u root -p < book_manager.sql
```

2. **Configure database connection** in `src/main/resources/application.properties`

3. **Start Redis**

4. **Run the application**
```bash
   mvn spring-boot:run
```

Backend runs on `http://localhost:8080`

## Key Technical Decisions

- **MyBatis dynamic SQL** — `<if>` tags build queries at runtime, one method handles all search filter combinations
- **Transaction management** — `@Transactional` on borrow/return operations ensures atomicity
- **Token authentication** — MD5 token stored in Redis with 1-hour expiry
- **Parameterised queries** — prevents SQL injection throughout

## Frontend Repository

[https://github.com/3215411409YongGuo/Frontend](https://github.com/3215411409YongGuo/Frontend)

## Main Repository

[https://github.com/3215411409YongGuo/Comp3000](https://github.com/3215411409YongGuo/Comp3000)

## License

This project is released under the Creative Commons Zero (CC0) licence for educational purposes.
