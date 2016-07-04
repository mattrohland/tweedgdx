package com.tweedgdx.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.JsonValue;


public class MetaComponent implements Component {
    public JsonValue data;

    public MetaComponent(JsonValue data) {
        this.data = data;
    }

    public static void addComponentToEntity(JsonValue instructions, Entity entity) {
        if (instructions != null) {
            entity.add(new MetaComponent(instructions));
        }
    }
}
