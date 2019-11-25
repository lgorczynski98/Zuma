package sample;

import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.util.ArrayList;
import java.util.List;

public class Ball extends Circle {

    private static List<Point2D> pathPoints = new ArrayList<>();

    private Point2D nextPathPoint;
    private int pathPointsIterator = 0;
    private static final int BALL_RADIUS = 30;

    public Ball() {
        this.nextPathPoint = pathPoints.get(pathPointsIterator);
        this.setRadius(BALL_RADIUS);
        this.setStroke(Color.BLACK);
    }

    public static void setPathPoints(List<Point2D> pointsList){
        pathPoints = pointsList;
    }

    public void setNextPoint(){
        setNextX();
        setNextY();
        if(this.getCenterX() == nextPathPoint.getX() && this.getCenterY() == nextPathPoint.getY()){
            pathPointsIterator++;
            nextPathPoint = pathPoints.get(pathPointsIterator);
        }
    }

    public void boostSetNextPoint(int booster){
        for (int i = 0; i < booster; i++) {
            setNextPoint();
        }
    }

    public void setNextPathPoint() {
        this.nextPathPoint = pathPoints.get(pathPointsIterator);
        this.setCenterX((int)this.getCenterX());
        this.setCenterY((int)this.getCenterY());
    }

    public int getPathPointsIterator() {
        return pathPointsIterator;
    }

    public void setPathPointsIterator(int pathPointsIterator) {
        this.pathPointsIterator = pathPointsIterator;
    }

    private void setNextX(){
        if (this.getCenterX() < nextPathPoint.getX())
            this.setCenterX(this.getCenterX() + 0.5);
        else if (this.getCenterX() > nextPathPoint.getX())
            this.setCenterX(this.getCenterX() - 0.5);
    }

    private void setNextY(){
        if (this.getCenterY() < nextPathPoint.getY())
            this.setCenterY(this.getCenterY() + 0.5);
        else if (this.getCenterY() > nextPathPoint.getY())
            this.setCenterY(this.getCenterY() - 0.5);
    }

    public static int getBallRadius(){
        return BALL_RADIUS;
    }
}
