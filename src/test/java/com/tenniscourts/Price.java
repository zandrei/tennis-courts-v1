package com.tenniscourts;

import java.math.BigDecimal;
import java.util.Objects;

public class Price {
    private final BigDecimal cents;

    private Price(BigDecimal cents) {
        this.cents = cents;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Price price = (Price) o;
        return cents.equals(price.cents);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cents);
    }

    public static Price cents(BigDecimal valueInCents) {
        return new Price(valueInCents);
    }

    public Price minusCents(BigDecimal subtractAmount) {
        return Price.cents(cents.subtract(subtractAmount));
    }

    public Price plusCents(BigDecimal addAmount) {
        return Price.cents(cents.add(addAmount));
    }
}
