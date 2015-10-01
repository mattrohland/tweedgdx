package com.tweedgdx.helpers;

import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.utils.JsonValue;


public class EntityLoader extends ConfigLoader{
    private Engine entityEngine;
    private EntityFactory entityFactory;

    public EntityLoader(Engine entityEngine, EntityFactory entityFactory, String configLocation){
        this.entityEngine = entityEngine;
        this.entityFactory = entityFactory;
        this.configLocation = configLocation;
        this.load();
    }

    public EntityLoader(Engine entityEngine, EntityFactory entityFactory, JsonValue config){
        this.entityEngine = entityEngine;
        this.entityFactory = entityFactory;
        this.config = config;
    }

    public void process(){
        if(this.config.isArray()) {
            for (JsonValue entityInstructions : this.config) {
                this.entityEngine.addEntity(entityFactory.create(entityInstructions));
            }
        }
    }
}
