# WorldPayMerchantOfferService
A simple RESTful service that will allow a merchant to create a new simple offer. Offers, once created, may be queried. After the period of time defined on the offer it should expire and further requests to query the offer should reflect that somehow. Before an offer has expired users may cancel it.

## API Reference
Complete API documentation can be build using the provided ``yaml/MerchantOfferService.yaml`` file.

## Prerequisites
```
- Java 1.8+
- GIT
- Maven
```

## Authentication
Authentication and authorization concerns are ignored in this project.

## Running

Application entry point -  _WorldPayMerchantOfferServiceApplication_

**POST** - Create a new merchant 

 _http://localhost:8081/merchants_

```
{
    "name": "Top Buy"
}
```

**POST** - Create a new offer.

New offer must be linked to an existing merchant 

 _http://localhost:8081/offers?merchantid={{merchantid}}_

```
{
    "description": "Don't let this offer expire",
    "currency": "EUR",
    "price": "100.0",
    "expirationDate": "2020-12-31T23:59:59"
}
```

**GET** - search for an existing offer 

_http://localhost:8081/offers/{{offerid}}?merchantid={{merchantid}}_

_note_  In all **GET** operation, the merchantid parameter is optional

Examples of returned JSON:

```
{
    "id": "01a182b7-fe56-4700-90bc-202dbccc5d80",
    "description": "Don't let this offer expire",
    "price": 100.0,
    "currency": "EUR",
    "creationDate": "2020-07-21T01:14:46.861",
    "expirationDate": "2020-12-31T23:59:59",
    "status": "ACTIVE",
    "_links": {
        "self": {
            "href": "http://localhost:8081/offers/01a182b7-fe56-4700-90bc-202dbccc5d80?merchantid=2"
        }
    }
}
```

**PUT** - Update an existing offer

The offer cannot be already expired or cancelled

_http://localhost:8081/offers/{{offerid}}?merchantid={{merchantid}}_

```
{
    "description": "Last chance to get this offer",
    "currency": "EUR",
    "price": "100.0",
    "expirationDate": "2020-12-31T23:59:59"
}
```

**DELETE** - Delete an existing active offer

The offer must be active (expiration date > now)

_http://localhost:8081/offers/{{offerid}}?merchantid={{merchantid}}_

_note_ The DELETE operation is not a physical delete as the offer persist in the database and only change its status

Examples of returned JSON:

```
{
    "id": "01a182b7-fe56-4700-90bc-202dbccc5d80",
    "description": "Don't let this offer expire",
    "price": 100.00,
    "currency": "EUR",
    "creationDate": "2020-07-21T01:14:46.861",
    "expirationDate": "2020-12-31T23:59:59",
    "status": "CANCELLED",
    "_links": {
        "self": {
            "href": "http://localhost:8081/offers/01a182b7-fe56-4700-90bc-202dbccc5d80?merchantid=2"
        }
    }
}
```
