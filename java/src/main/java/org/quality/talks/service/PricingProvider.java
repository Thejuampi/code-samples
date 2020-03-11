package org.quality.talks.service;

import java.util.Optional;
import org.quality.talks.model.Pricing;

public interface PricingProvider {
  Optional<Pricing> getPricing(String symbol);
}
