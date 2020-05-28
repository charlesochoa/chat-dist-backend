# Chat Dist Backend API REST

This documentation contains all the endpoints in the chat application and how to use them.

## Authentication

This controller is used for the authentication methods.

### `/auth/sign-up`
----
This endpoint creates a new user and returns the user that was created.

* **Method:** `POST`

* **Headers:** `None`
  
* **URL Params:** `None`

* **Body:**
    ```
      {
        "username": "user1",
        "email": "user1@gmail.com",
        "password":  "<password>",
      }
    ```

* **Success Response:**

  * **Code:** `200 OK` <br />
    **Content:**   
      ```
        {
          "id": 30,
          "username": "user1",
          "email": "user1@gmail.com",
          "password":  "<BCrypt encoded password>",
          "bindingName": "user.user1",
          "roles": []
        }
      ```
 
* **Error Response:**

  * **Code:** `400 BAD REQUEST` <br />
    **Content:**
      ```
        {
          "timestamp": "2020-05-14T15:25:59.186+0000",
          "status": 400,
          "error": "Bad Request",
          "message": "Username already exists",
          "path": "/auth/sign-up"
        }
      ```

### `/login`
----
This endpoint creates authenticates the user and returns a jwt token.

* **Method:** `POST`

* **Headers:** `None`
  
* **URL Params:** `None`

* **Body:**
    ```
      {
        "username": "user1",
        "password":  "<password>",
      }
    ```

* **Success Response:**

  * **Code:** `200 OK` <br />
    **Content:**   
      ```
        {
          "Authorization": "Bearer <JWT token>"
        }
      ```
 
* **Error Response:**

  * **Code:** `400 BAD REQUEST` <br />
    **Content:**
      ```
        {
          "timestamp": "2020-05-14T15:25:59.186+0000",
          "status": 400,
          "error": "Bad Request",
          "message": "Bad credentials",
          "path": "/login"
        }
      ```

## User

The model used to represent the users in the chat application.

### `/users/add`
----
This endpoint creates a new user and returns the user that was created.

* **Method:** `POST`

* **Headers:** `Authorization: Bearer <JWT token>`
  
* **URL Params:** `None`

* **Body:**
    ```
      {
        "username": "user1",
        "email": "user1@gmail.com",
        "password":  "<password>",
      }
    ```

* **Success Response:**

  * **Code:** `200 OK` <br />
    **Content:**   
      ```
        {
          "id": 30,
          "username": "user1",
          "email": "user1@gmail.com",
          "password":  "<BCrypt encoded password>",
          "bindingName": "user.user1",
          "roles": []
        }
      ```
 
* **Error Response:**

  * **Code:** `403 FORBIDDEN` <br />
    **Content:**
      ```
        {
          "timestamp": "2020-05-14T15:11:57.677+0000",
          "status": 403,
          "error": "Forbidden",
          "message": "Access Denied",
          "path": "/users/add"
        }
      ```
  OR

  * **Code:** `400 BAD REQUEST` <br />
    **Content:**
      ```
        {
          "timestamp": "2020-05-14T15:25:59.186+0000",
          "status": 400,
          "error": "Bad Request",
          "message": "Username already exists",
          "path": "/users/add"
        }
      ```

### `/users/all`
----
This endpoint returns all the users in the application.

* **Method:** `GET`

* **Headers:** `Authorization: Bearer <JWT token>`
  
* **URL Params:** `None`

* **Body** `None`

* **Success Response:**

  * **Code:** `200 OK` <br />
    **Content:**   
      ```
        [
          {
            "id": 1,
            "username": "user1",
            "email": null,
            "password": "<BCrypt encoded password>",
            "bindingName": "user.user1",
            "roles": []
          },
          {
            "id": 2,
            "username": "user2",
            "email": null,
            "password": "<BCrypt encoded password>",
            "bindingName": "user.user2",
            "roles": []
          }
        ]
      ```
 
* **Error Response:**

  * **Code:** `403 FORBIDDEN` <br />
    **Content:**
      ```
        {
          "timestamp": "2020-05-14T15:11:57.677+0000",
          "status": 403,
          "error": "Forbidden",
          "message": "Access Denied",
          "path": "/users/all"
        }
      ```

### `/users/all/normal`
----
This endpoint returns the list of users with role "NORMAL".

* **Method:** `GET`

* **Headers:** `Authorization: Bearer <JWT token>`
  
* **URL Params:** `None`

* **Body** `None`

