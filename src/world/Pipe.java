package world;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Pipe {
	
	private double x, y;
	private final static String path = "img/pipe_img.png"; 
	private Image image;
	private ImageView imageView;
	
	public Pipe(double x, double y) throws FileNotFoundException {
		this.x = x;
		this.y = y;
		image = new Image(new FileInputStream(this.path));
		imageView = new ImageView(image);
	    imageView.setFitHeight(700);
	    imageView.setFitWidth(100);
	}
	
	public double getX() {return x;}
	public double getY() {return y;}

	public void setX(double x) {this.x = x;}
	public void setY(double y) {this.y = y;}
	
	public ImageView up() {
	    return imageView;
	}
	
	public ImageView down() {
	    imageView.setRotate(180);
	    return imageView;
	}
}
