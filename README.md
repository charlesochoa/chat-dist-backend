# API REST

This documentation contains all the endpoints in the chat application and how to use them.

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
This endpoint returns all the users registered in the application.

* **Method:** `GET`

* **Headers:** `Authorization: Bearer <JWT token>`
  
* **URL Params:** `None`

* **Body** `None`

* **Success Response:**

  * **Code:** `200 OK` <br />
    **Content:**   
      ```
        {
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
          "path": "/users/all"
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

* **Body** `None`

* **Success Response:**

  * **Code:** `200 OK` <br />
    **Content:**   
      ```
        {
            {
              "id": 30,
              "username": "user1",
              "email": null,
              "password":  "<BCrypt encoded password>",
              "bindingName": "user.user1",
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
        {
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
          "path": "/chatrooms/all"
        }
      ```

