package io.github.finagle.example.petstore

import javax.inject.Inject
import com.twitter.finagle.http.Request
import com.twitter.finatra.http.Controller
import com.twitter.util.Future

// Guice
class PetstorePetController @Inject()(petstoreDb: PetstoreDb) extends Controller {

  /**
   * Endpoint for getPet
   * The long passed in the path becomes the ID of the Pet fetched.
   * @return A Router that contains the Pet fetched.
   */
  get("/pet/:id") { request: Request =>
    request.params.getLong("id") match {
      case Some(id) => petstoreDb.getPet(id)
      case None => throw MissingIdentifier("You must enter the id of a pet to find it!")
//      case None => Future.exception(new Exception)
    }
  }

  /**
   * Endpoint for addPet
   * The pet to be added must be passed in the body.
   * @return A Router that contains a RequestReader of the ID of the Pet added.
   */
  post("/pet") { pet: Pet =>
    petstoreDb.addPet(pet)
  }

  /**
   * Endpoint for updatePet
   * The updated, better version of the current pet must be passed in the body.
   * @return A Router that contains a RequestReader of the updated Pet.
   */
  put("/pet") { pet: Pet =>
    val identifier: Long = pet.id match {
      case Some(num) => num
      case None => throw MissingIdentifier("The updated pet must have a valid id.")
    }
    petstoreDb.updatePet(pet.copy(id = Some(identifier)))
  }

  /**
   * Endpoint for getPetsByStatus
   * The status(es) are passed as one query parameter.
   * @return A Router that contains a RequestReader of the sequence of all Pets with the Status in question.
   */
  get("/pet/findByStatus") { stati: Seq[String] =>
    if (!stati.isEmpty) petstoreDb.getPetsByStatus(stati)
    else throw InvalidInput("You must enter one or more Statuses!")
  }

  /**
   * Endpoint for findPetsByTag
   * The tags are passed as query parameters.
   * @return A Router that contains a RequestReader of the sequence of all Pets with the given Tags.
   */
  get("/pet/findByTags") { tags: Seq[String] =>
    if (!tags.isEmpty) petstoreDb.findPetsByTag(tags)
    else throw InvalidInput("You must enter one or more Tags!")
  }

  /**
   * Endpoint for deletePet
   * The ID of the pet to delete is passed in the path.
   * @return A Router that contains a RequestReader of the deletePet result (true for success, false otherwise).
   */
  delete("/pet/:id") { request: Request =>
    request.params.getLong("id") match {
      case Some(id) => petstoreDb.deletePet(id)
      case None => throw MissingIdentifier("You must enter the id of the pet to be deleted!")
    }
  }

  /**
   * Endpoint for the updatePetViaForm (form data) service method.
   * The pet's ID is passed in the path.
   * @return A Router that contains a RequestReader of the Pet that was updated.
   */
  post("/pet/:id") { request: Request =>
    val id: Long = request.params.getLongOrElse("id", throw MissingIdentifier("Must give an ID"))
    val name: String = request.params.getOrElse("name", throw InvalidInput("Must give a name"))
    val status: String = request.params.getOrElse("status", throw InvalidInput("Must give a status"))
    val realStat: Status = Status.valueOf(status)
    petstoreDb.updatePetViaForm(id, Some(name), Some(realStat))
  }

//  /**
//   * The ID of the pet corresponding to the image is passed in the path, whereas the image
//   * file is passed as form data.
//   * @return A Router that contains a RequestReader of the uploaded image's url.
//   */
//  post("/pet/:id/uploadImage") { request: Request =>
//    fileUpload("file").embedFlatMap { upload =>
//      petstoreDb.addImage(request.params.getLongOrElse("id", throw MissingIdentifier("Must give an ID")), upload.get())
//    }
//  }


//
//  post("/hi") { hiRequest: HiRequest =>
//    "Hello " + hiRequest.name + " with id " + hiRequest.id
//  }
}
