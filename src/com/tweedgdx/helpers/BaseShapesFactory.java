package com.tweedgdx.helpers;

import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.PolygonShape;


public class BaseShapesFactory {

    public static PolygonShape createQuadrilateral(float width, float height){
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width/2, height/2);
        return shape;
    }

    public static PolygonShape createPolygon(float[] vertices){
        PolygonShape shape = new PolygonShape();
        shape.set(vertices);
        return shape;
    }

    public static CircleShape createCircle(float width){
        CircleShape shape = new CircleShape();
        shape.setRadius(width/2);
        return shape;
    }

}
