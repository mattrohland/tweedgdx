package com.tweedgdx.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.JsonValue;


public class PositionComponent implements Component {
    public float x = 0.0f;
    public float y = 0.0f;
    public float z = 0.0f;
    public float yaw = 0.0f;

    public PositionComponent(float x, float y, float z, float yaw){
        this(x, y, z);
        this.yaw = yaw;
    }

    public PositionComponent(float x, float y, float z){
        this(x, y);
        this.z = z;
    }

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
            if(instructions.get("z") != null){
                entity.add(new PositionComponent(instructions.getFloat("x"), instructions.getFloat("y"), instructions.getFloat("z")));
            }else{
                entity.add(new PositionComponent(instructions.getFloat("x"), instructions.getFloat("y")));
            }
        }
    }
}
