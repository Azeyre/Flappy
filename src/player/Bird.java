package player;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Bird {
	
	private double x,y;
	private final static String path = "img/bird_img.png"; 
	private Image image;
	private ImageView imageView;
	
	public Bird(double x, double y) throws FileNotFoundException {
		this.x = x;
		this.y = y;
		image = new Image(new FileInputStream(this.path));
		imageView = new ImageView(image);
	    imageView.setFitHeight(70);
	    imageView.setFitWidth(70);
	}
	
	public double getX() {return x;}
	public double getY() {return y;}

	public void setX(double x) {this.x = x;}
	public void setY(double y) {this.y = y;}
	
	public ImageView draw() {
		imageView.setX(x); 
	    imageView.setY(y);
	    imageView.setLayoutY(400);
	    return imageView;
	}

}
