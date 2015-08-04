package io.github.finagle.example.petstore

import javax.inject.Inject

import com.fasterxml.jackson.databind.ObjectMapper
import com.twitter.finagle.http.Request
import com.twitter.finatra.http.Controller

class PetstoreUserController @Inject()(petstoreDb: PetstoreDb, mapper: ObjectMapper) extends Controller {

  /**
   * Endpoint for addUser
   * The information of the added User is passed in the body.
   * @return A Router that contains a RequestReader of the username of the added User.
   */
  post("/user") { u: User =>
    petstoreDb.addUser(u)
  }

  /**
   * Endpoint for addUsersViaList
   * The list of Users is passed in the body.
   * @return A Router that contains a RequestReader of a sequence of the usernames of the Users added.
   */
  post("/user/createWithList") { info: Seq[User] =>
//    info: UsersFromSeq =>
//      info.users.map(petstoreDb.addUser)
    info.map(petstoreDb.addUser)
  }

  /**
   * Endpoint for addUsersViaArray
   * The array of users is passed in the body.
   * @return A Router that contains a RequestReader of a sequence of the usernames of the Users added.
   */
  post("/user/createWithArray") { info: Seq[User] =>
    info.map(petstoreDb.addUser)
//    info: UsersFromSeq =>
//    if (!info.users.isEmpty) info.users.map(petstoreDb.addUser)
//    else throw InvalidInput("You must enter an array of users!")
  }

  /**
   * Endpoint for deleteUser
   * The username of the User to be deleted is passed in the path.
   * @return A Router that contains essentially nothing unless an error is thrown.
   */
  delete("/user/:username") { request: Request =>
    request.params.get("username") match {
      case Some(u) => petstoreDb.deleteUser(u)
      case None => throw InvalidInput("Must give the username of the user to be deleted!")
    }
  }

  /**
   * Endpoint for getUser
   * The username of the User to be found is passed in the path.
   * @return A Router that contains the User in question.
   */
  get("/user/:username") { request: Request =>
    request.params.get("username") match {
      case Some(u) => petstoreDb.getUser(u)
      case None => throw InvalidInput("Must give the username of the user to be found!")
    }
  }

  /**
   * Endpoint for updateUser
   * The username of the User to be updated is passed in the path.
   * The information with which to replace the old user with is passed in the body.
   * @return A Router that contains a RequestReader of the User updated.
   */
   put("/user/:user") {info: UpdateUser =>

    //ADD ERROR CHECKING

     petstoreDb.updateUser(
       User(info.id,
         info.username,
         info.firstName,
         info.lastName,
         info.email,
         info.password,
         info.phone))

//     petstoreDb.updateUser(info.betterUser.getOrElse(
//       throw InvalidInput("Must pass information to update the user with!")))








//
//     info.username match {
//      case u: String => petstoreDb.updateUser(info.betterUser.getOrElse(
//        throw InvalidInput("Must pass information to update the user with!")))
//      case _ => throw InvalidInput("Must give the username of the user to be updated!")
//     }
  }
}