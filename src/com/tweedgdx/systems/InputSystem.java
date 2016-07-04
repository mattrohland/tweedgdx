package com.tweedgdx.systems;

import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.utils.JsonValue;


public class InputSystem extends EntitySystem {
    private JsonValue instructions;

    public InputSystem(JsonValue instructions) {
        this.instructions = instructions;
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
    }
}
