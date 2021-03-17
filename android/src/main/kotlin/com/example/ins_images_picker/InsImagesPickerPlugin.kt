package com.example.ins_images_picker

import android.app.Activity
import android.util.Log
import androidx.annotation.NonNull
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureConfig
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.instagram.InsGallery
import com.luck.picture.lib.listener.OnResultCallbackListener
import com.luck.pictureselector.GlideEngine
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.embedding.engine.plugins.activity.ActivityAware
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler

/** InsImagesPickerPlugin */
class InsImagesPickerPlugin : FlutterPlugin, MethodCallHandler, ActivityAware {
    /// The MethodChannel that will the communication between Flutter and native Android
    ///
    /// This local reference serves to register the plugin with the Flutter Engine and unregister it
    /// when the Flutter Engine is detached from the Activity
    private lateinit var channel: MethodChannel
    private var activity: Activity? = null
    private var channelResult: MethodChannel.Result? = null
    private var callbackListener: OnResultCallbackListener<LocalMedia>? = null


    override fun onAttachedToEngine(@NonNull flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
        channel = MethodChannel(flutterPluginBinding.binaryMessenger, "ins_images_picker")
        channel.setMethodCallHandler(this)
        callbackListener = object : OnResultCallbackListener<LocalMedia> {
            override fun onResult(result: MutableList<LocalMedia>?) {
                val mutableList = mutableListOf<MutableMap<String, Any>>()

                for (media in result!!) {
                    Log.i("InsImagesPickerPlugin", "是否压缩:" + media.isCompressed)
                    Log.i("InsImagesPickerPlugin", "压缩:" + media.compressPath)
                    Log.i("InsImagesPickerPlugin", "原图:" + media.path)
                    Log.i("InsImagesPickerPlugin", "是否裁剪:" + media.isCut)
                    Log.i("InsImagesPickerPlugin", "裁剪:" + media.cutPath)
                    Log.i("InsImagesPickerPlugin", "是否开启原图:" + media.isOriginal)
                    Log.i("InsImagesPickerPlugin", "原图路径:" + media.originalPath)
                    Log.i("InsImagesPickerPlugin", "Android Q 特有Path:" + media.androidQToPath)
                    Log.i("InsImagesPickerPlugin", "Size: " + media.size)
                    Log.i("Media Type", "Size: " + media.mimeType)

                    mutableList.add(mutableMapOf(
                            Pair("path", media.path),
                            Pair("cutPath", media.cutPath),
                            Pair("qPath", media.androidQToPath),
                            Pair("mimeType", media.mimeType)
                    ))
                }
                channelResult?.success(
                        mutableList
                )
            }

            override fun onCancel() {
                TODO("Not yet implemented")
            }

        }
    }

    override fun onMethodCall(@NonNull call: MethodCall, @NonNull result: MethodChannel.Result) {
        channelResult = result

        if (call.method == "pickerImages") {
            InsGallery.applyInstagramOptions(activity!!.applicationContext, PictureSelector.create(activity)
                    .openGallery(1))
                    .imageEngine(GlideEngine.createGlideEngine()).hideBottomControls(false).rotateEnabled(true).maxVideoSelectNum(0)
                    .isCamera(false).isCameraAroundState(false)
                    .selectionMode(PictureConfig.SINGLE).isEnableCrop(call.argument<Boolean>("showCrop")!!)
                    .forResult(callbackListener)
        } else {
            result.notImplemented()
        }
    }

    override fun onAttachedToActivity(binding: ActivityPluginBinding) {
        this.activity = binding.activity
    }


    override fun onDetachedFromActivityForConfigChanges() {
        print("onDetachedFromActivityForConfigChanges")
    }

    override fun onReattachedToActivityForConfigChanges(binding: ActivityPluginBinding) {
        print("onReattachedToActivityForConfigChanges")
    }

    override fun onDetachedFromActivity() {
        print("onDetachedFromActivity")
    }

    override fun onDetachedFromEngine(@NonNull binding: FlutterPlugin.FlutterPluginBinding) {
        channel.setMethodCallHandler(null)

    }
}
