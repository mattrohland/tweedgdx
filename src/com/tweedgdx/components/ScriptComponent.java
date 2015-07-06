package com.tweedgdx.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.JsonValue;
import com.tweedgdx.helpers.ScriptHandlerLua;
import java.util.ArrayList;


public class ScriptComponent extends Component{
    public ArrayList<String> scriptLocations = new ArrayList<String>();
    public ArrayList<ScriptHandlerLua> scripts = new ArrayList<ScriptHandlerLua>();

    public ScriptComponent(String scriptLocation){
        this.scriptLocations.add(scriptLocation);
    }

    ScriptComponent(ArrayList<String> scriptLocations){
        this.scriptLocations = scriptLocations;
    }

    public static void addComponentToEntity(JsonValue instructions, Entity entity){
        if(
            instructions != null
            && instructions.get("location") != null
        ){
            entity.add(new ScriptComponent(instructions.getString("location")));
        }
    }
}