* **Success Response:**

  * **Code:** `200 OK` <br />
    **Content:**   
      ```
        [
          {
            "id": 1,
            "username": "user1",
            "email": null,
            "password": "<BCrypt encoded password>",
            "bindingName": "user.user1",
            "roles": [
              {
                "id": 2,
                "value": "NORMAL"
              }
            ]
          },
          {
            "id": 2,
            "username": "user2",
            "email": null,
            "password": "<BCrypt encoded password>",
            "bindingName": "user.user2",
            "roles": [
              {
                "id": 2,
                "value": "NORMAL"
              }
            ]
          }
        ]
      ```
 
* **Error Response:**

  * **Code:** `403 FORBIDDEN` <br />
    **Content:**
      ```
        {
          "timestamp": "2020-05-14T15:11:57.677+0000",
          "status": 403,
          "error": "Forbidden",
          "message": "Access Denied",
          "path": "/users/all/normal"
        }
      ```

### `/users/{username}`
----
This endpoint returns the user with the given username.

* **Method:** `GET`

* **Headers:** `Authorization: Bearer <JWT token>`
  
* **URL Params:** `None`

* **Body** `None`

* **Success Response:**

  * **Code:** `200 OK` <br />
    **Content:**   
      ```
        {
          "id": 1,
          "username": "user1",
          "email": null,
          "password": "<BCrypt encoded password>",
          "bindingName": "user.user1",
          "roles": [
            {
              "id": 2,
              "value": "NORMAL"
            }
          ]
        }
      ```
 
* **Error Response:**

  * **Code:** `403 FORBIDDEN` <br />
    **Content:**
      ```
        {
          "timestamp": "2020-05-14T15:11:57.677+0000",
          "status": 403,
          "error": "Forbidden",
          "message": "Access Denied",
          "path": "/users/user1"
        }
      ```
  OR

  * **Code:** `404 NOT FOUND` <br />
    **Content:**
      ```
        {
          "timestamp": "2020-05-28T20:40:26.699+0000",
          "status": 404,
          "error": "Not Found",
          "message": "User not found",
          "path": "/users/user111"
        } 
      ```    

## Chatroom

The model used to represent the chatroom in the chat application.

### `/chatrooms/add`
----
This endpoint creates a new chatroom and returns the chatroom that was created.

* **Method:** `POST`

* **Headers:** `Authorization: Bearer <JWT token>`
  
* **URL Params:** `None`

* **Body**
    ```
      {
        "users": [
          {
            "id": 10,
            "username": "user10",
            "email": "user10@gmail.com",
            "password": "<BCrypt encoded password>",
            "bindingName": "user.user10",
            "roles": []
          }
        ],
        "admin": {
          "id": 10,
          "username": "user10",
          "email": "user10@gmail.com",
          "password": "<BCrypt encoded password>",
          "bindingName": "user.user10",
          "roles": []
        },
        "bindingName": "chatroom.1.user10",
        "name": "Chatroom 1"
      }
    ```

* **Success Response:**

  * **Code:** `200 OK` <br />
    **Content:**   
      ```
        {
          "id": 1,
          "users": [
            {
              "id": 10,
              "username": "user10",
              "email": "user10@gmail.com",
              "password": "<BCrypt encoded password>",
              "bindingName": "user.user10",
              "roles": []
            }
          ],
          "admin": {
            "id": 10,
            "username": "user10",
            "email": "user10@gmail.com",
            "password": "<BCrypt encoded password>",
            "bindingName": "user.user10",
            "roles": []
          },
          "bindingName": "chatroom.1.user10",
          "name": "Chatroom 1"
        }
      ```
 
* **Error Response:**

  * **Code:** `403 FORBIDDEN` <br />
    **Content:**
      ```
        {
          "timestamp": "2020-05-14T15:11:57.677+0000",
          "status": 403,
          "error": "Forbidden",
          "message": "Access Denied",
          "path": "/chatrooms/add"
        }
      ```
  OR

  * **Code:** `400 BAD REQUEST` <br />
    **Content:**
      ```
        {
          "timestamp": "2020-05-14T15:25:59.186+0000",
          "status": 400,
          "error": "Bad Request",
          "message": "Username already exists",
          "path": "/chatrooms/add"
        }
      ```

### `/chatrooms/all`
----
This endpoint returns all the chatrooms in the application.

* **Method:** `GET`

* **Headers:** `Authorization: Bearer <JWT token>`
  
* **URL Params:** `None`

* **Body** `None`

