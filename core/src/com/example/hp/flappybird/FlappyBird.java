package com.example.hp.flappybird;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

import java.util.Random;

import static com.badlogic.gdx.graphics.Color.RED;
import static com.badlogic.gdx.graphics.Color.WHITE;

public class FlappyBird extends ApplicationAdapter {
	// texture is the image sprite is the collection of image
	SpriteBatch batch;
	// this is just like batch but is used for the shapes
	//ShapeRenderer shapeRenderer;

	Texture backImg;
	//  we create an array here beacuse we need to change the bird image every time
	Texture[] birds;

	Texture gameOver;

	// this will help us to flip between the bird image
	int birdState = 0;

	// to detect collision we have to create a shape for the bird
	Circle birdCircle;

	// similarly for the tubes
	Rectangle[] topTubeRectangles;
	Rectangle[] bottomTubeRectangles;

	// these variables will be used in setting up the gravity for the bird that is
	// how it falls down and rise up
	float birdYcoordinate = 0;
	float velocity = 0;
	float gravity = 1.7f;

	// this var is to make sure that the gameOver state is not reached
	int gameState = 0;

	// these are the pipes required
	Texture topTube;
	Texture bottomTube;

	// this is the gap between the pipes which we have to alter
	float gap = 450;
	// this will be used to set the different height of the tubes
	float maxTubeOffset;

	Random gapGenerator;

	// this will help us to manipulate the tube to move left
	float tubeVelocity = 4;
	// we want to create more than just a set of tube to get an infinite loop effect
	int noOfTubes = 4;
	// we have coordinates of 4 tubes
	float[] tubeXcoordinate = new float[noOfTubes];
	float distanceBetweenTubes;
	float[] tubeOffset = new float[noOfTubes];

	// to maintain the score
	int score = 0;
	int scoringTube = 0;

	// to write the score we need a font
	BitmapFont font;


	@Override
	public void create () {
		batch = new SpriteBatch();
		backImg = new Texture("bg.png");
		gameOver = new Texture("gameover.png");
		//shapeRenderer = new ShapeRenderer();
		birdCircle = new Circle();

		birds = new Texture[2];
		birds[0] = new Texture("bird.png");
		birds[1] = new Texture("bird2.png");

		topTube = new Texture("toptube.png");
		bottomTube = new Texture("bottomtube.png");
		gapGenerator = new Random();
		maxTubeOffset = Gdx.graphics.getHeight() / 2 - gap/2 - 100;
		distanceBetweenTubes = Gdx.graphics.getWidth() * 3/4;
		topTubeRectangles = new Rectangle[noOfTubes];
		bottomTubeRectangles = new Rectangle[noOfTubes];
		font = new BitmapFont();
		font.setColor(WHITE);
		// this will set the size of the font
		font.getData().setScale(10);
		startGame();

	}

	public void startGame(){
		birdYcoordinate = Gdx.graphics.getHeight()/2 - birds[0].getHeight()/2;
		// we set the coordinates of each tubes using this loop
		for (int i = 0; i < noOfTubes; i++){
			// generates a random number between 0 and 1
			tubeOffset[i] = (gapGenerator.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - gap - 200);

			// we dont want all the tubes to have the initial coordinate therefore we change the
			tubeXcoordinate[i] = Gdx.graphics.getWidth()/2 - topTube.getWidth()/2 +
					Gdx.graphics.getWidth() + i * distanceBetweenTubes;

			topTubeRectangles[i] = new Rectangle();
			bottomTubeRectangles[i] = new Rectangle();
		}
	}

