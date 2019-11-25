package sample;

import javafx.animation.AnimationTimer;
import javafx.geometry.Point2D;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BallsLineTimer extends AnimationTimer implements Observator{

    private int counter = 0;
    private final int COUNTER_MODULATOR = 120;
    private BallLine ballLine;
    private List<Ball> enteringBalls = new ArrayList<>();
    private Observable observable;

    public BallsLineTimer(Observable observable){
        observable.addObservator(this);
    }

    public void start(Pane pane, Point2D startingPoint){
        ballLine = new BallLine(pane, startingPoint);
        this.start();
    }

    @Override
    public void handle(long l) {
        counter = (counter + 1) % COUNTER_MODULATOR;
        if(counter == 0)
            ballLine.createNewBall();
        try {
            if(isAnyBallEntering())
                moveBallsWhileEntering();
            else
                moveBallsInLine();
        }
        catch(Exception e) {
            this.stop();
            observable.removeObservator(this);
        }
    }

    private void moveBallsInLine(){
        if(ballLine.size() == 1) {
            ballLine.get(0).setNextPoint();
        }
        else{
            moveBallsInLineWhenAFewBallsInLine();
        }
    }

    private void moveBallsInLineWhenAFewBallsInLine(){
        for (int i = ballLine.size() - 1; i >= 0; i--){
            if(i == ballLine.size() - 1)
                ballLine.get(i).setNextPoint();
            else if(checkCollisions(ballLine.get(i), ballLine.get(i + 1)))
                try {
                    if (!collisionAppearsWithCovering(ballLine.get(i), ballLine.get(i - 1)))
                        ballLine.get(i).setNextPoint();
                }
                catch(IndexOutOfBoundsException e) {
                    ballLine.get(i).setNextPoint();
                }
        }
    }

    private void moveBallsWhileEntering(){
        Ball enteringBall = findEnteringBallWithLowestIndex();
        int indexOfEnteringBall = ballLine.indexOf(enteringBall);
        if(checkIfAlmostCollision(enteringBall, ballLine.get(indexOfEnteringBall + 1))){
            ballLine.get(indexOfEnteringBall).boostSetNextPoint(4);
            for (int i = indexOfEnteringBall - 1; i >= 0; i--) {
                if(checkIfAlmostCollision(ballLine.get(i), ballLine.get(i + 1)))
                    ballLine.get(i).boostSetNextPoint(4);
            }
        }
        else{
            enteringBalls.remove(enteringBall);
            searchForThreeOrMoreSameBalls(enteringBall);
        }
    }

    private Ball findEnteringBallWithLowestIndex(){
        Ball lowestIndexBall = enteringBalls.get(0);
        for (Ball enteringBall : enteringBalls) {
            if(ballLine.indexOf(enteringBall) < ballLine.indexOf(lowestIndexBall))
                lowestIndexBall = enteringBall;
        }
        return lowestIndexBall;
    }

    private boolean collisionAppearsWithCovering(Ball firstBall, Ball secondBall){
        int smallerRadius = (int)(Ball.getBallRadius() * 0.9);
        int doubleSmallerRadius = 2 * smallerRadius;
        return firstBall.getBoundsInParent().intersects(secondBall.getCenterX() - smallerRadius, secondBall.getCenterY() - smallerRadius, doubleSmallerRadius, doubleSmallerRadius);
    }

    private boolean checkCollisions(Ball firstBall, Ball secondBall){
        return firstBall.getBoundsInParent().intersects(secondBall.getBoundsInParent());
    }

    private boolean checkIfAlmostCollision(Ball firstBall, Ball secondBall){
        int biggerRadius = (int)(Ball.getBallRadius() * 1.2);
        int doubleBiggerRadius = 2 * biggerRadius;
        return firstBall.getBoundsInParent().intersects(secondBall.getCenterX() - biggerRadius, secondBall.getCenterY() - biggerRadius, doubleBiggerRadius, doubleBiggerRadius);
    }

    public void addNewBall(int index, Ball ball){
        ball.setPathPointsIterator(ballLine.get(index).getPathPointsIterator() + 1/*Ball.getPercentOfPathPoints(5)*/);
        ball.setNextPathPoint();
        ballLine.add(index, ball);
        enteringBalls.add(ball);
    }

    private void searchForThreeOrMoreSameBalls(Ball ball){
        Set<Ball> sameColorBalls = new HashSet<>();
        sameColorBalls.add(ball);

        addSameColorBallsWithHigherIndex(ball, sameColorBalls);
        addSameColorBallsWithLowerIndex(ball, sameColorBalls);

        if(sameColorBalls.size() >= 3)
            ballLine.removeAll(sameColorBalls);
    }

    private void addSameColorBallsWithHigherIndex(Ball ball, Set<Ball> sameColorBalls){
        Paint color = ball.getFill();
        for (int i = ballLine.indexOf(ball); i < ballLine.size(); i++) {
            if(ballLine.get(i).getFill() == color)
                sameColorBalls.add(ballLine.get(i));
            else
                break;
        }
    }

    private void addSameColorBallsWithLowerIndex(Ball ball, Set<Ball> sameColorBalls){
        Paint color = ball.getFill();
        for (int i = ballLine.indexOf(ball); i >= 0; i--){
            if(ballLine.get(i).getFill() == color)
                sameColorBalls.add(ballLine.get(i));
            else
                break;
        }
    }

    private boolean isAnyBallEntering(){
        return (enteringBalls.size() > 0);
    }

    public int getBallIndex(Ball ball){
        return ballLine.indexOf(ball);
    }

    public BallLine getBalls() {
        return ballLine;
    }

    @Override
    public void handleNotification(boolean isPauzed) {
        if(isPauzed) this.stop();
        else this.start();
    }
}
