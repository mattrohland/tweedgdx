package com.tweedgdx.helpers;

import com.badlogic.gdx.physics.box2d.PolygonShape;


public class BaseShapesFactory {

    public static PolygonShape createQuadrilateral(float width, float height){
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width, height);
        return shape;
    }

}
