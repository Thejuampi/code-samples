import com.google.gson.JsonObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Map;
import java.util.Optional;

/**
 * Notice that these tests and the decoderUnderTest implementation were deliberately wrote
 * to show how NOT to write either production or test code.
 * i.e.
 */
class JsonDecoderTest {

    JsonDecoder decoderUnderTest = new JsonDecoder();

    @Test
    void coupledTest() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("symbol", "ABCD123");
        jsonObject.addProperty("timestamp", "2020-03-08 17:26:34");
        jsonObject.addProperty("price", 7.5d);

        Optional<String> maybeSymbol = JsonDecoder.getSymbol(jsonObject);

        if(maybeSymbol.isPresent()) {

            Assertions.assertEquals("ABCD123", maybeSymbol.get());

            Optional<LocalDateTime> maybeTimestamp = JsonDecoder.getTimestamp(jsonObject);

            if(maybeTimestamp.isPresent()) {

                Assertions.assertEquals(LocalDateTime.of(2020, Month.MARCH, 8, 17,26,34), maybeTimestamp.get());

                Optional<Double> maybePrice = JsonDecoder.getPrice(jsonObject);

                if(maybePrice.isPresent()) {

                    Assertions.assertEquals(7.5d, maybePrice.get());

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

        String json = "{" +
                "\"symbol\": \"ABCD123\"," +
                "\"price\": 7.5," +
                "\"timestamp\": \"2020-03-08 17:26:34\"" +
                "}";

        Map<String, Object> result = decoderUnderTest.decode(json.getBytes());

        Assertions.assertEquals("ABCD123", result.get("symbol"));
        Assertions.assertEquals(7.5d, result.get("price"));
        Assertions.assertEquals(LocalDateTime.of(2020, Month.MARCH, 8, 17,26,34), result.get("timestamp"));
    }

}
