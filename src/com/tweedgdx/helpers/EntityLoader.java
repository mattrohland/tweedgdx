package com.tweedgdx.helpers;

import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.JsonValue;

import java.util.ArrayList;


public class EntityLoader extends ConfigLoader{
    private Engine entityEngine;
    private EntityFactory entityFactory;

    public EntityLoader(Engine entityEngine, EntityFactory entityFactory, String configLocation){
        this.entityEngine = entityEngine;
        this.entityFactory = entityFactory;
        this.configLocation = configLocation;
        this.load();
    }

    public void loadAndProcess(){
        this.load();
        if(this.config != null){
            this.process();
        }
    }

    private void process(){
        if(this.config.get("entities").isArray()) {
            for (JsonValue entityInstructions : this.config.get("entities")) {
                this.entityEngine.addEntity(entityFactory.create(entityInstructions));
            }
        }
    }
}
