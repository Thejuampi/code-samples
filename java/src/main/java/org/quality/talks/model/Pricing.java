package org.quality.talks.model;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

@Getter
@Builder
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
@Accessors(fluent = true)
public final class Pricing {
  @NonNull private final String symbol;
  @NonNull private final Double price;
  @NonNull private final LocalDateTime timestamp;
}
