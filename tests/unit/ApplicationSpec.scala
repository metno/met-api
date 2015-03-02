/*
    MET-API

    Copyright (C) 2015 met.no
    Contact information:
    Norwegian Meteorological Institute
    Box 43 Blindern
    0313 OSLO
    NORWAY
    E-mail: met-api@met.no

    This program is free software; you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation; either version 2 of the License, or
    (at your option) any later version.
    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
    GNU General Public License for more details.
    You should have received a copy of the GNU General Public License
    along with this program; if not, write to the Free Software
    Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
    MA 02110-1301, USA
*/
import org.specs2.mutable._
import org.specs2.runner._
import org.junit.runner._

import play.api.test._
import play.api.test.Helpers._

/**
 * Add your spec here.
 * You can mock out a whole application including requests, plugins etc.
 * For more information, consult the wiki.
 */
@RunWith(classOf[JUnitRunner])
class ApplicationSpec extends Specification {

  "Application" should {

    "send 404 on a bad request" in new WithApplication{
      route(FakeRequest(GET, "/boom")) must beNone
    }

    "render the index page" in new WithApplication{
      val home = route(FakeRequest(GET, "/")).get

      status(home) must equalTo(OK)
      contentType(home) must beSome.which(_ == "text/plain")
      contentAsString(home) must contain ("METAPI: Needs Version")
    }

    "render swagger-UI" in new WithApplication{
      val ret = route(FakeRequest(GET, "/v0/swagger-ui")).get

      status(ret) must equalTo(OK)
      contentType(ret) must beSome.which(_ == "text/html")
    }

    "render 'Hello World'" in new WithApplication{
      val home = route(FakeRequest(GET, "/v0/helloWorld")).get

      status(home) must equalTo(OK)
      contentType(home) must beSome.which(_ == "text/plain")
      contentAsString(home) must contain ("Hello World")
    }

    "return 'bad request' if no source is defined in points API" in new WithApplication{
      val ret = route(FakeRequest(GET, "/v0/points")).get

      status(ret) must equalTo(BAD_REQUEST)
    }

    "return 'not found' when no data can be returned points API" in new WithApplication{
      val ret = route(FakeRequest(GET, "/v0/points?sources=abcdef")).get

      status(ret) must equalTo(NOT_FOUND)
      contentType(ret) must beSome.which(_ == "application/json")
    }

    "return valid json when a valid source is defined in points API" in new WithApplication{
      val ret = route(FakeRequest(GET, "/v0/points?sources=KS18700")).get

      status(ret) must equalTo(OK)
      contentType(ret) must beSome.which(_ == "application/json")
    }

    "return valid json when a valid list of sources is defined in points API" in new WithApplication{
      val ret = route(FakeRequest(GET, "/v0/points?sources=KS18700,KS18800")).get

      status(ret) must equalTo(OK)
      contentType(ret) must beSome.which(_ == "application/json")
    }

    "return valid json with many parameters defined in points API" in new WithApplication{
      val ret = route(FakeRequest(GET, "/v0/points?sources=KS18700&places=oslo&reftime=2015-01-01T00:00")).get

      status(ret) must equalTo(OK)
      contentType(ret) must beSome.which(_ == "application/json")
    }

    "return valid json if no source is defined in sourceStations API" in new WithApplication{
      val ret = route(FakeRequest(GET, "/v0/sourceStations")).get

      status(ret) must equalTo(OK)
      contentType(ret) must beSome.which(_ == "application/json")
    }

    "return 'not found' when no data can be returned sourceStations API" in new WithApplication{
      val ret = route(FakeRequest(GET, "/v0/sourceStations?sources=abcdef")).get

      status(ret) must equalTo(NOT_FOUND)
      contentType(ret) must beSome.which(_ == "application/json")
    }

    "return valid json when a valid source is defined in sourceStations API" in new WithApplication{
      val ret = route(FakeRequest(GET, "/v0/sourceStations?sources=KS18700")).get

      status(ret) must equalTo(OK)
      contentType(ret) must beSome.which(_ == "application/json")
    }

    "return valid json when a valid list of sources is defined in sourceStations API" in new WithApplication{
      val ret = route(FakeRequest(GET, "/v0/sourceStations?sources=KS18700,KS18800")).get

      status(ret) must equalTo(OK)
      contentType(ret) must beSome.which(_ == "application/json")
    }

    "return valid json with many parameters defined in sourceStations API" in new WithApplication{
      val ret = route(FakeRequest(GET, "/v0/sourceStations?sources=KS18700&places=oslo&parameters=2015-01-01T00:00")).get

      status(ret) must equalTo(OK)
      contentType(ret) must beSome.which(_ == "application/json")
    }

    "return valid 'json' when fetching a specific sourceStation by ID" in new WithApplication{
      val ret = route(FakeRequest(GET, "/v0/sourceStations/KS18700")).get

      status(ret) must equalTo(OK)
      contentType(ret) must beSome.which(_ == "application/json")

    }

    "return 'not found' if specified sourceStation by ID does not exist" in new WithApplication{
      val ret = route(FakeRequest(GET, "/v0/sourceStations/AB1234")).get

      status(ret) must equalTo(NOT_FOUND)
      contentType(ret) must beSome.which(_ == "application/json")

    }

  }
}
