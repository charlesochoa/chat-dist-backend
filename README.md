## User

The model user to represent our chat users.

### `/users/add`
----
This endpoint creates a new user and returns the user that was created.

* **Method:** `POST`

* **Headers:** `Authorization: Bearer <JWT token>`
  
* **URL Params:** `None`

* **Data Params** `None`

* **Success Response:**

  * **Code:** `200 OK` <br />
    **Content:**   
      ```
        {
            {
              "id": 30,
              "username": "usuario",
              "email": null,
              "password":  "$2a$10$zuUhpyO76h55LeDfnrF5j.vaiAH8VBocyygLQiCX/uYKa4gA/Cs5u",
              "bindingName": "user.usuario",
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

* **Data Params** `None`

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
