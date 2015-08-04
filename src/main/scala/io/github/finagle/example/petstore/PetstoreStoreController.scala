package io.github.finagle.example.petstore

import javax.inject.Inject

import com.twitter.finagle.http.Request
import com.twitter.finatra.http.Controller

/**
 * A controller that invokes all the store service methods of the Petstore API. Supported routes include:
 *
 * GET: /store/inventory (getInventory)
 *
 * GET: /store/order/:id (findOrder)
 *    The ID of the order to be found is passed in the path.
 *
 * POST: /store/order (addOrder)
 *    The order to be added is passed in the body.
 *
 * DELETE: /store/order/:id (deleteOrder)
 *    The ID of the order to be deleted is passed in the path.
 *
 * @param petstoreDb The petstore instance on which to perform the methods.
 */
class PetstoreStoreController @Inject()(petstoreDb: PetstoreDb) extends Controller{

  get("/store/inventory") { request: Request =>
    petstoreDb.getInventory
  }

  get("/store/order/:id") { request: Id =>
    petstoreDb.findOrder(request.id)
  }

  post("/store/order") { order: Order =>
    if (order.petId == None) throw InvalidInput("You must enter an order to add it!")
    else petstoreDb.addOrder(order)
  }

  delete("/store/order/:id") { request: Id =>
    petstoreDb.deleteOrder(request.id)
  }
}
