package sample;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;

import java.util.Random;

public class RandomImageGenerator {

    private Image[] images;
    private ImagePattern[] imagePatterns;
    private Random radonm;

    public RandomImageGenerator(){
        try {
            radonm = new Random();
            images = new Image[4];
            images[0] = new Image(getClass().getResource("images/football.png").toURI().toString());
            images[1] = new Image(getClass().getResource("images/basketball.png").toURI().toString());
            images[2] = new Image(getClass().getResource("images/poolball.png").toURI().toString());
            images[3] = new Image(getClass().getResource("images/volleyball.png").toURI().toString());

            imagePatterns = new ImagePattern[4];
            for (int i = 0; i < 4; i++) {
                imagePatterns[i] = new ImagePattern(images[i]);
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    public ImagePattern nextImagePattern(){
        return imagePatterns[radonm.nextInt(4)];
    }
}
