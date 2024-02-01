package com.iishanto.contactbuddy;

public class ConfigurationSingleton {
    private ConfigurationSingleton(){};
    private static ConfigurationSingleton instance;

    public static final String googleAuthType="google-id";
    public static final String classicAuthType="classic";
    public static final String baseUrl="http://10.0.2.2";
}
