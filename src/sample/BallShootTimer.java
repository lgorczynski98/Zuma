package sample;

import javafx.animation.AnimationTimer;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

import java.util.List;

public class BallShootTimer extends AnimationTimer implements Observator {

    private final double STARTX = 500;
    private final double STARTY = 500;
    private final int BALL_RADIUS = 30;
    private static final double sqrt2 = Math.sqrt(2);

    private double MOVEBASE = 6;
    private double offsetX;
    private double offsetY;

    private Ball ball;

    private Observable observable;

    public BallShootTimer(double angle, Pane pane, Paint color, Observable observable) {
        offsetX = MOVEBASE * Math.sin(Math.toRadians(angle));
        offsetY = MOVEBASE * (-Math.cos(Math.toRadians(angle)));
        createNewBall(pane, color);
        this.observable = observable;
        observable.addObservator(this);
    }

    private void createNewBall(Pane pane, Paint color){
        ball = new Ball();
        ball.setRadius(BALL_RADIUS);
        ball.setFill(color);
        ball.setStroke(Color.BLACK);
        ball.setLayoutX(0);
        ball.setLayoutY(0);
        ball.setCenterX(STARTX);
        ball.setCenterY(STARTY);
        pane.getChildren().add(ball);
    }

    @Override
    public void handle(long l) {
        ball.setCenterX(ball.getCenterX() + offsetX);
        ball.setCenterY(ball.getCenterY() + offsetY);
        checkIfCollisionAppears();
        checkIfOutsideTheMap();
    }

    private void checkIfCollisionAppears(){
        int halfOfCirlceInnerBoxSide = (int)(Ball.getBallRadius() / sqrt2);
        int circleInnerBoxSide = 2 * halfOfCirlceInnerBoxSide;
        for(Ball ballInLine : BallsLineTimer.getInstance().getBalls().getBallsList()){
            if(ball.getBoundsInParent().intersects(ballInLine.getCenterX() - halfOfCirlceInnerBoxSide, ballInLine.getCenterY() - halfOfCirlceInnerBoxSide, circleInnerBoxSide, circleInnerBoxSide)){
                handleCollision(ballInLine);
                return;
            }
        }
    }

    private void handleCollision(Ball ballInLine){
        int index = BallsLineTimer.getInstance().getBallIndex(ballInLine);
        BallsLineTimer.getInstance().addNewBall(index, ball);
        observable.removeObservator(this);
        this.stop();
    }

    private void checkIfOutsideTheMap(){
        if (ball.getCenterX() < 0 || ball.getCenterX() > 1000 || ball.getCenterY() < 0 || ball.getCenterY() > 1000)
            observable.removeObservator(this);
    }

    @Override
    public void handleNotification(boolean isPauzed) {
        if(isPauzed) this.stop();
        else this.start();
    }
}
