package com.tenniscourts;

import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode
public class Court {
  Long id;
  String name;
}
