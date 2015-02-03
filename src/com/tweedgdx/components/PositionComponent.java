package com.tweedgdx.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.JsonValue;

public class PositionComponent extends Component{
    public float x = 0.0f;
    public float y = 0.0f;

    public PositionComponent(float x, float y){
        this.x = x;
        this.y = y;
    }

    public static void addComponentToEntity(JsonValue instructions, Entity entity){
        if(
            instructions != null
            && instructions.get("x") != null
            && instructions.get("y") != null
        ){
            entity.add(new PositionComponent(instructions.getFloat("x"), instructions.getFloat("y")));
        }
    }
}
