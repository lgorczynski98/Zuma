package sample;

import javafx.geometry.Point2D;
import javafx.scene.layout.Pane;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class BallLine {
    private Pane pane;
    private volatile List<Ball> balls = new ArrayList<>();
    private Point2D startingPoint;

    public BallLine(Pane pane, Point2D startingPoint){
        this.pane = pane;
        this.startingPoint = startingPoint;
    }

    public void createNewBall(){
        Ball ball = new Ball();
        ball.setFill(Controller.getRandomImageGenerator().nextImagePattern());
        ball.setLayoutX(0);
        ball.setLayoutY(0);
        ball.setCenterX(startingPoint.getX());
        ball.setCenterY(startingPoint.getY());
        balls.add(ball);
        pane.getChildren().add(ball);
    }

    public int size(){
        return balls.size();
    }

    public Ball get(int index){
        return balls.get(index);
    }

    public int indexOf(Ball ball){
        return balls.indexOf(ball);
    }

    public void add(int index, Ball ball){
        balls.add(index, ball);
    }

    public List<Ball> getBallsList(){
        return balls;
    }

    public void removeAll(Set<Ball> sameColorBalls){
        sameColorBalls.forEach(ball -> {
            ball.setVisible(false);
            balls.remove(ball);
        });
    }
}
