# Tip-jar
## Description of APIs
### Get all payment history items
* URI: "/api/v1/user/{userId}/payments"
* Method: GET
* This api is to return all payments belongs to the user.
* HttpStatusCode 200 returned with an array of payments if it is successful.
* HttpStatusCode 200 returned with an empty array if the user has no payments.
* HttpStatusCode 404 returned if the user is not exists.
* The successful response looks like:
```
[
    {
        "id": 1,
        "amount": 2400.0,
        "tip": 240.0,
        "thumbnail": "abc",
        "createdAt": "2022-04-14T00:06:35.569166",
        "updatedAt": "2022-04-14T00:06:35.569166"
    },
    {
        "id": 2,
        "amount": 2300.0,
        "tip": 230.0,
        "thumbnail": "abc",
        "createdAt": "2022-04-14T00:06:35.594497",
        "updatedAt": "2022-04-14T00:06:35.594497"
    },
    {
        "id": 5,
        "amount": 10.0,
        "tip": 230.0,
        "thumbnail": "abc",
        "createdAt": "2022-04-14T00:22:41.752192",
        "updatedAt": "2022-04-14T00:22:41.752192"
    }
]
```
### Get latest payment history item
* URI: "/api/v1/user/{userId}/payments/latest"
* Method: GET
* This api is to return the latest payment belongs to the user.
* HttpStatusCode 200 returned with a payment object if it is successful.
* HttpStatusCode 404 returned if the user is not exists or the user has no payments.
* The successful response looks like:
```
{
    "id": 2,
    "amount": 2300.0,
    "tip": 230.0,
    "thumbnail": "abc",
    "createdAt": "2022-04-14T00:06:35.594497",
    "updatedAt": "2022-04-14T00:06:35.594497"
}
```
### Save payment history item
* URI: "/api/v1/user/{userId}/payment"
* Method: POST
* This api is to create a new payment belongs to the user.
* HttpStatusCode 201 returned with a payment object if it is successful.
* HttpStatusCode 404 returned if the user is not exists.
* HttpStatusCode 500 returned if it is failed to save the payment.
* The successful response looks like:
```
{
    "id": 2,
    "amount": 2300.0,
    "tip": 230.0,
    "thumbnail": "abc",
    "createdAt": "2022-04-14T00:06:35.594497",
    "updatedAt": "2022-04-14T00:06:35.594497"
}
```
### Get total paid for a specific duration. Example: “User wants to know how much they have paid between 2021-01-23 and 2021-05-13”
* URI: "/api/v1/user/{userId}/payments/calculateTotal"
* Method: GET
* Query Param
  * from: the String value of date time to define which time to start looking for. Ex: 2021-01-01
  * to: the String value of date time to define which time to end looking for. Ex: 2023-01-01
* This api is to get total paid for a specific duration
* HttpStatusCode 200 returned with a integer number of total paid
* HttpStatusCode 404 returned if the user is not exists or not found payments
* The successful response looks like:
```
4400
```
### Get the most expensive history item for the user
* URI: "/api/v1/user/{userId}/payments/mostExpensive"
* Method: GET
* This api is to get the most expensive history item for the user
* HttpStatusCode 200 returned with a integer number of total paid
* HttpStatusCode 404 returned if the user is not exists or not found payments
* The successful response looks like:
```
{
    "id": 2,
    "amount": 2300.0,
    "tip": 230.0,
    "thumbnail": "abc",
    "createdAt": "2022-04-14T00:06:35.594497",
    "updatedAt": "2022-04-14T00:06:35.594497"
}
```
### Generate API Key to the admin user
* URI: "/api/v1/user/{userId}/generateApiKey"
* Method: GET
* This api is to get the api key which used to authenticate the admin user for calling management apis
* HttpStatusCode 201 returned with a encrypted string of a key stored in the auth table related to a particular user
* HttpStatusCode 404 returned if the user is not exists
* HttpStatusCode 403 returned if it is not an admin user
* The successful response looks like:
```
2HZDeUgNSAU/ugCfkDuHA8nUp4Vsa/yEfLi5260GCXqlP6Fs2vtOynwjYY854aXL
```

## Management APIs (authenticated by API Key in the header with the header name is "apiKey")
### Delete a payment history item
* URI: "api/v1/managements/payments/{id}"
* Method: DELETE
* This api is to delete a particular payment history item
* HttpStatusCode 200 returned with a successful message
* HttpStatusCode 404 returned if the payment not found
* HttpStatusCode 401 returned if the apiKey is invalid or not provided
* HttpStatusCode 403 returned if the user is not an admin
* The successful response looks like:
```
Payment was deleted successful
```
### Edit a payment history item
* URI: "api/v1/managements/payments/{id}"
* Method: PUT
* This api is to update a new payment belongs to the user.
* HttpStatusCode 200 returned with a payment object if it is successful.
* HttpStatusCode 404 returned if the payment is not exists.
* The successful response looks like:
```
{
    "id": 2,
    "amount": 2300.0,
    "tip": 230.0,
    "thumbnail": "abc",
    "createdAt": "2022-04-14T00:06:35.594497",
    "updatedAt": "2022-04-14T00:06:35.594497"
}
