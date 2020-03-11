package org.quality.talks.service;

import static java.util.Optional.ofNullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.quality.talks.decoder.JsonDecoder;
import org.quality.talks.external.WeirdExternalDataListener;
import org.quality.talks.model.Pricing;

@RequiredArgsConstructor
public final class PriceService implements WeirdExternalDataListener,
                                           PricingProvider {

  private final Map<String, Pricing> cache = new HashMap<>();
  private final JsonDecoder decoder;

  @Override
  public void updateData(byte[] data) {
    final Pricing pricing = decoder.decode(data);
    cache.put(pricing.getSymbol(), pricing);
  }

  @Override
  public Optional<Pricing> getPricing(String symbol) {
    return ofNullable(cache.get(symbol));
  }
}
