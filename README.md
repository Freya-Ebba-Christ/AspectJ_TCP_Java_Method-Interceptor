![image](https://github.com/Freya-Ebba-Christ/AspectJ_TCP_Java_Method-Interceptor/assets/57752514/43999c49-f7b7-43e8-b1ee-6f72f2d0e34f)
<br>
# AspectJ_TCP_Java_Method-Interceptor

This runtime annotation aims to aid in gathering runtime information that is otherwise not provided by the system. out or trace log or would require adding some custom logging code.
Using an annotation eliminates the need to change the source code.
<br>
Happy coding!
<br>
<br>
<h1>For using SSL sockets:</h1>
<b>Generate a Keystore:</b>
<br>
keytool -genkeypair -alias server -keyalg RSA -keysize 2048 -storetype PKCS12 -keystore keystore.p12 -validity 365
<br>
<br>
<b>Configure SSL/TLS:</b>
<br>
System.setProperty("javax.net.ssl.keyStore", "path/to/keystore.p12");
<br>
System.setProperty("javax.net.ssl.keyStorePassword", "keystorepassword");
<br>
<br>
<h1>Adding and removing Annotations to/from source files</h1>
<b>java -cp "your-classpath-here" AnnotationAdder MyJavaFile.java MyAnnotation "param1='value1', param2='value2'"</b>
<br>
<b>java AnnotationRemover MyJavaFile.java com.example.MyAnnotation</b>