* **Success Response:**

  * **Code:** `200 OK` <br />
    **Content:**   
      ```
        [
          {
            "id": 1,
            "users": [
              {
                "id": 10,
                "username": "user10",
                "email": "user10@gmail.com",
                "password": "<BCrypt encoded password>",
                "bindingName": "user.user10",
                "roles": []
              }
            ],
            "admin": {
              "id": 10,
              "username": "user10",
              "email": "user10@gmail.com",
              "password": "<BCrypt encoded password>",
              "bindingName": "user.user10",
              "roles": []
            },
            "bindingName": "chatroom.1.user10",
            "name": "Chatroom 1"
          },
          {
            "id": 2,
            "users": [
              {
                "id": 10,
                "username": "user10",
                "email": "user10@gmail.com",
                "password": "<BCrypt encoded password>",
                "bindingName": "user.user10",
                "roles": []
              }
            ],
            "admin": {
              "id": 10,
              "username": "user10",
              "email": "user10@gmail.com",
              "password": "<BCrypt encoded password>",
              "bindingName": "user.user10",
              "roles": []
            },
            "bindingName": "chatroom.2.user10",
            "name": "Chatroom 2"
          },
        ]
      ```
 
* **Error Response:**

  * **Code:** `403 FORBIDDEN` <br />
    **Content:**
      ```
        {
          "timestamp": "2020-05-14T15:11:57.677+0000",
          "status": 403,
          "error": "Forbidden",
          "message": "Access Denied",
          "path": "/chatrooms/all"
        }
      ```

### `/chatrooms/all/{userId}`
----
This endpoint returns all the chatrooms where the user is either a participant.

* **Method:** `GET`

* **Headers:** `Authorization: Bearer <JWT token>`
  
* **URL Params:** `None`

* **Body** `None`

* **Success Response:**

  * **Code:** `200 OK` <br />
    **Content:**   
      ```
        [
          {
            "id": 1,
            "users": [
              {
                "id": 10,
                "username": "user10",
                "email": "user10@gmail.com",
                "password": "<BCrypt encoded password>",
                "bindingName": "user.user10",
                "roles": []
              }
            ],
            "admin": {
              "id": 10,
              "username": "user10",
              "email": "user10@gmail.com",
              "password": "<BCrypt encoded password>",
              "bindingName": "user.user10",
              "roles": []
            },
            "bindingName": "chatroom.1.user10",
            "name": "Chatroom 1"
          },
          {
            "id": 2,
            "users": [
              {
                "id": 10,
                "username": "user10",
                "email": "user10@gmail.com",
                "password": "<BCrypt encoded password>",
                "bindingName": "user.user10",
                "roles": []
              }
            ],
            "admin": {
              "id": 10,
              "username": "user10",
              "email": "user10@gmail.com",
              "password": "<BCrypt encoded password>",
              "bindingName": "user.user10",
              "roles": []
            },
            "bindingName": "chatroom.2.user10",
            "name": "Chatroom 2"
          },
        ]
      ```
 
* **Error Response:**

  * **Code:** `403 FORBIDDEN` <br />
    **Content:**
      ```
        {
          "timestamp": "2020-05-14T15:11:57.677+0000",
          "status": 403,
          "error": "Forbidden",
          "message": "Access Denied",
          "path": "/chatrooms/all"
        }
      ```
  OR

  * **Code:** `404 NOT FOUND` <br />
    **Content:**
      ```
        {
          "timestamp": "2020-05-28T20:50:41.395+0000",
          "status": 404,
          "error": "Not Found",
          "message": "User not found",
          "path": "/chatrooms/all/1"
        } 
      ```

### `/chatrooms/{id}/users`
----
This endpoint returns all the participants in the given chatroom.

* **Method:** `GET`

* **Headers:** `Authorization: Bearer <JWT token>`
  
* **URL Params:** `None`

* **Body** `None`

* **Success Response:**

  * **Code:** `200 OK` <br />
    **Content:**   
      ```
        [
          {
            "id": 1,
            "username": "user1",
            "email": null,
            "password": "<BCrypt encoded password>",
            "bindingName": "user.user1",
            "roles": [
              {
                "id": 2,
                "value": "NORMAL"
              }
            ]
          },
          {
            "id": 2,
            "username": "user2",
            "email": null,
            "password": "<BCrypt encoded password>",
            "bindingName": "user.user2",
            "roles": [
              {
                "id": 2,
                "value": "NORMAL"
              }
            ]
          }
        ]
      ```
 
