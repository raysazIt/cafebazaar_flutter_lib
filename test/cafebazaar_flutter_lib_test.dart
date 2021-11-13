import 'package:flutter/services.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:cafebazaar_flutter_lib/cafebazaar_flutter_lib.dart';

void main() {
  const MethodChannel channel = MethodChannel('cafebazaar_flutter_lib');

  TestWidgetsFlutterBinding.ensureInitialized();

  setUp(() {
    channel.setMockMethodCallHandler((MethodCall methodCall) async {
      return '42';
    });
  });

  tearDown(() {
    channel.setMockMethodCallHandler(null);
  });

  test('getPlatformVersion', () async {
    expect(await CafebazaarFlutterLib.platformVersion, '42');
  });
}
