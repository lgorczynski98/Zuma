package sample;

import java.util.ArrayList;
import java.util.List;

public class Pauze implements Observable{

    private boolean isPauzed;
    private List<Observator> observators;

    public Pauze(){
        isPauzed = false;
        observators = new ArrayList<>();
    }

    public boolean isPauzed(){
        return isPauzed;
    }

    public void changeState(){
        isPauzed = !isPauzed;
    }

    @Override
    public void addObservator(Observator observator) {
        observators.add(observator);
    }

    @Override
    public void removeObservator(Observator observator) {
        observators.remove(observator);
    }

    @Override
    public void notifyObservators() {
        for (Observator observator : observators) {
            observator.handleNotification(isPauzed());
        }
    }
}