* **Error Response:**

  * **Code:** `403 FORBIDDEN` <br />
    **Content:**
      ```
        {
          "timestamp": "2020-05-14T15:11:57.677+0000",
          "status": 403,
          "error": "Forbidden",
          "message": "Access Denied",
          "path": "/chatrooms/1/users"
        }
      ```
  OR

  * **Code:** `404 NOT FOUND` <br />
    **Content:**
      ```
        {
          "timestamp": "2020-05-28T20:54:33.021+0000",
          "status": 404,
          "error": "Not Found",
          "message": "Chatroom not found",
          "path": "/chatrooms/1/users"
        } 
      ```

### `/chatrooms/{id}/add-user/{userId}`
----
This endpoint adds the user to the chatroom's set of users.

* **Method:** `POST`

* **Headers:** `Authorization: Bearer <JWT token>`
  
* **URL Params:** `None`

* **Body** `None`

* **Success Response:**

  * **Code:** `200 OK` <br />
    **Content:**   
      ```
        [
          {
            "id": 1,
            "users": [
              {
                "id": 10,
                "username": "user10",
                "email": "user10@gmail.com",
                "password": "<BCrypt encoded password>",
                "bindingName": "user.user10",
                "roles": []
              }
            ],
            "admin": {
              "id": 10,
              "username": "user10",
              "email": "user10@gmail.com",
              "password": "<BCrypt encoded password>",
              "bindingName": "user.user10",
              "roles": []
            },
            "bindingName": "chatroom.1.user10",
            "name": "Chatroom 1"
          },
          {
            "id": 2,
            "users": [
              {
                "id": 10,
                "username": "user10",
                "email": "user10@gmail.com",
                "password": "<BCrypt encoded password>",
                "bindingName": "user.user10",
                "roles": []
              },
              {
                "id": 11,
                "username": "user11",
                "email": "user11@gmail.com",
                "password": "<BCrypt encoded password>",
                "bindingName": "user.user11",
                "roles": []
              }
            ],
            "admin": {
              "id": 10,
              "username": "user10",
              "email": "user10@gmail.com",
              "password": "<BCrypt encoded password>",
              "bindingName": "user.user10",
              "roles": []
            },
            "bindingName": "chatroom.2.user10",
            "name": "Chatroom 2"
          },
        ]
      ```
 
* **Error Response:**

  * **Code:** `403 FORBIDDEN` <br />
    **Content:**
      ```
        {
          "timestamp": "2020-05-14T15:11:57.677+0000",
          "status": 403,
          "error": "Forbidden",
          "message": "Access Denied",
          "path": "/chatrooms/2/add-user/32"
        }
      ```
  OR

  * **Code:** `404 NOT FOUND` <br />
    **Content:**
      ```
        {
          "timestamp": "2020-05-28T21:01:05.070+0000",
          "status": 404,
          "error": "Not Found",
          "message": "Chatroom not found",
          "path": "/chatrooms/2/add-user/32"
        }
      ```
  OR

  * **Code:** `404 NOT FOUND` <br />
    **Content:**
      ```
        {
          "timestamp": "2020-05-28T21:00:04.522+0000",
          "status": 404,
          "error": "Not Found",
          "message": "User not found",
          "path": "/chatrooms/43/add-user/100"
        }
      ```

### `/chatrooms/{id}`
----
This endpoint deletes the given chatroom from the database.

* **Method:** `DELETE`

* **Headers:** `Authorization: Bearer <JWT token>`
  
* **URL Params:** `None`

* **Body** `None`

* **Success Response:**

  * **Code:** `200 OK` <br />
    **Content:** `None`
 
* **Error Response:**

  * **Code:** `403 FORBIDDEN` <br />
    **Content:**
      ```
        {
          "timestamp": "2020-05-14T15:11:57.677+0000",
          "status": 403,
          "error": "Forbidden",
          "message": "Access Denied",
          "path": "/chatrooms/1"
        }
      ```
  OR

  * **Code:** `404 NOT FOUND` <br />
    **Content:**
      ```
        {
          "timestamp": "2020-05-28T21:01:05.070+0000",
          "status": 404,
          "error": "Not Found",
          "message": "Chatroom not found",
          "path": "/chatrooms/2"
        }
      ```
  OR

  * **Code:** `403 FORBIDDEN` <br />
    **Content:**
      ```
        {
          "timestamp": "2020-05-28T21:00:04.522+0000",
          "status": 403,
          "error": "Forbidden",
          "message": "Forbidden",
          "path": "/chatrooms/43"
        }
      ```

