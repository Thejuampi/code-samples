package org.quality.talks;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.google.gson.JsonObject;
import java.time.LocalDateTime;
import java.time.Month;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.quality.talks.decoder.JsonDecoder;
import org.quality.talks.external.WeirdExternalDataListener;
import org.quality.talks.model.Pricing;
import org.quality.talks.service.PriceService;
import org.quality.talks.service.PricingProvider;

class PricingServiceTest {

  private JsonDecoder decoder;
  private PricingProvider serviceUnderTest;
  private WeirdExternalDataListener externalDataListener;

  @BeforeEach
  void setUp() {
    this.decoder = new JsonDecoder();
    final PriceService priceService = new PriceService(decoder);
    this.serviceUnderTest = priceService;
    this.externalDataListener = priceService;
  }

  @Test
  void normalPricing() {
    //Given
    var expected = new Pricing("aSymbol", 1.3d, timestamp(2020, Month.MARCH, 9));
    var input = input("aSymbol", 1.3d, "2020-03-09T00:00:00");

    //When
    anInputEventIsReceived(input);

    //Then
    var actual = serviceUnderTest.getPricing("aSymbol");
    assertThat(actual).get().isEqualTo(expected);
  }

  @ParameterizedTest
  @MethodSource("invalidInputs")
  void explodeCombinationsOfFailure(String symbol, Double price, String timestamp) {
    var invalidInput = input(symbol, price, timestamp);
    assertThrows(Exception.class, () -> anInputEventIsReceived(invalidInput));
  }

  private static Object[][] invalidInputs() {
    return new Object[][] {
      {null, 1.3d, "2020-03-09T00:00:00"},
      {"symbol", null, "2020-03-09T00:00:00"},
      {"symbol", 1.3d, null}
    };
  }

  private byte[] input(String symbol, Double price, String timestamp) {
    JsonObject object = new JsonObject();
    object.addProperty("symbol", symbol);
    object.addProperty("price", price);
    object.addProperty("timestamp", timestamp);

    return object.toString().getBytes();
  }

  private LocalDateTime timestamp(int year, Month month, int day) {
    return LocalDateTime.of(year, month, day, 0, 0);
  }

  private void anInputEventIsReceived(byte[] input) {
    externalDataListener.updateData(input);
  }
}
