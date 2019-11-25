package sample;

public interface Observable {

    void addObservator(Observator observator);
    void removeObservator(Observator observator);
    void notifyObservators();

}
