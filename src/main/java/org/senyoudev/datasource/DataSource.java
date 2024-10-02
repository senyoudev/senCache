package org.senyoudev.datasource;

public sealed interface DataSource permits FileDataSource {
    String getType();
}
