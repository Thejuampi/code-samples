package org.quality.talks.service;

import static java.util.Optional.ofNullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Logger;

import lombok.RequiredArgsConstructor;
import org.quality.talks.decoder.JsonDecoder;
import org.quality.talks.decoder.PricingDecodingException;
import org.quality.talks.external.WeirdExternalDataListener;
import org.quality.talks.model.Pricing;

@RequiredArgsConstructor
public final class PriceService implements WeirdExternalDataListener,
                                           PricingProvider {

  private static final Logger log = Logger.getLogger(PriceService.class.getName());

  private final Map<String, Pricing> cache = new HashMap<>();
  private final JsonDecoder decoder;

  @Override
  public void updateData(byte[] data) {
    try {
      final Pricing pricing = decoder.decode(data);
      cache.put(pricing.symbol(), pricing);
    } catch (PricingDecodingException e) {
      log.severe("Error decoding price");
    }
  }

  @Override
  public Optional<Pricing> getPricing(String symbol) {
    return ofNullable(cache.get(symbol));
  }
}
