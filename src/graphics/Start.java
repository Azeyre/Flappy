package graphics;

import java.io.FileNotFoundException;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import player.Bird;
import world.Pipe;

public class Start extends Application {
	
	private final int HEIGHT = 800, WIDTH = 800;
	private GraphicsContext gc, pointGc;
	private Bird bird;
	private StackPane img, pointPane;
	private ImageView temp1, temp2;
	private ImageView[] pipesView;
	private static boolean started = false;
	private boolean finish = false;
	private static Timer timer;
	private double ySpeed = 1.0;
	private int point = 0;
	
	public void start(Stage s) throws FileNotFoundException {
		bird = new Bird(300,300); //Creation de l'oiseau
		Group root = new Group(); //Groupe racine
		img = new StackPane(); //StackPane comportant l'oiseau
		pointPane = new StackPane();
		pipesView = new ImageView[6];
		createPipe();
		
		Scene scene = new Scene(root,WIDTH,HEIGHT);
		
		Canvas canvas = new Canvas(WIDTH,HEIGHT);
		Canvas pointCanvas = new Canvas(100,100);
		gc = canvas.getGraphicsContext2D();
		pointGc = pointCanvas.getGraphicsContext2D();
		//clear();
		drawPoint();
		img.setLayoutX(bird.getX());
		img.setLayoutY(bird.getY());
		
		img.getChildren().add(bird.draw());
		pointPane.getChildren().add(pointCanvas);
		pointPane.setLayoutX(WIDTH / 2);
		pointPane.setLayoutY(100);
		root.getChildren().addAll(canvas, img , pipesView[0], pipesView[1],pipesView[2],pipesView[3],pipesView[4],pipesView[5], pointPane); //L'oiseau sera au dessus du canvas
		
		s.setTitle("Flappy Bird");
		s.setResizable(false);
		s.setScene(scene);
		
		addKeyHandler(scene);
		
		s.show();
	}
	
	private void createPipe() throws FileNotFoundException {
		for(int i = 0 ; i < 3 ; i++) {
			Pipe p1 = new Pipe(0,0);
			Pipe p2 = new Pipe(0,0);
			
			pipesView[(i*2)] = p1.up();
			pipesView[(i*2) + 1] = p2.down();
			
			pipesView[(i*2)].setX(800 + (i * 400));
			pipesView[(i*2)+1].setX(800 + (i * 400));
			
			pipesView[(i*2)].setY(rand(-700, -150));
			pipesView[(i*2)+1].setY(pipesView[(i*2)].getY() + 700 + 150);
		}
	}
	
	public static void main(String[] args) {
		Application.launch();
		if(started) timer.cancel();
		System.exit(0);
	}
	
	private void addKeyHandler(Scene scene) {
		scene.setOnKeyPressed(ke -> {
			KeyCode keyCode = ke.getCode();
			if(keyCode.equals(KeyCode.SPACE)) {
				ySpeed = -9;
				img.setRotate(-40);
				loop();
			}
		});
	}
	
	private void loop() {
		if(!started) {
			started = true;
			finish = false;
			timer = new Timer();
			timer.schedule(
				    new TimerTask() {

				        @Override
				        public void run() {
							bird.setY(bird.getY() + ySpeed);
							img.setLayoutY(bird.getY());
							if(img.getRotate() < 75) img.setRotate(img.getRotate() + 1.5);
							ySpeed += 0.4;
							for(int i = 0 ; i < pipesView.length ; i++) {pipesView[i].setX(pipesView[i].getX() - 5);}
							
							if(bird.getY() < - 30 || bird.getY() > HEIGHT) {
								started = false;
								finish = true;
								bird.setY(300);
								img.setLayoutY(bird.getY());
								img.setRotate(0);
								timer.cancel();
							} if(pipesView[0].getX() < -80) {
								pipesView[0].setX(1200);
								pipesView[1].setX(1200);
								pipesView[0].setY(rand(-700, -150));
								pipesView[1].setY(pipesView[0].getY() + 700 + 150);
								temp1 = pipesView[0];
								temp2 = pipesView[1];
								pipesView[0] = pipesView[2];
								pipesView[1] = pipesView[3];
								pipesView[2] = pipesView[4];
								pipesView[3] = pipesView[5];
								pipesView[4] = temp1;
								pipesView[5] = temp2;
							} if(bird.getX() == pipesView[0].getX()) {
								point++;
								drawPoint();
								//System.out.println(point);
							}
							checkCollision();
				        }
				    }, 0, 16);
		}
	}
	
	private static int rand(int min, int max) {
		Random r = new Random();
		return r.nextInt((max - min) + 1) + min;
	}
	
	private void drawPoint() {
		pointGc.clearRect(0, 0, 100, 100);
		pointGc.setFill(Color.WHITE);
		pointGc.setFont(new Font("Arial bold", 68));
		pointGc.fillText(Integer.toString(point), 0, 70);
		pointGc.setStroke(Color.RED);
		pointGc.setLineWidth(3);
		pointGc.strokeText(Integer.toString(point), 0, 70);
	}
	
	private boolean checkCollision() {
		if(bird.getX() >= pipesView[0].getX() - 50 && bird.getX() <= pipesView[0].getX() + 100) {
			if(bird.getY() <= pipesView[0].getY() + 680 || bird.getY() >= pipesView[1].getY() - 50) {
				//System.out.println("HIT");
				started = false;
				finish = true;
				bird.setY(300);
				img.setLayoutY(bird.getY());
				img.setRotate(0);
				timer.cancel();
				point = 0;
				drawPoint();
				for(int i = 0 ; i < 3 ; i++) {					
					pipesView[(i*2)].setX(800 + (i * 400));
					pipesView[(i*2)+1].setX(800 + (i * 400));
					
					pipesView[(i*2)].setY(rand(-700, -150));
					pipesView[(i*2)+1].setY(pipesView[(i*2)].getY() + 700 + 150);
				}
			}
		}
		return false;
	}

}
