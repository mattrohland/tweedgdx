package com.tweedgdx.helpers;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.JsonValue;


public class EntityLoader extends ConfigLoader {
    private Engine entityEngine;
    private EntityFactoryInterface entityFactory;

    public EntityLoader(Engine entityEngine, EntityFactoryInterface entityFactory, String configLocation) {
        this.entityEngine = entityEngine;
        this.entityFactory = entityFactory;
        this.configLocation = configLocation;
        this.load();
    }

    public EntityLoader(Engine entityEngine, EntityFactoryInterface entityFactory, JsonValue config) {
        this.entityEngine = entityEngine;
        this.entityFactory = entityFactory;
        this.config = config;
    }

    public Entity load(String entityKey) {
        Entity entity = entityFactory.create(this.config.get(entityKey));

        this.entityEngine.addEntity(entity);

        return entity;
    }
}