## Direct Message

The model used to represent the direct message in the chat application, the direct message is 
a message between two different users.

### `/direct-messages/send`
----
This endpoint adds a direct message to the database and sends the message to RabbitMQ.

* **Method:** `POST`

* **Headers:** `Authorization: Bearer <JWT token>`
  
* **URL Params:** `None`

* **Body**
    ```
      {
        "sender": {
            "id": 1,
            "username": "user1",
            "email": "user1@gmail.com",
            "password": "<BCrypt encoded password>",
            "bindingName": "user.user1",
            "roles": []
        },
        "time": "1970-01-01T00:00:00.000+0000",
        "content": "A new message",
        "text": true,
        "bytes": 0,
        "receiver": {
            "id": 2,
            "username": "user2",
            "email": "user2@gmail.com",
            "password": "<BCrypt encoded password>",
            "bindingName": "user.user2",
            "roles": []
        }
      }
    ```

* **Success Response:**

  * **Code:** `200 OK` <br />
    **Content:**   
      ```
        {
          "id": 1,
          "sender": {
              "id": 1,
              "username": "user1",
              "email": "user1@gmail.com",
              "password": "<BCrypt encoded password>",
              "bindingName": "user.user1",
              "roles": []
          },
          "time": "1970-01-01T00:00:00.000+0000",
          "content": "A new message",
          "text": true,
          "bytes": 0,
          "receiver": {
              "id": 2,
              "username": "user2",
              "email": "user2@gmail.com",
              "password": "<BCrypt encoded password>",
              "bindingName": "user.user2",
              "roles": []
          }
        }
      ```
 
* **Error Response:**

  * **Code:** `403 FORBIDDEN` <br />
    **Content:**
      ```
        {
          "timestamp": "2020-05-14T15:11:57.677+0000",
          "status": 403,
          "error": "Forbidden",
          "message": "Access Denied",
          "path": "/direct-messages/send"
        }
      ```
  OR

  * **Code:** `403 FORBIDDEN` <br />
    **Content:**
      ```
        {
          "timestamp": "2020-05-28T21:00:04.522+0000",
          "status": 403,
          "error": "Forbidden",
          "message": "Message too long",
          "path": "/direct-messages/send"
        }
      ```

### `/direct-messages/send-all`
----
This endpoint adds a direct message to the database and sends the message to RabbitMQ.

* **Method:** `POST`

* **Headers:** `Authorization: Bearer <JWT token>`
  
* **URL Params:** `None`

* **Body**
    ```
      {
        "sender": {
            "id": 1,
            "username": "admin",
            "email": "admin@gmail.com",
            "password": "<BCrypt encoded password>",
            "bindingName": "user.admin",
            "roles": [
              {
                "id": 1,
                "value": "ADMIN"
              }
            ]
        },
        "time": "1970-01-01T00:00:00.000+0000",
        "content": "A new message",
        "text": true,
        "bytes": 0,
        "receiver": null
      }
    ```

* **Success Response:**

  * **Code:** `200 OK` <br />
    **Content:**   
      ```
        {
          "id": 1,
          "sender": {
              "id": 1,
              "username": "admin",
              "email": "admin@gmail.com",
              "password": "<BCrypt encoded password>",
              "bindingName": "user.admin",
              "roles": [
                {
                  "id": 1,
                  "value": "ADMIN"
                }
              ]
          },
          "time": "1970-01-01T00:00:00.000+0000",
          "content": "A new message",
          "text": true,
          "bytes": 0,
          "receiver": null
        }
      ```
 
* **Error Response:**

  * **Code:** `403 FORBIDDEN` <br />
    **Content:**
      ```
        {
          "timestamp": "2020-05-14T15:11:57.677+0000",
          "status": 403,
          "error": "Forbidden",
          "message": "Access Denied",
          "path": "/direct-messages/send-all"
        }
      ```
  OR

  * **Code:** `403 FORBIDDEN` <br />
    **Content:**
      ```
        {
          "timestamp": "2020-05-28T21:00:04.522+0000",
          "status": 403,
          "error": "Forbidden",
          "message": "Message too long",
          "path": "/direct-messages/send-all"
        }
      ```
  OR

  * **Code:** `403 FORBIDDEN` <br />
    **Content:**
      ```
        {
          "timestamp": "2020-05-28T21:00:04.522+0000",
          "status": 403,
          "error": "Forbidden",
          "message": "Forbidden",
          "path": "/direct-messages/send-all"
        }
      ```

