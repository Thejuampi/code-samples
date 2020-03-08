import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * !!!
 * Notice that this implementation were deliberately wrote
 * to show how NOT to write production code
 * !!!.
 */
public class JsonDecoder {

    private static final Logger LOGGER = Logger.getLogger(JsonDecoder.class.getName());

    private static final DateTimeFormatter TIMESTAMP_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public Map<String, Object> decode(byte[] input) {
        String json = new String(input);
        Map<String, Object> result = new HashMap<>();

        try {
            JsonObject jsonObject = JsonParser.parseString(json).getAsJsonObject();
            try {
                Optional<String> symbol = getSymbol(jsonObject);
                if(symbol.isPresent()) {
                    result.put("symbol", symbol.get());
                }
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "error calculating symbol", e);
            }

            try {
                Optional<LocalDateTime> timestamp = getTimestamp(jsonObject);
                if(timestamp.isPresent()) {
                    result.put("timestamp", timestamp.get());
                }
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "error calculating timestamp", e);
            }

            try {
                Optional<Double> price = getPrice(jsonObject);
                if(price.isPresent()) {
                    result.put("price", price.get());
                }
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "error calculating timestamp", e);
            }

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "error parsing json", e);
        }

        return result;
    }

    public static Optional<LocalDateTime> getTimestamp(JsonObject jsonObject) {
        if(jsonObject != null) {

            JsonElement maybeTimestamp = jsonObject.get("timestamp");

            if(maybeTimestamp != null && maybeTimestamp.isJsonPrimitive()) {

                JsonPrimitive primitive = maybeTimestamp.getAsJsonPrimitive();

                if(primitive != null && primitive.isString()) {

                    String timestamp = primitive.getAsString();

                    LocalDateTime datetime = LocalDateTime.parse(timestamp, TIMESTAMP_FORMATTER);

                    return Optional.ofNullable(datetime);
                }

            }

        }

        return Optional.empty();
    }

    public static Optional<Double> getPrice(JsonObject jsonObject) {

        if (jsonObject != null) {

            JsonElement maybePrice = jsonObject.get("price");

            if(maybePrice != null && maybePrice.isJsonPrimitive()) {

                JsonPrimitive primitive = maybePrice.getAsJsonPrimitive();

                if(primitive != null && primitive.isNumber()) {

                    return Optional.of(primitive.getAsDouble());

                }

            }
        }
        return Optional.empty();
    }

    public static Optional<String> getSymbol(JsonObject jsonObject) {
        
        if(jsonObject != null ) {

            JsonElement maybeSymbol = jsonObject.get("symbol");

            if(maybeSymbol != null && maybeSymbol.isJsonPrimitive()) {

                JsonPrimitive primitive = maybeSymbol.getAsJsonPrimitive();

                if(primitive != null && primitive.isString()) {

                    return Optional.ofNullable(primitive.getAsString());

                }

            }

        }
        
        return Optional.empty();
    }

}
