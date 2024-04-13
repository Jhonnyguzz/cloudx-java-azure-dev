package com.jhonny.petstoreapp.functions.settings;

public class LocalProperties {

  public static final String STORAGE_CONNECTION = System.getenv("AZURE_STORAGE_CONNECTION_STRING");
  public static final String CONTAINER_NAME = System.getenv("AZURE_STORAGE_CONTAINER_NAME");

}
