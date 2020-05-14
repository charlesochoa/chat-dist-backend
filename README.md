## User

The model user to represent our chat users.

### `/users/add`
----
This endpoint creates a new user if the username doesn't exists.

* **Method:** `GET`

* **Headers:** `Authorization: Bearer <JWT token>`
  
* **URL Params:** `None`

* **Data Params** `None`

* **Success Response:**

  * **Code:** 200 <br />
    **Content:** `{ id : 12, name : "Michael Bloom" }`
 
* **Error Response:**

  * **Code:** 404 NOT FOUND <br />
    **Content:** `{ error : "User doesn't exist" }`

  OR

  * **Code:** 401 UNAUTHORIZED <br />
    **Content:** `{ error : "You are unauthorized to make this request." }`

* **Sample Call:**

  ```
  {
    status: "ok",
    message-type: "work-agency",
    message-version: "1.0.0",
    message: {
      DOI: "10.1037/0003-066x.59.1.29",
      agency: {
        id: "crossref",
        label: "Crossref"
      }
    }
  }
  ```
