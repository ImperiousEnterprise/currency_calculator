# Currency Calculator

This project uses Quarkus, the Supersonic Subatomic Java Framework in order to create a currency calculator rest api.

The Endpoints are:

```
POST /currency - add a currency (post json as {"base": <currency name>, "quote":<currency name>, "rate":<rate as double i.e.(1.25)>})
GET /currency - get all currencies
GET /currency/<currency_code> - get all currecies and rates for a specific currency
POST /currency/convert - calculate conversion between two currencies (post json as {"from": <currency name>, "to":<currency name>, "amount":<amount as double i.e.(1.00)>}))
```

## Running the application in dev mode

You can run your application in dev mode that enables live coding using:
```
./mvnw quarkus:dev
```

## Packaging and running the application

The application can be packaged using `./mvnw package`.
It produces the `currency-1.0-SNAPSHOT-runner.jar` file in the `/target` directory.
Be aware that it’s not an _über-jar_ as the dependencies are copied into the `target/lib` directory.

The application is now runnable using `java -jar target/currency-1.0-SNAPSHOT-runner.jar`.

## Creating a native executable

You can create a native executable using: `./mvnw package -Pnative`.

Or, if you don't have GraalVM installed, you can run the native executable build in a container using: `./mvnw package -Pnative -Dquarkus.native.container-build=true`.

You can then execute your native executable with: `./target/currency-1.0-SNAPSHOT-runner`

If you want to learn more about building native executables, please consult https://quarkus.io/guides/building-native-image.