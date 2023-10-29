# Open API Games Processor

This repository contains a backend application simulating an interaction between a gaming platform and a provider. It
manages requests related to user balances and game transactions. The project is structured into two main packages: `app`
and `database`.

### IMPORTANT:

to use the encryption of the request change the flag [encryption.enabled.flag=false] to true, in the release version of
the project all request are done without encryption

## App Package

The `app` package comprises the application responsible for processing requests to the
endpoint: `http://localhost:9010/open-api-games/v1/games-processor/`.

### Endpoints:

1. `/balance`: POST request for retrieving user balances.
2. `/credit`: POST request for processing credit transactions.
3. `/debit`: POST request for processing debit transactions.

Assumptions made for the requests:

- All POST requests expect input in a uniform JSON format.
- The data model for request handling is standardized using a predefined structure.

## Database Package

The `database` package simulates a separate application handling database-related operations. It responds to the
endpoint: `http://localhost:9010/wallets`.

### Endpoints:

1. `/wallets`: Responsible for wallet-related operations.

#### WalletController Endpoints:

- `POST`: Create a wallet.
- `POST /{walletId}/accounts`: Add an account to a wallet.
- `GET /{walletId}/accounts/{accountId}`: Get an account from a wallet.
- `PUT /{walletId}/accounts/{accountId}`: Update an account in a wallet with a new balance.
- `DELETE /{walletId}`: Delete a wallet.
- `GET /{walletId}`: Get wallet details.

Assumptions made for the interaction with the database:

- The database operations are abstracted as if interacting with a separate application.
- Retrofit is used to simulate calls to the database as an external service.

## Running the Application with Docker

To run the application using Docker:

1. Ensure Docker is installed and running on your system.

2. Use the following Docker commands:

```bash
docker build -t game-processor-app .
docker run -d -p 9010:9010 game-processor-app
```

## Notes

For this task I consider that the database functional is in another container/app ( and because of this all interaction
between the app itself and the database are done via retrofit and rest endpoints)

To use the app, start by creating a wallet with a request similar to the provided Postman request available in the /docs
folder.

The /docs folder contains a Postman collection with examples for each operation exported for reference.

No unit tests have been included in this build as they are not part of the task.

If you are using curl for testing run this first

```bash
    curl -X POST "http://localhost:9010/wallets" -H "Content-Type: application/json" -d '{
    "userId": 123,
    "userNick": "player123",
    "maxWin": 3000,
    "accounts": [
    {
    "balance": 2000.0,
    "denomination": 8,
    "currency": "Currency for demo games"
    },
    {
    "balance": 50.0,
    "denomination": 8,
    "currency": "Albanian Lek"
    }
    ]
    }'
```