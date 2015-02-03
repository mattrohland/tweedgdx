package com.tweedgdx.helpers;

import com.badlogic.ashley.core.Entity;


public interface ScriptHandlerInterface{
    boolean isCallable();
    void callInit(Entity entity);
    void call(String method);
}
