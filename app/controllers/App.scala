package controllers

import java.util.Date

import model.{Commenter, Viewpoint, Subject}
import org.joda.time.DateTime
import play.api.Logger
import play.api.mvc.{Action, Controller}

object App extends Controller with PanDomainAuthActions {

  def index = AuthAction {
    Ok(views.html.Application.app("Viewpoints"))
  }

//  def index(id: String = "") = AuthAction.async { req =>
//
//    val jsFileName = "build/app.js"
//
//    val jsLocation = sys.env.get("JS_ASSET_HOST") match {
//      case Some(assetHost) => assetHost + jsFileName
//      case None => routes.Assets.versioned(jsFileName).toString
//    }
//
//    Permissions.getPermissionsForUser(req.user.email).map { permissions =>
//
//      val clientConfig = ClientConfig(Config().capiUrl, Config().capiKey, TagType.list.map(_.name), permissions)
//
//      Ok(views.html.Application.app("Tag Manager", jsLocation, Json.toJson(clientConfig).toString()))
//    }
//
//  }

  def hello = AuthAction {
    Logger.info("saying hello")
    Ok(views.html.Application.hello("Hello world"))
  }

  def defaultRenderTemp = AuthAction {

    val hillaryClinton = Commenter(1, "Hillary Clinton", Some("http://static.guim.co.uk/sys-images/Guardian/Pix/pictures/2016/1/12/1452598832111/HillaryClintonR.png"), Some("Former secretary of state"), Some("Democrats"), Some("category?"))

    val donaldTrump = Commenter(2, "Donald Trump", Some("http://static.guim.co.uk/sys-images/Guardian/Pix/pictures/2016/1/12/1452598831853/DonaldTrumpL.png"), Some("Real estate mogul"), Some("Republicans"), Some("category?"))

    val testCandidates = List(hillaryClinton, donaldTrump)

    val testViewpointOne = Viewpoint(
      123,
      1,
      "What is wrong with us, that we cannot stand up to the NRA and the gun lobby, and the gun manufacturers they represent?",
      Some("http://www.theguardian.com"),
      Some(new DateTime(2016, 1, 10, 0, 0))
    )

    val testViewpointTwo = Viewpoint(
      123,
      2,
      "Here’s another important way to fight crime – empower law-abiding gun owners to defend themselves.",
      Some("http://www.theguardian.com"),
      Some(new DateTime(2016, 1, 10, 0, 0))
    )

    val testSubject = Subject(123, "Gun laws", Some("http://www.theguardian.com"), List(testViewpointOne, testViewpointTwo), 1)

    Ok(views.html.Application.defaultRendering(testSubject, testCandidates))
  }

}