### `/direct-messages/all`
----
This endpoint returns all the direct messages in the application.

* **Method:** `GET`

* **Headers:** `Authorization: Bearer <JWT token>`
  
* **URL Params:** `None`

* **Body** `None`

* **Success Response:**

  * **Code:** `200 OK` <br />
    **Content:**   
      ```
       [
          {
            "id": 1,
            "sender": {
                "id": 1,
                "username": "user1",
                "email": "user1@gmail.com",
                "password": "<BCrypt encoded password>",
                "bindingName": "user.user1",
                "roles": []
            },
            "time": "1970-01-01T00:00:00.000+0000",
            "content": "A new message",
            "text": true,
            "bytes": 0,
            "receiver": {
                "id": 2,
                "username": "user2",
                "email": "user2@gmail.com",
                "password": "<BCrypt encoded password>",
                "bindingName": "user.user2",
                "roles": []
            }
          },
          {
            "id": 2,
            "sender": {
                "id": 3,
                "username": "user3",
                "email": "user3@gmail.com",
                "password": "<BCrypt encoded password>",
                "bindingName": "user.user3",
                "roles": []
            },
            "time": "1970-01-01T00:00:00.000+0000",
            "content": "asdfasdfasdfasdf",
            "text": true,
            "bytes": 0,
            "receiver": {
                "id": 1,
                "username": "user1",
                "email": "user1@gmail.com",
                "password": "<BCrypt encoded password>",
                "bindingName": "user.user1",
                "roles": []
            }
          }
        ]
      ```
 
* **Error Response:**

  * **Code:** `403 FORBIDDEN` <br />
    **Content:**
      ```
        {
          "timestamp": "2020-05-14T15:11:57.677+0000",
          "status": 403,
          "error": "Forbidden",
          "message": "Access Denied",
          "path": "/direct-messages/all"
        }
      ```

### `/direct-messages/all/{userId}`
----
This endpoint returns all the direct messages where the given user is either the sender or receiver.

* **Method:** `GET`

* **Headers:** `Authorization: Bearer <JWT token>`
  
* **URL Params:** `None`

* **Body** `None`

* **Success Response:**

  * **Code:** `200 OK` <br />
    **Content:**   
      ```
       [
          {
            "id": 1,
            "sender": {
                "id": 1,
                "username": "user1",
                "email": "user1@gmail.com",
                "password": "<BCrypt encoded password>",
                "bindingName": "user.user1",
                "roles": []
            },
            "time": "1970-01-01T00:00:00.000+0000",
            "content": "A new message",
            "text": true,
            "bytes": 0,
            "receiver": {
                "id": 2,
                "username": "user2",
                "email": "user2@gmail.com",
                "password": "<BCrypt encoded password>",
                "bindingName": "user.user2",
                "roles": []
            }
          },
          {
            "id": 2,
            "sender": {
                "id": 3,
                "username": "user3",
                "email": "user3@gmail.com",
                "password": "<BCrypt encoded password>",
                "bindingName": "user.user3",
                "roles": []
            },
            "time": "1970-01-01T00:00:00.000+0000",
            "content": "asdfasdfasdfasdf",
            "text": true,
            "bytes": 0,
            "receiver": {
                "id": 1,
                "username": "user1",
                "email": "user1@gmail.com",
                "password": "<BCrypt encoded password>",
                "bindingName": "user.user1",
                "roles": []
            }
          }
        ]
      ```
 
* **Error Response:**

  * **Code:** `403 FORBIDDEN` <br />
    **Content:**
      ```
        {
          "timestamp": "2020-05-14T15:11:57.677+0000",
          "status": 403,
          "error": "Forbidden",
          "message": "Access Denied",
          "path": "/direct-messages/all/{userId}"
        }
      ```
  OR

  * **Code:** `404 NOT FOUND` <br />
    **Content:**
      ```
        {
          "timestamp": "2020-05-28T21:00:04.522+0000",
          "status": 403,
          "error": "Not Found",
          "message": "User not found",
          "path": "/direct-messages/all/{userId}"
        }
      ```

## Group Message

The model used to represent the group message in the chat application, the group message is a message sent from an user 
to a chatroom.

### `/group-messages/send/{chatroomId}`
----
This endpoint adds a group message to the database and sends the message to RabbitMQ.

* **Method:** `POST`

* **Headers:** `Authorization: Bearer <JWT token>`
  
