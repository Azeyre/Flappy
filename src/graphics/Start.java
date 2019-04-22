package graphics;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import player.Bird;
import world.Pipe;

public class Start extends Application {
	
	private final int HEIGHT = 800, WIDTH = 800;
	private GraphicsContext gc, pointGc;
	private Bird bird;
	private StackPane img, temp, pointPane;
	private StackPane[] allPane;
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
		allPane = new StackPane[3];
		
		allPane[0] = new StackPane();
		allPane[1] = new StackPane();
		allPane[2] = new StackPane();
		
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
		root.getChildren().addAll(canvas, img , allPane[0], allPane[1], allPane[2], pointPane); //L'oiseau sera au dessus du canvas
		
		s.setTitle("Flappy Bird");
		s.setResizable(false);
		s.setScene(scene);
		
		createPipe();
		addKeyHandler(scene);
		
		s.show();
	}
	
	public static void main(String[] args) {
		Application.launch();
		if(started) timer.cancel();
		System.exit(0);
	}
	
	private void clear() {
		gc.setFill(Color.RED);
		gc.fillRect(0, 0, WIDTH, HEIGHT);
	}
	
	private void addKeyHandler(Scene scene) {
		scene.setOnKeyPressed(ke -> {
			KeyCode keyCode = ke.getCode();
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
								for(int i = 0 ; i < allPane.length ; i++) {allPane[i].setLayoutX(allPane[i].getLayoutX() - 5);}
								
								if(bird.getY() < - 30 || bird.getY() > HEIGHT) {
									started = false;
									finish = true;
									bird.setY(300);
									img.setLayoutY(bird.getY());
									img.setRotate(0);
									timer.cancel();
								} if(allPane[0].getLayoutX() < -80) {
									allPane[0].setLayoutX(1200);
									allPane[0].setLayoutY(rand(-700, -100));
									temp = allPane[0];
									allPane[0] = allPane[1];
									allPane[1] = allPane[2];
									allPane[2] = temp;
								} if(bird.getX() == allPane[0].getLayoutX()) {
									point++;
									drawPoint();
									System.out.println(point);
								}
								checkCollision();
					        }
					    }, 0, 16);
			}
			if(keyCode.equals(KeyCode.SPACE)) {
				ySpeed = -7;
				img.setRotate(-40);
			}
		});
	}
	
	private void createPipe() throws FileNotFoundException {
		Pipe temp1;
		Pipe temp2;
		VBox vbox;
		for(int i = 0 ; i < allPane.length ; i++) {
			temp1 = new Pipe(0,0);
			temp2 = new Pipe(0,0);
			vbox = new VBox();
			vbox.setSpacing(150);
			vbox.getChildren().addAll(temp1.up(),temp2.down());
			allPane[i].getChildren().add(vbox);
			allPane[i].setLayoutX(800 + (i * 400));
			allPane[i].setLayoutY(rand(-700, 0));
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
		pointGc.strokeText(Integer.toString(point), 0, 70);
	}
	
	private boolean checkCollision() {
		if(bird.getX() >= allPane[0].getLayoutX() - 50 && bird.getX() <= allPane[0].getLayoutX() + 100) {
			if(bird.getY() <= allPane[0].getLayoutY() + 680 || bird.getY() >= allPane[0].getLayoutY() + 800) {
				//System.out.println("HIT");
				started = false;
				finish = true;
				bird.setY(300);
				img.setLayoutY(bird.getY());
				img.setRotate(0);
				timer.cancel();
				point = 0;
				drawPoint();
				allPane[0].setLayoutX(800);
				allPane[1].setLayoutX(1200);
				allPane[2].setLayoutX(1600);
			}
		}
		return false;
	}

}
