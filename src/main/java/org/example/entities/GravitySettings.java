package org.example.entities;

import org.example.Game;

import javax.print.attribute.standard.PresentationDirection;

import static org.example.Game.SCALE;

public class GravitySettings {
    private float airSpeed;
    private float gravityForce;
    private float jumpSpeed;
    private float postCollisionFallSpeed;

    public GravitySettings(float airSpeed, float gravityForce, float jumpSpeed, float postCollisionFallSpeed) {
        this.airSpeed = airSpeed;
        this.gravityForce = gravityForce;
        this.jumpSpeed = jumpSpeed;
        this.postCollisionFallSpeed = postCollisionFallSpeed;
    }

    public float getAirSpeed() {
        return airSpeed;
    }

    public void setAirSpeed(float airSpeed) {
        this.airSpeed = airSpeed;
    }

    public float getGravityForce() {
        return gravityForce;
    }

    public void setGravityForce(float gravityForce) {
        this.gravityForce = gravityForce;
    }

    public float getJumpSpeed() {
        return jumpSpeed;
    }

    public void setJumpSpeed(float jumpSpeed) {
        this.jumpSpeed = jumpSpeed;
    }

    public float getPostCollisionFallSpeed() {
        return postCollisionFallSpeed;
    }

    public void setPostCollisionFallSpeed(float postCollisionFallSpeed) {
        this.postCollisionFallSpeed = postCollisionFallSpeed;
    }
}
