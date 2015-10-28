package com.glazdans.echo.physics;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.glazdans.echo.Entity;
import com.glazdans.echo.Physics;

public class PhysicsEngine {
    public Array<Entity> collisionList;

    public PhysicsEngine(){
        collisionList = new Array<>();
    }

    private static Vector2 tmp = new Vector2();
    public void update(float delta, Entity player){
        for(Entity e : collisionList){
            tmp.set(e.velocity);
            e.position.add(tmp.scl(delta));
        }
        tmp.set(player.velocity);
        player.position.add(tmp.scl(delta));

        for(Entity e : collisionList){
            if(Physics.areColliding(player,e)) {

                Manifold m = new Manifold();
                m.a = player;
                m.b = e;
                // Two times to check x and y
                AABBvsAABB(m);
            }
        }

    }

    public void ResolveCollision( Entity A, Entity B, Manifold m)
    {
        Gdx.app.log("Physics update","Resolving collision");
        // Calculate relative velocity

        tmp.set(B.velocity);
        Vector2 rv = tmp.sub(A.velocity);

        // Calculate relative velocity in terms of the normal direction
        float velAlongNormal = rv.dot(m.normal);
        Gdx.app.log("Physics update: Normal:",m.normal.toString());

         // Do not resolve if velocities are separating
         if(velAlongNormal > 0) {
             Gdx.app.log("Physics update: Normal:", "returning");
             return;
         }
        // Calculate restitution
        float e = Math.min(A.restitution, B.restitution);

        // Calculate impulse scalar
        float j = -(1 + e) * velAlongNormal;
        j /= 1 / A.mass + 1 / B.mass;

        // Apply impulse
        Vector2 impulse = m.normal.scl(j);

        A.velocity.add(impulse.scl(1 / A.mass*-1));
        B.velocity.add(impulse.scl(1 / B.mass));
    }

    public boolean AABBvsAABB( Manifold m)
    {
        // Setup a couple pointers to each object
        Entity a = m.a;
        Entity b = m.b;

        // Vector from A to B
        tmp.set(b.position);
        Vector2 n = tmp.sub(a.position);

        // Calculate half extents along x axis for each object
        float a_extent = (a.dimensions.x) / 2;
        float b_extent = (b.dimensions.x ) / 2;

        // Calculate overlap on x axis
        float x_overlap = a_extent + b_extent - Math.abs( n.x);

        // SAT test on x axis
        if(x_overlap > 0)
        {
            // Calculate half extents along x axis for each object
            a_extent = (a.dimensions.y) / 2;
            b_extent = (b.dimensions.y) / 2;

            // Calculate overlap on y axis
            float y_overlap = a_extent + b_extent - Math.abs(n.y);

            // SAT test on y axis
            if(y_overlap > 0)
            {
                // Find out which axis is axis of least penetration
                if(x_overlap > y_overlap)
                {
                    // Point towards B knowing that n points from A to B
                    if(n.x < 0)
                        m.normal.set(-1f, 0f );
                    else
                        m.normal.set(1f, 0f);
                    m.penetration = x_overlap;
                    ResolveCollision(m.a,m.b,m);
                    return true;
                }
                else
                {
                    // Point toward B knowing that n points from A to B
                    if(n.y < 0)
                        m.normal.set(0f, -1f );
                    else
                        m.normal.set(0f, 1f);
                    m.penetration = y_overlap;
                    ResolveCollision(m.a,m.b,m);
                    return true;
                }
            }
        }
        return false;
    }

}
