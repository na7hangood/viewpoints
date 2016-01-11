package controllers

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

}
