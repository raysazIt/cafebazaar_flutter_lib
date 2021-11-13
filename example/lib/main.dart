import 'package:flutter/material.dart';
import 'dart:async';

import 'package:flutter/services.dart';
import 'package:cafebazaar_flutter_lib/cafebazaar_flutter_lib.dart';

void main() {
  runApp(MyApp());
}

class MyApp extends StatefulWidget {
  @override
  _MyAppState createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  final _bazaar = CafebazaarFlutterLib.instance;




  bool isLoggedIn = false;
  int appVersionCode = -1;

  @override
  void initState() {
    _bazaar.isLoggedIn().then((value) {
      setState(() {
        isLoggedIn = value;
      });
    });
    _bazaar.getLatestVersion().then((value) {
      setState(() {
        appVersionCode = value;
      });
    });
    super.initState();
  }

  @override
  void dispose() {
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('CafeBazaar Plugin API Example'),
        ),
        body: Directionality(
          textDirection: TextDirection.rtl,
          child: SingleChildScrollView(
            child: Padding(
              padding: const EdgeInsets.all(16),
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.stretch,
                children: [
                  Text("آخرین نسخه برنامه : $appVersionCode"),
                  const SizedBox(
                    height: 12,
                  ),
                  if (!isLoggedIn)
                    FlatButton(
                      color: Colors.green,
                      textColor: Colors.white,
                      height: 50,
                      onPressed: _login,
                      child: Text("ورود به بازار"),
                    ),
                  if (!isLoggedIn)
                    const SizedBox(
                      height: 12,
                    ),
                  FlatButton(
                    color: Colors.purple,
                    textColor: Colors.white,
                    height: 50,
                    onPressed: _bazaar.openDetail,
                    child: Text("مشاهده صفحه برنامه در بازار"),
                  ),
                  const SizedBox(
                    height: 12,
                  ),
                  FlatButton(
                    color: Colors.purple,
                    textColor: Colors.white,
                    height: 50,
                    onPressed: _bazaar.openCommentForm,
                    child: Text("ثبت نظر در بازار"),
                  ),
                  const SizedBox(
                    height: 12,
                  ),
                  FlatButton(
                    color: Colors.purple,
                    textColor: Colors.white,
                    height: 50,
                    onPressed: () => _bazaar.openDeveloperPage("google-llc"),
                    child: Text("مشاهده اپلیکیشن های توسعه دهنده"),
                  ),
                ],
              ),
            ),
          ),
        ),
      ),
    );
  }

  Future _login() async {
    await _bazaar.openLogin();
    isLoggedIn = await _bazaar.isLoggedIn();
    setState(() {});
  }


}
