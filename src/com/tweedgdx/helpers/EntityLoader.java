package com.tweedgdx.helpers;

import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;

import java.util.ArrayList;


public class EntityLoader{
    private Engine entityEngine;
    private EntityFactory entityFactory;

    public EntityLoader(Engine entityEngine, EntityFactory entityFactory){
        this.entityEngine = entityEngine;
        this.entityFactory = entityFactory;
    }

    public void loadAndProcess(String configLocation){
        ArrayList<JsonValue> config = this.load(configLocation);
        this.process(config);
    }

    private ArrayList<JsonValue> load(String configLocation){
        ArrayList<JsonValue> config = new ArrayList<JsonValue>();

        if(!Gdx.files.internal(configLocation).exists()){
            Gdx.app.error("CONFIG", configLocation+" does NOT exist.");
        }else{
            Json json = new Json();
            config = json.fromJson(ArrayList.class, Gdx.files.local(configLocation));
        }

        return config;
    }

    private void process(ArrayList<JsonValue> config){
        for(JsonValue entityInstructions : config){
            this.entityEngine.addEntity(entityFactory.create(entityInstructions));
        }
    }
}
