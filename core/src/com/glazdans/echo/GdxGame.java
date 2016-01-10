package com.glazdans.echo;

import com.badlogic.gdx.Game;
import com.glazdans.echo.bullet.BulletTestScreen;
import com.glazdans.echo.physics.blob.BlobScreen;

public class GdxGame extends Game {

    @Override
    public void create() {
        setScreen(new BulletTestScreen(this));
    }

/**/
}
