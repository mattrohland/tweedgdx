package com.tweedgdx.helpers;

import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.utils.JsonValue;


public class EntityLoader extends ConfigLoader{
    private Engine entityEngine;
    private EntityFactoryInterface entityFactory;

    public EntityLoader(Engine entityEngine, EntityFactoryInterface entityFactory, String configLocation){
        this.entityEngine = entityEngine;
        this.entityFactory = entityFactory;
        this.configLocation = configLocation;
        this.load();
    }

    public EntityLoader(Engine entityEngine, EntityFactoryInterface entityFactory, JsonValue config){
        this.entityEngine = entityEngine;
        this.entityFactory = entityFactory;
        this.config = config;
    }

    public void load(String entityKey){
        if(this.config.get(entityKey).isObject()) {
            this.entityEngine.addEntity(entityFactory.create(this.config.get(entityKey)));
        }
    }
}
