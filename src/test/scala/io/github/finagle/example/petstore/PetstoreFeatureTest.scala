package io.github.finagle.example.petstore

import com.twitter.finagle.http.Status._
import com.twitter.finagle.http.{Method, FileElement, Request, RequestBuilder}
import com.twitter.finatra.http.test.EmbeddedHttpServer
import com.twitter.inject.server.FeatureTest
import com.twitter.io.{Reader, Buf}
import com.twitter.util.Await
import org.apache.commons.io.IOUtils
import org.jboss.netty.buffer.ChannelBuffers
import org.jboss.netty.handler.codec.http.HttpRequest

class PetstoreFeatureTest extends FeatureTest {

  override val server = new EmbeddedHttpServer(new PetstoreServer)

  "PetstoreServer" should {
    //getPet
    "Return valid pets" in { randInt: Int =>
      server.httpGet(
        path = "/pet/" + randInt,
        andExpect = Ok
      )
    }

    "Fail to return invalid pets" in {
      server.httpGet(
        path = "/pet/100",
        andExpect = NotFound
      )
    }

    //addPet
    "Add valid pets" in {
      server.httpPost(
        path = "/pet",
        postBody =
          """
            |{
            |  "name": "Ell",
            |  "photoUrls": [],
            |  "category": {"name": "Wyverary"},
            |  "tags": [{"name": "Wyvern"}, {"name": "Library"}],
            |  "status": "pending"
            |}
          """.stripMargin,
        andExpect = Ok
      )
    }

    "Fail to add invalid pets" in {
      server.httpPost(
        path = "/pet",
        postBody =
          """
            |{
            |  "id": 0,
            |  "name": "Ell",
            |  "photoUrls": [],
            |  "category": {"name": "Wyverary"},
            |  "tags": [{"name": "Wyvern"}, {"name": "Library"}],
            |  "status": "pending"
            |}
          """.stripMargin,
        andExpect = NotFound
      )
    }

//    //updatePet
    "Update valid pets" in {
      server.httpPut(
        path = "/pet",
        headers = Map("content-type" -> "application/json"),
        putBody =
          """
            |{
            |  "id": 0,
            |  "name": "A-Through-L",
            |  "photoUrls": [],
            |  "category": {"name": "Wyverary"},
            |  "tags": [{"name": "Wyvern"}, {"name": "Library"}],
            |  "status": "pending"
            |}
          """.stripMargin,
        andExpect = Ok
      )
    }

    "Fail attempts to update pets without specifying an ID" in {
      server.httpPut(
        path = "/pet",
        headers = Map("content-type" -> "application/json"),
        putBody =
          """
            |{
            |  "name": "Ell",
            |  "photoUrls": [],
            |  "category": {"name": "Wyverary"},
            |  "tags": [{"name": "Wyvern"}, {"name": "Library"}],
            |  "status": "pending"
            |}
          """.stripMargin,
        andExpect = NotFound
      )
    }

    //getPetsByStatus
    "Successfully find pets by status" in {
      server.httpGet(
        path = "/pet/findByStatus?status=available%2C%20pending",
        andExpect = Ok
      )
    }

    "Fail if no valid statuses are given to findByStatus endpoint" in {
      server.httpGet(
        path = "/pet/findByStatus?status=",
        andExpect = NotFound
      )
    }

    //getPetsByTag
    "Successfully find pets by tag" in {
      server.httpGet(
        path = "/pet/findByTags?tags=puppy%2C%20white",
        andExpect = Ok
      )
    }

    "Fail if no tags are given to findbyTags endpoint" in {
      server.httpGet(
        path = "/pet/findByTags?tags=",
        andExpect = NotFound
      )
    }

    //deletePet
    "Successfully delete existing pets" in {
      server.httpDelete(
        path = "/pet/7",
        andExpect = Ok
      )
    }

    "Fail to delete nonexistent pets" in {
      server.httpDelete(
        path = "/pet/100",
        andExpect = NotFound
      )
    }

    //updatePetViaForm
    "Allow the updating of pets via form data" in {
      server.httpFormPost(
        path = "/pet/1",
        params = Map("name" -> "Higgins", "status" -> "available"),
        andExpect = Ok
      )
      server.httpFormPost(
        path = "/pet/1",
        params = Map("name" -> "Higgins", "status" -> "pending"),
        andExpect = Ok
      )
      server.httpFormPost(
        path = "/pet/1",
        params = Map("name" -> "Higgins", "status" -> "adopted"),
        andExpect = Ok
      )
    }

    "Fail appropriately if an invalid status is given when updating a pet via form data" in {
      server.httpFormPost(
        path = "/pet/1",
        params = Map("name" -> "Higgins", "status" -> "hungry"),
        andExpect = NotFound
      )
    }

    //addImage
    "Allow the uploading of images for pets" in {
      val imageDataStream = getClass.getResourceAsStream("/bear.jpg")
      val imageByte = IOUtils.toByteArray(imageDataStream)
      val imageData: Buf = Await.result(Reader.readAll(Reader.fromStream(imageDataStream)))
      val req: HttpRequest = RequestBuilder()
        .url("http://localhost:8888/pet/1/uploadImage")
        .add(FileElement("file", ChannelBuffers.wrappedBuffer(imageByte)))
        .buildFormPost(true)
      server.httpRequest(
        request = Request(req),
        andExpect = Ok,
        suppress = true
      )

      val secImageDataStream = getClass.getResourceAsStream("/jelly.jpg")
      val secImageByte = IOUtils.toByteArray(secImageDataStream)
      val secImageData: Buf = Await.result(Reader.readAll(Reader.fromStream(secImageDataStream)))
      val req2: HttpRequest = RequestBuilder()
        .url("http://localhost:8888/pet/1/uploadImage")
        .add(FileElement("file", ChannelBuffers.wrappedBuffer(secImageByte)))
        .buildFormPost(true)
      server.httpRequest(
        request = Request(req2),
        andExpect = Ok,
        suppress = true
      )
    }

    "Fail when a pet ID is not passed when uploading images" in {
      val imageDataStream = getClass.getResourceAsStream("/bear.jpg")
      val imageByte = IOUtils.toByteArray(imageDataStream)
      val imageData: Buf = Await.result(Reader.readAll(Reader.fromStream(imageDataStream)))
      val req: HttpRequest = RequestBuilder()
          .url("http://localhost:8888/pet/chocolate/uploadImage")
          .add(FileElement("file", ChannelBuffers.wrappedBuffer(imageByte)))
          .buildFormPost(true)
      server.httpRequest(
        request = Request(req),
        andExpect = NotFound,
        suppress = true
      )
    }

    "Fail when attempting to upload a nonexistent image to a pet" in {
      val req: HttpRequest = RequestBuilder()
          .url("http://localhost:8888/pet/2/uploadImage")
          .build(Method.Post, None)
      server.httpRequest(
        request = Request(req),
        andExpect = NotFound,
        suppress = true
      )
    }

    //getInventory
    "Allow users to get the store inventory" in {
      server.httpGet(
        path = "/store/inventory",
        andExpect = Ok
      )
    }

    //addOrder
    "Allow users to add orders to the store" in {
      server.httpPost(
        path = "/store/order",
        postBody =
          """
            |{
            |  "petId": 3,
            |  "quantity": 4,
            |  "shipDate": "2015-7-30",
            |  "status": "placed"
            |}
          """.stripMargin,
        andExpect = Ok
      )
    }

    "Fail to add inappropriately formed orders" in {
      server.httpPost(
        path = "/store/order",
        postBody =
            """
              |{
              |  "id": 0,
              |  "quantity": 4,
              |  "shipDate": "2015-7-30",
              |  "status": "placed"
              |}
            """.stripMargin,
        andExpect = NotFound
      )
    }

    "Fail appropriately when no order is passed in addOrder" in {
      server.httpPost(
        path = "/store/order",
        postBody =
            """
              |{}
            """.stripMargin,
        andExpect = NotFound
      )
    }

    //deleteOrder
    "Allow for the deletion of store orders" in {
      server.httpDelete(
        path = "/store/order/1",
        andExpect = Ok
      )
    }

    "Fail appropriately if a user tries to delete an order without passing an id" in {
      server.httpDelete(
        path = "/store/order",
        andExpect = NotFound
      )
    }

    //getOrder
    "Allow for the lookup of store orders" in {
      server.httpGet(
        path = "/store/order/0",
        andExpect = Ok
      )
    }

    "Fail when looking up nonexistant orders" in {
      server.httpGet(
        path = "/store/order/100",
        andExpect = NotFound
      )
    }

    //addUser
    "Allow for the adding of new users" in {
      server.httpPost(
        path = "/user",
        postBody =
            """
              |{
              |  "username": "ichigoMilk",
              |  "firstName": "gintoki",
              |  "lastName": "sakata",
              |  "email": "yorozuyagc@kabukicho.com",
              |  "password": "dango"
              |}
            """.stripMargin,
        andExpect = Ok
      )
    }

    "Fail when adding users with redundant usernames" in {
      server.httpPost(
        path = "/user",
        postBody =
            """
              |{
              |  "username": "ichigoMilk",
              |  "firstName": "gintoki",
              |  "lastName": "sakata",
              |  "email": "yorozuyagc@kabukicho.com",
              |  "password": "dango"
              |}
            """.stripMargin,
        andExpect = NotFound
      )
    }

    //getUser
    "Allow for the retrieval of existing users" in {
      server.httpGet(
        path = "/user/kagura",
        andExpect = Ok
      )
    }

    "Fail on the retrieval of nonexistent users" in {
      server.httpGet(
        path = "/user/kaneki",
        andExpect = NotFound
      )
    }

    //updateUser
    "Allow for the updating of existing users" in {
      server.httpPut(
        path = "/user/kagura",
        headers = Map("content-type" -> "application/json"),
        putBody =
            """
              |{
              |  "username": "kagura",
              |  "firstName": "gintoki",
              |  "lastName": "sakata",
              |  "email": "yorozuyagc@kabukicho.com",
              |  "password": "dango"
              |}
            """.stripMargin,
        andExpect = Ok
      )
    }

    "Fail appropriately if a username is not given in the path when updating a user" in {
      server.httpPut(
        path = "/user",
        headers = Map("content-type" -> "application/json"),
        putBody =
            """
              |{
              |  "username": "kagura",
              |  "firstName": "gintoki",
              |  "lastName": "sakata",
              |  "email": "yorozuyagc@kabukicho.com",
              |  "password": "dango"
              |}
            """.stripMargin,
        andExpect = NotFound
      )
    }

    "Fail appropriately if information for a user to be updated is given incorrectly" in {
      server.httpPut(
        path = "/user",
        headers = Map("content-type" -> "application/json"),
        putBody =
            """
              |{
              |  "username": "kagura",
              |  "firstName": "gintoki",
              |  "lastName": "sakata",
              |  "email": "yorozuyagc@kabukicho.com"
              |}
            """.stripMargin,
        andExpect = NotFound
      )
    }

    //deleteUser
    "Allow for the deletion of existing users" in {
      server.httpDelete(
        path = "/user/coraline",
        andExpect = Ok
      )
    }

    "Fail when deleting nonexistant users" in {
      server.httpDelete(
        path = "/user/100",
        andExpect = NotFound
      )
    }

    //addUserViaArray
    "Allow the adding of an array of users" in {
      server.httpPost(
        path = "/user/createWithArray",
        postBody =
            """
              |[
              |  {
              |    "username": "shiroyasha",
              |    "firstName": "gintoki",
              |    "lastName": "sakata",
              |    "email": "yorozuyagc@kabukicho.com",
              |    "password": "dango"
              |  }
              |]
            """.stripMargin,
        andExpect = Ok
      )
    }

    "Do nothing when given an empty array of users" in {
      server.httpPost(
        path = "/user/createWithArray",
        postBody =
            """
              |[]
            """.stripMargin,
        andExpect = Ok
      )
    }

    //addUserViaList
    "Allow the adding of a list of users" in {
      server.httpPost(
        path = "/user/createWithList",
        postBody =
            """
              |[
              |  {
              |    "username": "danna",
              |    "firstName": "gintoki",
              |    "lastName": "sakata",
              |    "email": "yorozuyagc@kabukicho.com",
              |    "password": "dango"
              |  }
              |]
            """.stripMargin,
        andExpect = Ok
      )
    }

  }
}
