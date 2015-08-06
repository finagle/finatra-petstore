package io.github.finagle.example.petstore

import javax.inject.Inject
import com.twitter.finagle.http.Request
import com.twitter.finatra.http.Controller
import com.twitter.finatra.http.fileupload.MultipartItem
import com.twitter.finatra.http.request.RequestUtils

/**
 * A controller that invokes all the pet service methods of the Petstore API. Supported routes include:
 *
 * GET: /pet/findByStatus (getPetsByStatus)
 *    Status(es) are passed as one query parameter.
 *
 * GET: /pet/findByTags (findPetsByTag)
 *    The tags are passed as query parameters.
 *
 * GET: /pet/:id (getPet)
 *    The long passed in the path becomes the ID of the Pet fetched.
 *
 * POST: /pet/:id (updatePetNameStatus)
 *    The pet's ID is passed in the path. Its new name and status are passed as form data.
 *
 * POST: /pet (addPet)
 *    The pet to be added must be passed in the body.
 *
 * POST: /pet/:id/uploadImage (addImage)
 *    The ID of the pet corresponding to the image is passed in the path, whereas the image
 *    file is passed as form data.
 *
 * PUT: /pet (updatePet)
 *    The updated, better version of the current pet must be passed in the body.
 *
 * DELETE: /pet/:id (deletePet)
 *    The ID of the pet to delete is passed in the path.
 *
 * @param petstoreDb The petstore instance on which to perform the methods.
 */
class PetstorePetController @Inject()(petstoreDb: PetstoreDb) extends Controller {

  get("/pet/findByStatus") { findStati: SeekStatus =>
    if (findStati.status.length > 0) petstoreDb.getPetsByStatus(findStati.status.split(",").map(_.trim))
    else throw InvalidInput("You must enter one or more Statuses!")
  }

  get("/pet/findByTags") { allTags: SeekTags =>
    if (allTags.tags.length > 0) petstoreDb.findPetsByTag(allTags.tags.split(",").map(_.trim))
    else throw InvalidInput("You must enter one or more Tags!")
  }

  get("/pet/:id") { request: Id =>
    petstoreDb.getPet(request.id)
  }

  post("/pet/:id") { request: FormPostRequest =>
    val status: String = request.status
    val realStat: Status = status match {
      case "available" => Status.Available
      case "pending" => Status.Pending
      case "adopted" => Status.Adopted
      case other => throw InvalidInput(s"$other is not a valid status")
    }
    petstoreDb.updatePetNameStatus(request.id, Some(request.name), Some(realStat))
  }

  post("/pet") { pet: Pet =>
    petstoreDb.addPet(pet)
  }

  post("/pet/:id/uploadImage") { request: Request =>
    val id: Long = request.params.getLongOrElse("id", 0)
    val imageItem: MultipartItem = RequestUtils.multiParams(request)
        .getOrElse("file", throw InvalidInput("Must pass in an image!"))
    if ((id == 0 && request.getParam("id").equals("0")) || (id != 0)) petstoreDb.addImage(id, imageItem.data)
    else throw InvalidInput("Must give a valid Long type ID of the pet to upload image to!")
  }

  put("/pet") { pet: Pet =>
    val identifier: Long = pet.id match {
      case Some(num) => num
      case None => throw MissingIdentifier("The updated pet must have a valid id.")
    }
    petstoreDb.updatePet(pet.copy(id = Some(identifier)))
  }

  delete("/pet/:id") { request: Id =>
    petstoreDb.deletePet(request.id)
  }
}
