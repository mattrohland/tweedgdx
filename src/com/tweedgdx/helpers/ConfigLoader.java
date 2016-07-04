package com.tweedgdx.helpers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;


public class ConfigLoader {
    protected String configLocation;
    public JsonValue config;

    public ConfigLoader() {
    }

    public ConfigLoader(String configLocation) {
        this.configLocation = configLocation;
        this.load();
    }

    public ConfigLoader(JsonValue config) {
        this.config = config;
    }

    public void load() {
        if (configLocation != null && !Gdx.files.internal(configLocation).exists()) {
            Gdx.app.error("CONFIG", configLocation + " does NOT exist.");
        } else {
            Json json = new Json();
            this.config = new JsonReader().parse(Gdx.files.local(configLocation));
        }
    }
}
