package org.quality.talks.decoder;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import org.quality.talks.model.Pricing;
import org.quality.talks.model.Pricing.PricingBuilder;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.logging.Logger;


/**
 * !!!
 * Notice that this implementation was deliberately wrote
 * to show how NOT to write production code
 * !!!.
 */
public class JsonDecoder {

    private static final Logger LOGGER = Logger.getLogger(JsonDecoder.class.getName());

    private static final DateTimeFormatter TIMESTAMP_FORMATTER = DateTimeFormatter.ISO_DATE_TIME;

    public Pricing decode(byte[] input) {
        try {
            return doDecodeV1(input);
        } catch (Exception e) {
            throw new PricingDecodingException(e);
        }
    }

    private Pricing doDecodeV1(byte[] input) {
        String json = new String(input);
        JsonObject jsonObject = JsonParser.parseString(json).getAsJsonObject();

        PricingBuilder builder = Pricing.builder();
        getSymbol(jsonObject).ifPresent(builder::symbol);
        getPrice(jsonObject).ifPresent(builder::price);
        getTimestamp(jsonObject).ifPresent(builder::timestamp);

        return builder.build();
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
