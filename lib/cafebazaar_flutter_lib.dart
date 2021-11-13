
import 'dart:async';

import 'package:flutter/services.dart';

class CafebazaarFlutterLib {
  static const String _NAMESPACE = 'cafebazaar_flutter_lib';

  static const MethodChannel _methodChannel =
  MethodChannel('${CafebazaarFlutterLib._NAMESPACE}/methods');

  CafebazaarFlutterLib._();

  static final CafebazaarFlutterLib _instance = CafebazaarFlutterLib._();
  static CafebazaarFlutterLib get instance => _instance;

  /// Get latest version of app in cafeBazaar
  Future<dynamic> getLatestVersion() =>
      _methodChannel.invokeMethod('getLatestVersion');

  /// Check user isLoggedIn cafeBazaar
  Future<dynamic> isLoggedIn() => _methodChannel.invokeMethod('isLoggedIn');

  /// Open cafeBazaar login page
  Future openLogin() => _methodChannel.invokeMethod('openLogin');

  /// Open detail page of app in cafeBazaar
  Future openDetail([String packageName=""]) =>
      _methodChannel.invokeMethod('openDetail', {
        'packageName': packageName,
      });

  /// Open comment form of app in cafeBazaar
  Future openCommentForm([String packageName=""]) =>
      _methodChannel.invokeMethod('openCommentForm', {
        'packageName': packageName,
      });

  /// Open developer app list In cafeBazaar
  Future openDeveloperPage(String developerId) =>
      _methodChannel.invokeMethod('openDeveloperPage', {
        'developerId': developerId,
      });

}
