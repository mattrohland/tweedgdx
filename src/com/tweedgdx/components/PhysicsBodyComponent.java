package com.tweedgdx.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.JsonValue;


public class PhysicsBodyComponent extends Component{
    public String type; // Options include: DYNAMIC, KINEMATIC, STATIC ...
    public String shape; // Options include: QUADRILATERAL, CIRCLE ...
    public float width;
    public float height;
    public float density;
    public float friction;
    public float restitution;
    public Body body;

    public PhysicsBodyComponent(String type, String shape, float width, float height, float density, float friction, float restitution){
        this.type = type;
        this.shape = shape;
        this.width = width;
        this.height = height;
        this.density = density;
        this.friction = friction;
        this.restitution = restitution;
    }

    public static void addComponentToEntity(JsonValue instructions, Entity entity){
        if(
            instructions != null
            && instructions.get("type") != null
            && instructions.get("shape") != null
            && instructions.get("density") != null
            && instructions.get("friction") != null
            && instructions.get("restitution") != null
        ){
            entity.add(
                new PhysicsBodyComponent(
                    instructions.getString("type"),
                    instructions.getString("shape"),
                    instructions.getFloat("width"),
                    instructions.getFloat("height"),
                    instructions.getFloat("density"),
                    instructions.getFloat("friction"),
                    instructions.getFloat("restitution")
                )
            );
        }
    }
}
