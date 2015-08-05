package io.github.finagle.example.petstore

import javax.inject.Inject

import com.twitter.finagle.http.Request
import com.twitter.finatra.http.Controller

/**
 * A controller that invokes all the user service methods of the Petstore API. Supported routes include:
 *
 * GET: /user/:username (getUser)
 *    The username of the User to be found is passed in the path.
 *
 * POST: /user (addUser)
 *    The information of the added User is passed in the body.
 *
 * POST: /user/createWithList (addUser for every user in the list)
 *    The list of Users is passed in the body.
 *
 * POST: /user/createWithArray (addUser for every user in the array)
 *    The array of users is passed in the body.
 *
 * PUT: /user/:user (updateUser)
 *    The username of the User to be updated is passed in the path.
 *    The information with which to replace the old user with is passed in the body.
 *
 * DELETE: /user/:username (deleteUser)
 *    The username of the User to be deleted is passed in the path.
 *
 * @param petstoreDb The petstore instance on which to perform the methods.
 */
class PetstoreUserController @Inject()(petstoreDb: PetstoreDb) extends Controller {

  get("/user/:username") { request: Request =>
    petstoreDb.getUser(request.params("username"))
  }

  post("/user") { u: User =>
    petstoreDb.addUser(u)
  }

  post("/user/createWithList") { info: Seq[User] =>
    info.map(petstoreDb.addUser)
  }

  post("/user/createWithArray") { info: Seq[User] =>
    info.map(petstoreDb.addUser)
  }

  put("/user/:user") {info: UpdateUser =>
    print(info.user)
    petstoreDb.updateUser(
      User(info.id,
        info.username,
        info.firstName,
        info.lastName,
        info.email,
        info.password,
        info.phone))
  }

  delete("/user/:username") { request: Request =>
    petstoreDb.deleteUser(request.params("username"))
  }
}
