package com.tweedgdx.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.JsonValue;


public class AliasComponent implements Component {
    public String alias;

    public AliasComponent(String alias){
        this.alias = alias;
    }

    public static void addComponentToEntity(JsonValue instructions, Entity entity){
        if(instructions != null && instructions.get("alias") != null){
            entity.add(new AliasComponent(instructions.getString("alias")));
        }
    }
}
