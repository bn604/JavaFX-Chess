package game;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.ReadOnlyIntegerWrapper;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;

public abstract class GameBoard<P extends GamePlayer<?>, T extends Node>
        extends GameWidget<T> {
    
    protected final BooleanProperty pausedProperty = new SimpleBooleanProperty(true);

    protected final ObservableList<P> gamePlayers = FXCollections.observableArrayList();
    
    protected final ReadOnlyIntegerWrapper turnCountProperty;
    
    protected GameBoard(final T node, final int initialTurn) {
        
        super(node);

        turnCountProperty = new ReadOnlyIntegerWrapper(initialTurn);
        
    }
    
    public final BooleanProperty pausedProperty() {
        
        return pausedProperty;
    }

    public final boolean isPaused() {

        return pausedProperty.get();
    }
    
    public final void setPaused(final boolean paused) {
        
        pausedProperty.set(paused);
        
    }
    
    public final ReadOnlyIntegerProperty turnCountProperty() {
        
        return turnCountProperty.getReadOnlyProperty();
    }
    
    public final int getTurnCount() {
        
        return turnCountProperty.get();
    }
    
    protected final void incrementTurnCount() {
        
        turnCountProperty.set(turnCountProperty.get() + 1);
        
    }
    
}