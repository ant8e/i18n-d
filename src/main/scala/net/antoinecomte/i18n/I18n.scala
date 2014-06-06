/*
 * Copyright 2014 Antoine Comte
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.antoinecomte.i18n

/**
 * Provide support for easy retrieval of localized messages
 *
 */

import java.util.{ Date, Locale }
import net.antoinecomte.i18n.resolver.{ ResourceBundleResolver, MessageResolver }
import scala.language.dynamics

/**
 * This object let your retrieve localized message in an easy way provided you have put a [[MessageResolver]] in your
 * implicit scope
 *
 *
 * First you should add a resolver to the implicit scope
 * For example a ResourceBundle based resolver
 * {{{
 * implicit val resolver = new ResourceBundleResolver("i18n/sample")
 * }}}
 *
 * You can then start to retrieve messages using your message key like it is a Scala attribue/method call.
 * you will get in return a Option with a value if the key was found and None otherwise.
 * Message are localized with the system default Locale if none is specified.
 *
 * {{{
 * val text: Option[String] = I18n.sample.dialog.caption.text()
 * }}}
 *
 * You can pass arguments as well,
 * the actual formatting is delegated to the MessageResolver
 *
 * {{{
 * val date: Date = ???
 * val formattedText = I18n.sample.dialog.description.text("a", "b", date)
 * }}}
 *
 *
 *
 * If you put a Locale into your implicit scope it will override the default Locale
 * {{{
 * import java.util.Locale.ENGLISH
 * implicit val english = ENGLISH
 * val textInEnglish = I18n.sample.dialog.caption.text()
 *
 * }}}
 *
 * You can also select the Locale explicitly
 * {{{
 * val textInEnglish = I18n(ENGLISH).sample.dialog.caption.text()
 * }}}
 *
 *
 * You can override the resolver as well
 * {{{
 * val customResolver: MessageResolver = ???
 * val textWithCustomResolver = I18n()(customResolver).sample.dialog.caption.text()
 * }}}
 *
 *
 *
 */
object I18n extends Dynamic {
  private val default: Locale = Locale.getDefault

  /**
   *
   */
  def apply()(implicit resolver: MessageResolver) = new I18n(default, Nil, resolver)

  def apply(l: Locale)(implicit resolver: MessageResolver) = new I18n(l, Nil, resolver)

  def selectDynamic(name: String)(implicit il: Locale = default, resolver: MessageResolver): I18n = apply(il).selectDynamic(name)

  def applyDynamic(name: String)(args: Any*)(implicit il: Locale = default, resolver: MessageResolver): Option[String] = apply(il).applyDynamic(name)(args: _*)
}

/**
 *
 */
private[i18n] class I18n(private val l: Locale, private val path: List[String], resolver: MessageResolver) extends Dynamic {

  def selectDynamic(name: String): I18n = new I18n(l, name :: path, resolver)

  def applyDynamic(name: String)(args: Any*): Option[String] = new I18n(l, name :: path, resolver).getMessage(args: _*)

  def getMessage(args: Any*): Option[String] = resolver.message(l, path.reverse.mkString("."), args: _*)

}

object sample {

  // First you should add a resolver to the implicit scope 
  // For example a ResourceBundle based resolver
  implicit val resolver = new ResourceBundleResolver("i18n/sample")

  // you can then start to retrieve messages with the default Locale
  // you will get in return a Option with a value if the key was found and None otherwise
  val text: Option[String] = I18n.sample.dialog.caption.text()

  //You can pass arguments as well, 
  // the actual formatting is delegated to the MessageResolver
  val date: Date = ???
  val formattedText = I18n.sample.dialog.description.text("a", "b", date)

  import java.util.Locale.ENGLISH

  // if you bring an Locale into your implicit scope it will override the default Locale
  {
    implicit val english = ENGLISH
    val textInEnglish = I18n.sample.dialog.caption.text()
  }

  // You can also select the Locale explicitly
  val textInEnglish = I18n(ENGLISH).sample.dialog.caption.text()

  //You can override the resolver as well
  val customResolver: MessageResolver = ???
  val textWithCustomResolver = I18n()(customResolver).sample.dialog.caption.text()

}