package io.github.finagle.example.petstore

import com.twitter.finatra.request.{RouteParam, QueryParam}

case class Id( @RouteParam id: Option[Long] )

case class SeekStatus( @QueryParam status: String )

case class SeekTags( @QueryParam tags: String )

case class UsersFromSeq( users: Seq[User])

case class UpdateUserInfo(
    @RouteParam username: String,
    betterUser: Option[User])

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