* **URL Params:** `None`

* **Body**
    ```
      {
        "sender": {
          "id": 38,
          "username": "user3",
          "email": null,
          "password": "$2a$10$dNJeCWSb9SABZQqPDQlIjupTqAiLSPy1b3ULILzGSMSCc6kJLJFcu",
          "bindingName": "user.user3",
          "roles": [
            {
              "id": 2,
              "value": "NORMAL"
            }
          ]
        },
        "time": "2020-05-26T08:24:02.802+0000",
        "content": "Primer mensaje de prueba",
        "text": true,
        "bytes": 0
      }
    ```

* **Success Response:**

  * **Code:** `200 OK` <br />
    **Content:**   
      ```
        {
          "id": 43,
          "sender": {
            "id": 38,
            "username": "user3",
            "email": null,
            "password": "$2a$10$dNJeCWSb9SABZQqPDQlIjupTqAiLSPy1b3ULILzGSMSCc6kJLJFcu",
            "bindingName": "user.user3",
            "roles": [
              {
                "id": 2,
                "value": "NORMAL"
              }
            ]
          },
          "time": "2020-05-26T08:24:02.802+0000",
          "content": "Primer mensaje de prueba",
          "text": true,
          "bytes": 0,
          "chatRoom": {
            "id": 43,
            "users": [
              {
                "id": 36,
                "username": "user1",
                "email": null,
                "password": "$2a$10$1p2pq66i2RHaZtjbI32aUu8myBiqcA./SRUOsPeYr8UL6QH0nACh6",
                "bindingName": "user.user1",
                "roles": [
                  {
                    "id": 2,
                    "value": "NORMAL"
                  }
                ]
              },
              {
                "id": 37,
                "username": "user2",
                "email": null,
                "password": "$2a$10$oFL1Dav9pJJXdfalMTgQ/uO85MKLfdmfHXRdLa3YFulini8eaJmIS",
                "bindingName": "user.user2",
                "roles": [
                  {
                    "id": 2,
                    "value": "NORMAL"
                  }
                ]
              }
            ],
            "admin": {
              "id": 36,
              "username": "user1",
              "email": null,
              "password": "$2a$10$1p2pq66i2RHaZtjbI32aUu8myBiqcA./SRUOsPeYr8UL6QH0nACh6",
              "bindingName": "user.user1",
              "roles": [
                {
                  "id": 2,
                  "value": "NORMAL"
                }
              ]
            },
            "bindingName": "chatroom.43.user1",
            "name": "grupo con 1, 2 y 3"
          }
        }
      ```
 
* **Error Response:**

  * **Code:** `403 FORBIDDEN` <br />
    **Content:**
      ```
        {
          "timestamp": "2020-05-14T15:11:57.677+0000",
          "status": 403,
          "error": "Forbidden",
          "message": "Access Denied",
          "path": "/group-messages/send/32"
        }
      ```
  OR

  * **Code:** `403 FORBIDDEN` <br />
    **Content:**
      ```
        {
          "timestamp": "2020-05-28T21:00:04.522+0000",
          "status": 403,
          "error": "Forbidden",
          "message": "Message too long",
          "path": "/group-messages/send/33"
        }
      ```
  OR

  * **Code:** `404 NOT FOUND` <br />
    **Content:**
      ```
        {
          "timestamp": "2020-05-28T21:01:05.070+0000",
          "status": 404,
          "error": "Not Found",
          "message": "Chatroom not found",
          "path": "/group-messages/send/33"
        }
      ```

### `/group-messages/all`
----
This endpoint returns all the group messages in the application.

* **Method:** `GET`

* **Headers:** `Authorization: Bearer <JWT token>`
  
* **URL Params:** `None`

* **Body** `None`

