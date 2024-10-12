package org.senyoudev.datasource;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;

public record FileDataSource(Path filePath) implements DataSource {

  @Override
  public String getType() {
    return "FILE";
  }

  @Override
  public void clear() {
    try (FileWriter fileWriter = new FileWriter(filePath.toFile())) {
      fileWriter.write("");
    } catch (IOException e) {
      throw new RuntimeException("Error clearing file", e);
    }
  }
}
