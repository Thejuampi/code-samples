package org.quality.talks;

import static java.time.Month.JANUARY;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.google.gson.JsonObject;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.quality.talks.decoder.JsonDecoder;
import org.quality.talks.model.Pricing;

/**
 * Notice that these tests and the decoderUnderTest implementation were deliberately wrote to show
 * how NOT to write either production or test code. i.e.
 */
class JsonDecoderTest {

  JsonDecoder decoderUnderTest = new JsonDecoder();

  @Test
  void coupledTest() {
    JsonObject jsonObject = new JsonObject();
    jsonObject.addProperty("symbol", "ABCD123");
    jsonObject.addProperty("timestamp", "2020-03-08T17:26:34");
    jsonObject.addProperty("price", 7.5d);

    Optional<String> maybeSymbol = JsonDecoder.getSymbol(jsonObject);

    if (maybeSymbol.isPresent()) {

      assertEquals("ABCD123", maybeSymbol.get());

      Optional<LocalDateTime> maybeTimestamp = JsonDecoder.getTimestamp(jsonObject);

      if (maybeTimestamp.isPresent()) {

        assertEquals(LocalDateTime.of(2020, Month.MARCH, 8, 17, 26, 34), maybeTimestamp.get());

        Optional<Double> maybePrice = JsonDecoder.getPrice(jsonObject);

        if (maybePrice.isPresent()) {

          assertEquals(7.5d, maybePrice.get());

        } else {
          Assertions.fail();
        }

      } else {
        Assertions.fail();
      }
    } else {
      Assertions.fail();
    }
  }

  @Test
  void notSoCoupledButMultipleAssertions() {

    String json =
        "{"
            + "\"symbol\": \"ABCD123\","
            + "\"price\": 7.5,"
            + "\"timestamp\": \"2020-03-08T17:26:34\""
            + "}";

    var result = decoderUnderTest.decode(json.getBytes());

    assertEquals("ABCD123", result.symbol());
    assertEquals(7.5d, result.price());
    assertEquals(LocalDateTime.of(2020, Month.MARCH, 8, 17, 26, 34), result.timestamp());
  }

  @Test
  void decoupleImplementationDetailsFromTestedFeature() {
    // Given some input
    var input = pricingInputWith("aSymbol", 1.0, "2020-01-01T00:00:00");
    var expected = expected("aSymbol", 1.0, timestamp(2020, JANUARY, 1));
    // When decoder gets call
    var actual = decoderUnderTest.decode(input);
    // Then we get what we expect
    assertEquals(expected, actual);
  }

  private LocalDateTime timestamp(int year, Month month, int day) {
    return LocalDateTime.of(year, month, day, 0, 0);
  }

  private Pricing expected(String symbol, Double price, LocalDateTime timestamp) {
    return Pricing.builder()
      .symbol(symbol)
      .price(price)
      .timestamp(timestamp)
      .build();
  }

  private byte[] pricingInputWith(String symbol, Double price, String timestamp) {
    JsonObject jsonObject = new JsonObject();
    jsonObject.addProperty("symbol", symbol);
    jsonObject.addProperty("timestamp", timestamp);
    jsonObject.addProperty("price", price);
    return jsonObject.toString().getBytes();
  }
}