* **Success Response:**

  * **Code:** `200 OK` <br />
    **Content:**   
      ```
        [
          {
            "id": 44,
            "sender": {
              "id": 38,
              "username": "user3",
              "email": null,
              "password": "$2a$10$dNJeCWSb9SABZQqPDQlIjupTqAiLSPy1b3ULILzGSMSCc6kJLJFcu",
              "bindingName": "user.user3",
              "roles": [
                {
                  "id": 2,
                  "value": "NORMAL"
                }
              ]
            },
            "time": "2020-05-26T08:24:02.802+0000",
            "content": "Primer mensaje de prueba",
            "text": true,
            "bytes": 0,
            "chatRoom": {
              "id": 43,
              "users": [
                {
                  "id": 36,
                  "username": "user1",
                  "email": null,
                  "password": "$2a$10$1p2pq66i2RHaZtjbI32aUu8myBiqcA./SRUOsPeYr8UL6QH0nACh6",
                  "bindingName": "user.user1",
                  "roles": [
                    {
                      "id": 2,
                      "value": "NORMAL"
                    }
                  ]
                },
                {
                  "id": 37,
                  "username": "user2",
                  "email": null,
                  "password": "$2a$10$oFL1Dav9pJJXdfalMTgQ/uO85MKLfdmfHXRdLa3YFulini8eaJmIS",
                  "bindingName": "user.user2",
                  "roles": [
                    {
                      "id": 2,
                      "value": "NORMAL"
                    }
                  ]
                }
              ],
              "admin": {
                "id": 36,
                "username": "user1",
                "email": null,
                "password": "$2a$10$1p2pq66i2RHaZtjbI32aUu8myBiqcA./SRUOsPeYr8UL6QH0nACh6",
                "bindingName": "user.user1",
                "roles": [
                  {
                    "id": 2,
                    "value": "NORMAL"
                  }
                ]
              },
              "bindingName": "chatroom.43.user1",
              "name": "grupo con 1, 2 y 3"
            }
          }
        ]
      ```
 
* **Error Response:**

  * **Code:** `403 FORBIDDEN` <br />
    **Content:**
      ```
        {
          "timestamp": "2020-05-14T15:11:57.677+0000",
          "status": 403,
          "error": "Forbidden",
          "message": "Access Denied",
          "path": "/group-messages/all"
        }
      ```

### `/group-messages/all/{chatroomId}`
----
This endpoint returns all the group messages in the given chatroom.

* **Method:** `GET`

* **Headers:** `Authorization: Bearer <JWT token>`
  
* **URL Params:** `None`

* **Body** `None`

* **Success Response:**

  * **Code:** `200 OK` <br />
    **Content:**   
      ```
        [
          {
            "id": 44,
            "sender": {
              "id": 38,
              "username": "user3",
              "email": null,
              "password": "$2a$10$dNJeCWSb9SABZQqPDQlIjupTqAiLSPy1b3ULILzGSMSCc6kJLJFcu",
              "bindingName": "user.user3",
              "roles": [
                {
                  "id": 2,
                  "value": "NORMAL"
                }
              ]
            },
            "time": "2020-05-26T08:24:02.802+0000",
            "content": "Primer mensaje de prueba",
            "text": true,
            "bytes": 0,
            "chatRoom": {
              "id": 43,
              "users": [
                {
                  "id": 36,
                  "username": "user1",
                  "email": null,
                  "password": "$2a$10$1p2pq66i2RHaZtjbI32aUu8myBiqcA./SRUOsPeYr8UL6QH0nACh6",
                  "bindingName": "user.user1",
                  "roles": [
                    {
                      "id": 2,
                      "value": "NORMAL"
                    }
                  ]
                },
                {
                  "id": 37,
                  "username": "user2",
                  "email": null,
                  "password": "$2a$10$oFL1Dav9pJJXdfalMTgQ/uO85MKLfdmfHXRdLa3YFulini8eaJmIS",
                  "bindingName": "user.user2",
                  "roles": [
                    {
                      "id": 2,
                      "value": "NORMAL"
                    }
                  ]
                }
              ],
              "admin": {
                "id": 36,
                "username": "user1",
                "email": null,
                "password": "$2a$10$1p2pq66i2RHaZtjbI32aUu8myBiqcA./SRUOsPeYr8UL6QH0nACh6",
                "bindingName": "user.user1",
                "roles": [
                  {
                    "id": 2,
                    "value": "NORMAL"
                  }
                ]
              },
              "bindingName": "chatroom.43.user1",
              "name": "grupo con 1, 2 y 3"
            }
          }
        ]
      ```
 
* **Error Response:**

  * **Code:** `403 FORBIDDEN` <br />
    **Content:**
      ```
        {
          "timestamp": "2020-05-14T15:11:57.677+0000",
          "status": 403,
          "error": "Forbidden",
          "message": "Access Denied",
          "path": "/group-messages/all/12"
        }
      ```
  OR

  * **Code:** `404 NOT FOUND` <br />
    **Content:**
      ```
        {
          "timestamp": "2020-05-28T21:01:05.070+0000",
          "status": 404,
          "error": "Not Found",
          "message": "Chatroom not found",
          "path": "/group-messages/all/33"
        }
      ```
