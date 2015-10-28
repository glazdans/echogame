package com.glazdans.echo;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.glazdans.echo.physics.FireworksDemoScreen;

public class GdxGame extends Game {

	TestScreen testScreen;

	@Override
	public void create () {
		testScreen = new TestScreen(this);
		setScreen(new FireworksDemoScreen());
	}


}
