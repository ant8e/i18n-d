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

package net.antoinecomte.i18n.resolver

/**
 * Provides classes for Message resolution for [[net.antoinecomte.i18n.I18n]]]
 *
 *
 *
 */

import java.text.MessageFormat
import java.util
import java.util.Locale
import scala.util.Try

/**
 * Class extending this trait are able to provide message resolution
 *
 */
@annotation.implicitNotFound("No message resolver implicit in scope, you need to put a MessageResolver in the scope to localize messages")
trait MessageResolver {
  def message(l: Locale, messageKey: String, args: Any*): Option[String]
}

/**
 * Resolves Message via  java.util.ResourceBundle
 *
 *
 * @param baseName base name for your resource bundle. See javadoc for ResourceBundle.getBundle for details
 */

class ResourceBundleResolver(baseName: String) extends MessageResolver {
  override def message(l: Locale, messageKey: String, args: Any*): Option[String] =
    rawMessage(l, messageKey) map (formatMessage(l, _, args))

  private[i18n] def formatMessage(l: Locale, s: String, args: Any*): String =
    Try {
      new MessageFormat(s, l) format args.toArray
    } getOrElse s

  @inline private def bundle(l: Locale) = util.ResourceBundle.getBundle(baseName, l)

  @inline private def rawMessage(l: Locale, messageKey: String): Option[String] =
    Try {
      bundle(l) getString messageKey
    }.toOption
}

