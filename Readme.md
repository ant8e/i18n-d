i18n-d
======

Welcome to i18n-d, a simple Scala library for easy retrieval of localized messages using Scala Dynamic.



Using i18n-d
------------


First you should add a resolver to the implicit scope,
for example a ResourceBundle based resolver

``` 
implicit val resolver = new ResourceBundleResolver("i18n/sample")
``` 

You can then start to retrieve messages using your message key like it were a Scala attribute or method call.


``` 
val text: Option[String] = I18n.sample.dialog.caption.text()
``` 

The above line will try to retrieve a message under the *sample.dialog.caption.text* key.

It returns an *Option* with your localized message as value if the message was found and *None* otherwise.

You can pass arguments as well,
the actual formatting being delegated to the MessageResolver.

``` 
val date: Date = ...
val formattedText = I18n.sample.dialog.description.text("a", "b", date)
``` 

Message are localized with the system default Locale unless you put a Locale into your implicit scope that will override the system default Locale :

```
import java.util.Locale.ENGLISH

implicit val english = ENGLISH

val textInEnglish = I18n.sample.dialog.caption.text()
```

You can also set the Locale explicitly :

``` 
val textInEnglish = I18n(ENGLISH).sample.dialog.caption.text()
``` 


The resolver can also be specified explicitly on a call by call basis : 

``` 
val customResolver: MessageResolver = ???

val textWithCustomResolver = I18n()(customResolver).sample.dialog.caption.text()
``` 

ResourceBundleResolver
----------------------

*ResourceBundleResolver* is a MessageResolver implementation that use standard Java [RessourceBundle](http://docs.oracle.com/javase/7/docs/api/java/util/ResourceBundle.html) for resolving messages.

For example with your resource bundle properties files setup like this :

```
src/main/resources/i18n/
├── sample.properties
├── sample_fr.properties
└── sample_en.properties
``` 

you will be able to resolve messages in your bundle by defining a *ResourceBundleResolver* like this :
 
```
implicit val resolver = new ResourceBundleResolver("i18n/sample")
``` 

*ResourceBundleResolver* handles formating with Java [MessageFormat](http://docs.oracle.com/javase/7/docs/api/java/text/MessageFormat.html://). See the documentation for details.

Custom MessageResolver
----------------------
To implement your own custom *MessageResolver* extends the trait net.antoinecomte.i18n.resolver.MessageResolver and define 

```
def message(l: Locale, messageKey: String, args: Any*): Option[String]
```

Installing i18n-d
-----------------

Download or clone this project and compile it with [sbt](http://www.scala-sbt.org).

In case of interest, publication to some public repository could be considered. 

License
-------

This code is open source software licensed under the <a href="http://www.apache.org/licenses/LICENSE-2.0.html">Apache 2.0 License</a>.