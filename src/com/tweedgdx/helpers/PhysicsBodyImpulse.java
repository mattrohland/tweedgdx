package com.tweedgdx.helpers;

public class PhysicsBodyImpulse {
    public float impulseX;
    public float impulseY;
    public float pointX;
    public float pointY;
    public boolean wake = true;

    public PhysicsBodyImpulse(float impulseX, float impulseY, float pointX, float pointY) {
        this.impulseX = impulseX;
        this.impulseY = impulseY;
        this.pointX = pointX;
        this.pointY = pointY;
    }
}