	@Override
	public void render () {

		batch.begin();
		batch.draw(backImg, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());


		if (gameState == 1) {


			// here we check the left coordinate of the scoring tube passes the width
			if ( tubeXcoordinate[scoringTube] < Gdx.graphics.getWidth() / 2){
				score++ ;
				Gdx.app.log("Score", String.valueOf(score));

				// because we have only 4 tubes so we cannot allow the scoringTube to exceed by 4
				// which is the noOfTubes
				// we have used -1 because array starts from 0
				if (scoringTube < noOfTubes - 1){
					scoringTube++;
				}else {
					scoringTube = 0;
				}
			}

			// the if condition to rise the bird up by 30
			if (Gdx.input.justTouched()){

				velocity = -30;

			}

			for (int i = 0; i < noOfTubes; i++) {
				//we have to check if the pipes have gone completely off screen
				if (tubeXcoordinate[i] < -topTube.getWidth()){
					// this will move the pipe 4 times the distance between the pipes
					tubeXcoordinate[i] = noOfTubes * distanceBetweenTubes;
					// this is to reset the pipe positions
					tubeOffset[i] = (gapGenerator.nextFloat() - 0.5f) * (Gdx.graphics.getHeight()
							- gap - 200);
				}else {
					tubeXcoordinate[i] = tubeXcoordinate[i] - tubeVelocity;

				}

				// we add the pipes here because we have to display it only when the game is active
				batch.draw(topTube,
						tubeXcoordinate[i],
						Gdx.graphics.getHeight() / 2 + gap / 2 + tubeOffset[i]);
				batch.draw(bottomTube,
						tubeXcoordinate[i],
						Gdx.graphics.getHeight() / 2 - gap / 2 - bottomTube.getHeight() +
								tubeOffset[i]);

				// the shape dimensions of the tubes will be exactly as that of tubes
				// we can use the simplest Rectangle method to draw the circle
				topTubeRectangles[i] = new Rectangle(tubeXcoordinate[i],
						Gdx.graphics.getHeight() / 2 + gap / 2 + tubeOffset[i],
						topTube.getWidth(),
						topTube.getHeight());
				bottomTubeRectangles[i] = new Rectangle(tubeXcoordinate[i],
						Gdx.graphics.getHeight()/2 - gap/2 - bottomTube.getHeight() + tubeOffset[i],
						topTube.getWidth(),
						topTube.getHeight());
			}

			// this if will stop the bird from going off screen
			if (birdYcoordinate > 0) {
                // we will increase the velocity every time and then decrease the height by that velocity
                velocity = velocity + gravity;
                birdYcoordinate -= velocity;
            }else {
				gameState = 2;
			}

		} else if (gameState == 0){

			if (Gdx.input.justTouched()){
				gameState = 1;
			}

		}else if (gameState == 2){
			batch.draw(gameOver, Gdx.graphics.getWidth()/2-gameOver.getWidth()/2,
					Gdx.graphics.getHeight()/2 - gameOver.getHeight()/2);

			// once game over then touch the screen
			if (Gdx.input.justTouched()){
				gameState = 1;
				startGame();
				scoringTube = 0;
				score = 0;
				velocity = 0;
			}
		}

		if (birdState == 0) {
			birdState = 1;
		} else {
			birdState = 0;
		}

		// because the bird image is a square in png therefore we need to subtract the width of the
		// bird
		batch.draw(birds[birdState], Gdx.graphics.getWidth() / 2 - birds[birdState].getWidth() / 2,
				birdYcoordinate);
		// to show the score on the screen
		font.draw(batch,String.valueOf(score),100,200);

		batch.end();

		birdCircle.set(Gdx.graphics.getWidth()/2, birdYcoordinate + birds[birdState].getHeight()/2,
				birds[birdState].getWidth()/2);




			// this is to set the shape of the bird
		/*shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
		shapeRenderer.setColor(RED);
			// we have to set the shape of the circle using the shape of the bird
		shapeRenderer.circle(birdCircle.x, birdCircle.y, birdCircle.radius);
		*/
		for (int i = 0; i < noOfTubes; i++){
			/*shapeRenderer.rect(tubeXcoordinate[i],
					Gdx.graphics.getHeight() / 2 + gap / 2 + tubeOffset[i],
					topTube.getWidth(),
					topTube.getHeight());
			shapeRenderer.rect(tubeXcoordinate[i],
					Gdx.graphics.getHeight()/2 - gap/2 - bottomTube.getHeight() + tubeOffset[i],
					topTube.getWidth(),
					topTube.getHeight());
			*/
			// now we check for the collision of top rectangle with circle and bottom rectangle with
			// circle if it collides then the gamestate will be set to 2
			if (Intersector.overlaps(birdCircle,topTubeRectangles[i]) ||
					Intersector.overlaps(birdCircle,bottomTubeRectangles[i])){
				gameState = 2;
			}
		}

		//shapeRenderer.end();

	}
}
