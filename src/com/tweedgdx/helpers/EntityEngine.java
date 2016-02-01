package com.tweedgdx.helpers;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.JsonValue;

public class EntityEngine extends Engine{
    private JsonValue instructions;
    private BaseEntityFactory entityFactory;
    private EntityLoader entityLoader;

    public EntityEngine(JsonValue instructions){
        super();
        this.instructions = instructions;
        this.entityFactory = new BaseEntityFactory();
        this.entityLoader = new EntityLoader(this, this.entityFactory, this.instructions.get("entity"));
    }

    public Entity loadEntity(String entityKey){
        return this.entityLoader.load(entityKey);
    }
}
