
Authorizer is a standalone application which allows to the user operates with his bank account. 

It handle two operations types:

* Account creation
* Transaction authorization


## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes. 

### Prerequisites
It is necessary to have these components installed in your local machine 

```
Gradle 5.2.1
Java 11
```

### Build

This gradle command allows you build the project 
```
gradle build
```

## Running the tests

This gradle command allows you run the automated test

```
gradle test
```

## Running the application
 
This gradle command allows you run the automated test

```
gradle run --console=plain
```

### How to use

Once the application has started we can operate 

 * Example create an account:
      ```
      { "account": { "activeCard": true, "availableLimit": 100 } }
      ```
Once created, the account should not be updated or recreated, in case of violate this rule the output will be this:
      ```
      { "account": { "activeCard": true, "availableLimit": 100 }, "violations": [ "account-already-initialized" ] }
      ```

 * Example transaction authorization
      ```
      { "transaction": { "merchant": "Burger King", "amount": 20, "time": "2019-02-13T10:00:00.000Z" } }
      ```
     
      - The transaction amount should not exceed available limit: insufficient-limit
      - No transaction should be accepted when the card is not active: card-not-active
      - There should not be more than 3 transactions on a 2 minute interval: high-frequency-small-interval
      - There should not be more than 2 similar transactions (same amount and merchant) in a 2 minutes interval:
      doubled-transaction
      
      Any rule violation does not allow the transaction and the application will show the account with the previous state adding in the violation
 
 
 
 
 
 
