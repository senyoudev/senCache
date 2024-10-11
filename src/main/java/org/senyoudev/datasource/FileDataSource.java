package org.senyoudev.datasource;

public record FileDataSource() implements DataSource {
  @Override
  public String getType() {
    return "FILE";
  }
}
