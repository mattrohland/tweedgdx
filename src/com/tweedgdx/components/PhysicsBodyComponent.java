package com.tweedgdx.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.JsonValue;


public class PhysicsBodyComponent implements Component {
    public String type; // Options include: DYNAMIC, KINEMATIC, STATIC ...
    public String shape; // Options include: QUADRILATERAL, CIRCLE ...
    public float width;
    public float height;
    public float[] vertices;
    public float density;
    public float friction;
    public float restitution;
    public boolean bullet;
    public Body body;

    public PhysicsBodyComponent(String type, String shape, float width, float height, float[] vertices, float density, float friction, float restitution, boolean bullet) {
        this.type = type;
        this.shape = shape;
        this.width = width;
        this.height = height;
        this.vertices = vertices;
        this.density = density;
        this.friction = friction;
        this.restitution = restitution;
        this.bullet = bullet;
    }

    public static void addComponentToEntity(JsonValue instructions, Entity entity) {
        float[] vertices = new float[0];

        if (
            instructions != null
                && instructions.get("type") != null
                && instructions.get("shape") != null
                && instructions.get("density") != null
            ) {
            if (instructions.get("vertices") != null && instructions.get("vertices").isArray()) {
                vertices = instructions.get("vertices").asFloatArray();
            }

            entity.add(
                new PhysicsBodyComponent(
                    instructions.getString("type"),
                    instructions.getString("shape"),
                    instructions.getFloat("width", instructions.getFloat("height", 0)),
                    instructions.getFloat("height", instructions.getFloat("width")),
                    vertices,
                    instructions.getFloat("density"),
                    instructions.getFloat("friction", 0),
                    instructions.getFloat("restitution", 0),
                    instructions.getBoolean("bullet", false)
                )
            );
        }
    }
}
