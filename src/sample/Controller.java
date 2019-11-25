package sample;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @FXML private Pane pane;
    @FXML private Group cannon;
    @FXML private Path path;
    @FXML private Circle outerCircle;
    @FXML ImageView pathEndImage;
    @FXML private Circle nextBallCircle;
    private List<Point2D> pathPoints = new ArrayList<>();
    private Point2D startingPoint;
    private volatile double turnedAngle;
    private static RandomImageGenerator randomImageGenerator;
    private Pauze pauze;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        randomImageGenerator = new RandomImageGenerator();
        pauze = new Pauze();
        prepareFrog();
        setCannonRotationPropery();
        setPathShapeAndPropertioes();
    }

    private void prepareFrog(){
        nextBallCircle.setFill(randomImageGenerator.nextImagePattern());
        try {
            Image image = new Image(getClass().getResource("images/rotated_frog.png").toURI().toString());
            outerCircle.setFill(new ImagePattern(image));
            outerCircle.setStroke(Color.TRANSPARENT);
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    private void setCannonRotationPropery(){
        pane.setOnMouseMoved(mouseEvent -> {
            if (!pauze.isPauzed()){
                double mouseX = mouseEvent.getX();
                double mouseY = mouseEvent.getY();

                double xDistance = mouseX - cannon.getLayoutX();
                double yDistance = mouseY - cannon.getLayoutY();

                turnedAngle = Math.toDegrees(Math.atan2(yDistance, xDistance)) + 90;
                cannon.setRotate(turnedAngle);
            }
        });
    }

    private void setPathShapeAndPropertioes(){
        setMousePressedToDrawPath();
        setMouseDraggedToDrawPath();
        setMouseReleasedAfterDrawingPath();
    }

    private void setMousePressedToDrawPath(){
        pane.setOnMousePressed(mouseEvent -> {
            path.getElements().clear();
            path.getElements().add(new MoveTo(mouseEvent.getX(), mouseEvent.getY()));
            startingPoint = new Point2D(mouseEvent.getX(), mouseEvent.getY());
        });
    }

    private void setMouseDraggedToDrawPath(){
        pane.setOnMouseDragged(mouseEvent -> {
            path.getElements().add(new LineTo(mouseEvent.getX(), mouseEvent.getY()));
            pathPoints.add(new Point2D(mouseEvent.getX(), mouseEvent.getY()));
        });
    }

    private void setMouseReleasedAfterDrawingPath(){
        pane.setOnMouseReleased(mouseDragEvent -> {
            setPathEndImage(mouseDragEvent.getX(), mouseDragEvent.getY());
            clearMouseListeners();
            try {
                path.setStroke(new ImagePattern(new Image(getClass().getResource("images/ground.jpg").toURI().toString())));
            }
            catch(Exception e) {
                e.printStackTrace();
            }
            setBallsLineTimerPath();
            setMousePressedShooting();
        });
    }

    private void setPathEndImage(double x, double y){
        pathEndImage.setX(x - pathEndImage.getFitWidth() / 2);
        pathEndImage.setY(y - pathEndImage.getFitHeight() / 2);
        pathEndImage.setVisible(true);
    }

    private void clearMouseListeners(){
        pane.setOnMousePressed(mouseEvent -> {});
        pane.setOnMouseDragged(mouseEvent -> {});
        pane.setOnMouseReleased(mouseEvent -> {});
    }

    private void setBallsLineTimerPath(){
        Ball.setPathPoints(pathPoints);
        pauze.addObservator(BallsLineTimer.getInstance());
        BallsLineTimer.getInstance().start(pane, startingPoint);
    }

    private void setMousePressedShooting(){
        pane.setOnMousePressed(mouseEvent -> {
            if (mouseEvent.getButton() == MouseButton.PRIMARY && !pauze.isPauzed()){
                BallShootTimer ballShootTimer = new BallShootTimer(turnedAngle, pane, nextBallCircle.getFill(), pauze);
                nextBallCircle.setFill(randomImageGenerator.nextImagePattern());
                ballShootTimer.start();
            }
            else if(mouseEvent.getButton() == MouseButton.SECONDARY){
                preparePauze();
            }
        });
    }

    private void preparePauze(){
        pauze.changeState();
        pauze.notifyObservators();
    }

    public static RandomImageGenerator getRandomImageGenerator() {
        return randomImageGenerator;
    }
}
