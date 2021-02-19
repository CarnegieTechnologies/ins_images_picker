import 'dart:async';
import 'dart:io';

import 'package:flutter/services.dart';
import 'package:flutter/material.dart';

class InsImagesPicker {
  static const MethodChannel _channel =
      const MethodChannel('ins_images_picker');

  static const int camera = 0;
  static const int video = 1;
  static const int galleryImage = 2;
  static const int galleryVideo = 3;

  static Future<List<File>> showPicker({
    @required int screenType,
    @required int maxImages,
    @required String appName,
    @required Color navigationBarColor,
    @required Color navigationBarItemColor,
    @required Color backgroundColor,
    @required int statusBarStyleValue,
    @required double compressionQuality,
    bool showCrop = false,
    List<String> ratios = const ['1:1'],
    bool enableCropRotation = false,
    bool showTrimVideo = false,
    String videoQuality = 'AVAssetExportPreset1280x720',
    double maxVideoDurationSeconds = 1800
  }) async {
    try {
      final List<dynamic> images = await _channel.invokeMethod(
          'pickerImages', <String, dynamic>{
        "screenType": screenType,
        "maxImages": maxImages,
        "appName": appName,
        "navigationBarColor": '#${navigationBarColor.value.toRadixString(16)}',
        "navigationBarItemColor": '#${navigationBarItemColor.value.toRadixString(16)}',
        "backgroundColor": '#${backgroundColor.value.toRadixString(16)}',
        "statusBarStyleValue": statusBarStyleValue,
        "compressionQuality": compressionQuality,
        "showCrop": showCrop,
        "ratios": ratios,
        "enableCropRotation": enableCropRotation,
        "showTrim": showTrimVideo,
        "videoQuality": videoQuality,
        "maxVideoDurationSeconds": maxVideoDurationSeconds
      });
      return images.map((f) {
        return File(f["path"]);
      }).toList();
    } catch (e) {
      print(e);
      return List<File>();
    }
  }
}