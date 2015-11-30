package com.glazdans.echo;

import com.badlogic.gdx.Game;
import com.glazdans.echo.physics.blob.BlobScreen;

public class GdxGame extends Game {

    TestScreen testScreen;

    @Override
    public void create() {
        testScreen = new TestScreen(this);
        setScreen(new BlobScreen());
    }

/**/
}
