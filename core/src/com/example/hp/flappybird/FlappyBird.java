package com.example.hp.flappybird;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class FlappyBird extends ApplicationAdapter {
	// texture is the image sprite is the collection of image
	SpriteBatch batch;

	Texture backImg;
	//  we create an array here beacuse we need to change the bird image every time
	Texture[] birds;
	// this will help us to flip between the bird image
	int birdState = 0;

	// these variables will be used in setting up the gravity for the bird that is
	// how it falls down and rise up
	float birdYcoordinate = 0;
	float velocity = 0;
	float gravity = 1.5f;

	// this var is to make sure that the gameOver state is not reached
	int gameState = 0;

	@Override
	public void create () {
		batch = new SpriteBatch();
		backImg = new Texture("bg.png");
		birds = new Texture[2];
		birds[0] = new Texture("bird.png");
		birds[1] = new Texture("bird2.png");
		birdYcoordinate = Gdx.graphics.getHeight()/2 - birds[0].getHeight()/2;
	}

	@Override
	public void render () {

		// this condition will check if the bird is touched or not


		if (gameState !=0) {

			// the if condition to rise the bird up by 30
			if (Gdx.input.justTouched()){

				velocity = -30;
			}

			// this if will stopp the bird from going off screen
			if (birdYcoordinate > 0  || velocity < 0) {
                // we will increase the velocity every time and then decrease the height by that velocity
                velocity = velocity + gravity;
                birdYcoordinate -= velocity;
            }

		} else {

			if (Gdx.input.justTouched()){
				gameState = 1;
			}
		}

		if (birdState == 0) {
			birdState = 1;
		} else {
			birdState = 0;
		}

		batch.begin();
		batch.draw(backImg, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		// because the bird image is a square in png therefore we need to subtract the width of the
		// bird
		batch.draw(birds[birdState], Gdx.graphics.getWidth() / 2 - birds[birdState].getWidth() / 2,
				birdYcoordinate);
		batch.end();

	}
}
