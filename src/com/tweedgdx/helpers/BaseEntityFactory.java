package com.tweedgdx.helpers;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.JsonValue;
import com.tweedgdx.components.AliasComponent;
import com.tweedgdx.components.PhysicsBodyComponent;
import com.tweedgdx.components.PositionComponent;
import com.tweedgdx.components.ScriptComponent;


public class BaseEntityFactory implements EntityFactory{
    public Entity create(JsonValue entityInstructions){
        Entity entity = new Entity();

        AliasComponent.addComponentToEntity(entityInstructions.get("AliasComponent"), entity);
        PositionComponent.addComponentToEntity(entityInstructions.get("PositionComponent"), entity);
        PhysicsBodyComponent.addComponentToEntity(entityInstructions.get("PhysicsBodyComponent"), entity);
        ScriptComponent.addComponentToEntity(entityInstructions.get("ScriptComponent"), entity);

        return entity;
    }
}
