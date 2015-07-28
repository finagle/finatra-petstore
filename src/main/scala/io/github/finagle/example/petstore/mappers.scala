package io.github.finagle.example.petstore

import javax.inject.Inject

import com.twitter.finagle.{Service, SimpleFilter}
import com.twitter.finagle.http.{Response, Request}
import com.twitter.finatra.http.exceptions.ExceptionMapper
import com.twitter.finatra.http.response.ResponseBuilder
import com.twitter.util.Future

class InvalidInputMapper @Inject()(response: ResponseBuilder)
    extends ExceptionMapper[InvalidInput] {

  override def toResponse(request: Request, exception: InvalidInput): Response = {
    response.notFound("Invalid input!")
  }
}

class MissingIdentifierMapper @Inject()(response: ResponseBuilder)
    extends ExceptionMapper[MissingIdentifier] {

  override def toResponse(request: Request, exception: MissingIdentifier): Response = {
    response.notFound("ID not given or ID does not exist!")
  }
}

class MissingPetMapper @Inject()(response: ResponseBuilder)
    extends ExceptionMapper[MissingPet] {

  override def toResponse(request: Request, exception: MissingPet): Response = {
    response.notFound("Your pet doesn't exist! :(")
  }
}

class MissingUserMapper @Inject()(response: ResponseBuilder)
    extends ExceptionMapper[MissingPet] {

  override def toResponse(request: Request, exception: MissingPet): Response = {
    response.notFound("This user doesn't exist! :(")
  }
}

class OrderNotFoundMapper @Inject()(response: ResponseBuilder)
    extends ExceptionMapper[MissingPet] {

  override def toResponse(request: Request, exception: MissingPet): Response = {
    response.notFound("Your order doesn't exist! :(")
  }
}

class RedundantUsernameMapper @Inject()(response: ResponseBuilder)
    extends ExceptionMapper[MissingPet] {

  override def toResponse(request: Request, exception: MissingPet): Response = {
    response.notFound("You can't reuse an existing username! :(")
  }
}