package org.example.entities;

import org.example.Game;

public class GravitySettings {
    private float airSpeed;
    private float gravityForce; // todo should be a constant
    private float jumpSpeed; // todo should be a constant
    private float postCollisionFallSpeed; // todo should be a constant

    public GravitySettings(float airSpeed, float gravityForce, float jumpSpeed, float postCollisionFallSpeed) {
        this.airSpeed = airSpeed * Game.SCALE;
        this.gravityForce = gravityForce * Game.SCALE;
        this.jumpSpeed = jumpSpeed * Game.SCALE;
        this.postCollisionFallSpeed = postCollisionFallSpeed * Game.SCALE;
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
