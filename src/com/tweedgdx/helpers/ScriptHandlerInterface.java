package com.tweedgdx.helpers;

import com.badlogic.ashley.core.Entity;

import org.luaj.vm2.LuaValue;


public interface ScriptHandlerInterface{
    boolean isCallable();
    void callInit(Entity entity);
    void call(String method);
    void call(String method, LuaValue[] additionalContext);
}
