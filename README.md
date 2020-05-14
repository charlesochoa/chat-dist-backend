## User

The model user to represent our chat users.

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
          "path": "/user/all"
        }
      ```
