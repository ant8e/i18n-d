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

import java.util.Locale
import net.antoinecomte.i18n.resolver.MessageResolver

/**
 * [[I18n]] specification
 *
 */
class I18nSpec extends BaseSpec {

  implicit val dummyResolver = new MessageResolver {
    override def message(l: Locale, messageKey: String, args: Any*): Some[String] = Some(s"$l#$messageKey#${args.toList.mkString(",")}")
  }
  val defaultLocale = Locale.getDefault

  describe("A Resource bundle resolver") {

    it("should retrieve a  message with the default Locale") {
      val message: Option[String] = I18n.mymessage()
      message should be(Some(s"$defaultLocale#mymessage#"))
    }

    it("should retrieve a message with multiple arguments") {
      object dummy {
        override def toString: String = "dummy"
      }
      val message: Option[String] = I18n.mymessage("a", "B", dummy)
      message should be(Some(s"$defaultLocale#mymessage#a,B,dummy"))
    }

    it("should retrieve a  message with an arbitraty key length") {
      val message: Option[String] = I18n.mymessage.a.b.c.d.e.f()
      message should be(Some(s"$defaultLocale#mymessage.a.b.c.d.e.f#"))
    }

    it("should retrieve a  message with an implicit  Locale") {
      implicit val l = Locale.ENGLISH
      val message: Option[String] = I18n.mymessage()
      message should be(Some(s"$l#mymessage#"))
    }

    it("should retrieve a message with an explicit Locale") {
      val l = Locale.ENGLISH
      val message: Option[String] = I18n(l).mymessage()
      message should be(Some(s"$l#mymessage#"))
    }

    it("should retrieve a message with an explicit resolver") {
      val m = "EX"
      val r = new MessageResolver {
        override def message(l: Locale, messageKey: String, args: Any*): Option[String] = Some(m)
      }
      val message: Option[String] = I18n()(r).mymessage()
      message should be(Some(m))
    }
  }

}
