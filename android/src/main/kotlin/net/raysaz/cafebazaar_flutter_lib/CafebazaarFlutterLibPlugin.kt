package net.raysaz.cafebazaar_flutter_lib

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.annotation.NonNull
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.embedding.engine.plugins.activity.ActivityAware
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding
import io.flutter.plugin.common.EventChannel
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result
import io.flutter.plugin.common.PluginRegistry.ActivityResultListener

/** CafebazaarFlutterLibPlugin */
class CafebazaarFlutterLibPlugin: FlutterPlugin, MethodCallHandler, ActivityAware, ActivityResultListener {
  companion object {
    private const val MARKET_REQUEST_CODE = 36345
    private const val NAMESPACE = "cafebazaar_flutter_lib"
  }


  private lateinit var channel : MethodChannel
  private var activity : Activity? = null
  private var pendingResult : Result? = null


  override fun onAttachedToEngine(@NonNull flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
    channel = MethodChannel(flutterPluginBinding.binaryMessenger, "$NAMESPACE/methods")
    channel.setMethodCallHandler(this)

  }

  override fun onAttachedToActivity(binding: ActivityPluginBinding) {
    activity = binding.activity
    binding.addActivityResultListener(this)
  }

  override fun onDetachedFromActivity() {
    activity = null
  }

  override fun onReattachedToActivityForConfigChanges(binding: ActivityPluginBinding) {
    activity = binding.activity
  }

  override fun onDetachedFromActivityForConfigChanges() {

  }

  override fun onMethodCall(@NonNull call: MethodCall, @NonNull result: Result) {
    when (call.method) {
      "openLogin" -> openLogin(result)
      "openDetail" -> openDetail(call, result)
      "openCommentForm" -> openCommentForm(call, result)
      "openDeveloperPage" -> openDeveloperPage(call, result)
      "isLoggedIn" -> isLoggedIn(result)
      "getLatestVersion" -> getLatestVersion(result)
      else -> result.notImplemented()
    }
  }

  private fun openDetail(@NonNull call: MethodCall, @NonNull result: Result) {
    if(activity == null) {
      result.error("openDetail", "activity == null", null)
      return
    }
    try {
      val intent = Intent(Intent.ACTION_VIEW)
      var packageName = call.argument<String>("packageName")
      if(packageName == null) {
        packageName = activity!!.packageName
      }

      intent.data = Uri.parse("bazaar://details?id=$packageName")
      intent.setPackage("com.farsitel.bazaar")
      activity!!.startActivityForResult(intent, MARKET_REQUEST_CODE)
      pendingResult = result
    } catch (e: Exception) {
      result.error("openDetail", "CafeBazaar not installed!", e.message)
    }
  }

  private fun openCommentForm(@NonNull call: MethodCall, @NonNull result: Result) {
    if(activity == null) {
      result.error("openComments", "activity == null", null)
      return
    }
    try {
      val intent = Intent(Intent.ACTION_EDIT)
      var packageName = call.argument<String>("packageName")
      if(packageName == null) {
        packageName = activity!!.packageName
      }
      intent.data = Uri.parse("bazaar://details?id=$packageName")
      intent.setPackage("com.farsitel.bazaar")
      activity!!.startActivityForResult(intent, MARKET_REQUEST_CODE)
      pendingResult = result
    } catch (e: Exception) {
      result.error("error", "CafeBazaar not installed!", e.message)
    }
  }

  private fun openDeveloperPage(@NonNull call: MethodCall, @NonNull result: Result) {
    if(activity == null) {
      result.error("openDeveloperPage", "activity == null", null)
      return
    }
    val developerId = call.argument<String>("developerId")
    if(developerId == null) {
      result.error("openDeveloperPage", "developerId is required", null)
      return
    }
    try {
      val intent = Intent(Intent.ACTION_VIEW)
      intent.data = Uri.parse("bazaar://collection?slug=by_author&aid=$developerId")
      intent.setPackage("com.farsitel.bazaar")
      activity!!.startActivityForResult(intent, MARKET_REQUEST_CODE)
      pendingResult = result
    } catch (e: Exception) {
      result.error("openDeveloperPage", "CafeBazaar not installed!", e.message)
    }
  }

  private fun openLogin(@NonNull result: Result) {
    if(activity == null) {
      result.error("openLogin", "activity == null", null)
      return
    }
    try {
      val intent = Intent(Intent.ACTION_VIEW)
      intent.data = Uri.parse("bazaar://login")
      intent.setPackage("com.farsitel.bazaar")
      activity!!.startActivityForResult(intent, MARKET_REQUEST_CODE)
      pendingResult = result
    } catch (e: Exception) {
      result.error("openLogin", "CafeBazaar not installed!", e.message)
    }
  }

  private fun isLoggedIn(@NonNull result: Result) {
    if(activity == null) {
      result.error("isLoggedIn", "activity == null", null)
      return
    }
    val bgResult = BgResult(result)
    val service = LoginServiceConnection(bgResult, activity!!)
    val intent = Intent("com.farsitel.bazaar.service.LoginCheckService.BIND")
    intent.setPackage("com.farsitel.bazaar")
    activity!!.bindService(intent, service, Context.BIND_AUTO_CREATE)
  }

  private fun getLatestVersion(@NonNull result: Result) {
    if(activity == null) {
      result.error("getLatestVersion", "activity == null", null)
      return
    }
    val bgResult = BgResult(result)
    val service = UpdateServiceConnection(bgResult, activity!!)
    val intent = Intent("com.farsitel.bazaar.service.UpdateCheckService.BIND")
    intent.setPackage("com.farsitel.bazaar")
    activity!!.bindService(intent, service, Context.BIND_AUTO_CREATE)
  }




  override fun onDetachedFromEngine(@NonNull binding: FlutterPlugin.FlutterPluginBinding) {
    channel.setMethodCallHandler(null)
  }

  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?): Boolean {

    return false
  }
}
