package net.antoinecomte.i18n.resolver

import java.util.Locale
import net.antoinecomte.i18n.BaseSpec

/**
 * [[ResourceBundleResolver]] specification
 *
 */
class ResourceBundleResolverSpec extends BaseSpec {

  val baseName = "i18n/messages"
  val r = new ResourceBundleResolver(baseName)

  describe("A Resource bundle resolver") {

    it("should retrieve a message for a know key") {
      r.message(Locale.ENGLISH, "test.message").value should be("Hello")
      r.message(Locale.FRENCH, "test.message").value should be("Bonjour")
    }

    it("should not retrieve a message for a unknow key") {
      r.message(Locale.ENGLISH, "test.unknow") should be(None)
    }

    it("should format messages") {

      r.formatMessage(Locale.ENGLISH, "{0}", "") should be("")
      r.formatMessage(Locale.ENGLISH, "{0}", "a") should be("a")
      r.formatMessage(Locale.ENGLISH, "{0}{1}", "a", "b") should be("ab")
    }
  }

}
