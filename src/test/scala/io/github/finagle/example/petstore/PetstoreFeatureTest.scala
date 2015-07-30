package io.github.finagle.example.petstore

import com.twitter.finagle.http.Status._
import com.twitter.finatra.http.test.EmbeddedHttpServer
import com.twitter.inject.server.FeatureTest

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

    //getPetsByTag
    "Successfully find pets by tag" in {
      server.httpGet(
        path = "/pet/findByTags?tags=puppy%2C%20white",
        andExpect = Ok
      )
    }

    //deletePet
    "Successfully delete existing pets" in {
      server.httpDelete(
        path = "/pet/0",
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
        params = Map("name" -> "Higgins", "status" -> "pending"),
        andExpect = Ok
      )
    }

    //addImage
  }
}
