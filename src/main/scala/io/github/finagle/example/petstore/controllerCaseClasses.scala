package io.github.finagle.example.petstore

import com.twitter.finatra.request.{FormParam, RouteParam, QueryParam}

/**
 * Case class for the ID of a User, Pet, or Order
 */
case class Id( @RouteParam id: Long)

/**
 * Case class that takes in a sequence of Strings representing the status of pets.
 * Used for the getPetsByStatus route.
 */
case class SeekStatus( @QueryParam status: String )

/**
 * Case class that takes in a sequence of Strings representing the tags of pets.
 * Used for the findPetsByTag route.
 */
case class SeekTags( @QueryParam tags: String )

/**
 * Case class for the updatePetNameStatus route.
 */
case class FormPostRequest(
    @RouteParam id: Long,
    @FormParam name: String,
    @FormParam status: String
    )

/**
 * Case class for the updateUser route.
 */
case class UpdateUser(
    @RouteParam user: String,
    id: Option[Long],
    username: String,
    firstName: Option[String],
    lastName: Option[String],
    email: Option[String],
    password: String,
    phone: Option[String]
    )

