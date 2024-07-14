package com.xemplarsoft.nnt;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.utils.Align;
import com.xemplarsoft.nnt.entity.Unit;
import com.xemplarsoft.nnt.neuro.NetworkRenderer;
import com.xemplarsoft.nnt.screens.SimScreen;

public class TestGame extends Game {
	public static BitmapFont font, buttonText, networkLabel;
	public static TextureAtlas textures;

	public void create () {
		initAssets();
		setScreen(new SimScreen());
	}

	private void initAssets(){
		font = new BitmapFont();
		font.setUseIntegerPositions(false);
		font.getData().setScale(0.03F);
		font.setColor(Color.BLACK);

		networkLabel = new BitmapFont();
		networkLabel.setUseIntegerPositions(false);
		networkLabel.getData().setScale(0.02F);
		networkLabel.setColor(Color.BLACK);

		buttonText = new BitmapFont();
		buttonText.setUseIntegerPositions(false);
		buttonText.getData().setScale(0.02F);
		buttonText.setColor(Color.BLACK);

		textures = new TextureAtlas(Gdx.files.internal("textures.atlas"));
	}
}
