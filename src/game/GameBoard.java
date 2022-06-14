package game;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.ReadOnlyIntegerWrapper;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;

public abstract class GameBoard<P extends GamePlayer<?>, T extends Node>
        extends GameWidget<T> {
    
    private final ReadOnlyBooleanWrapper gameStartedProperty = new ReadOnlyBooleanWrapper(false);
    
    protected final BooleanProperty pausedProperty = new SimpleBooleanProperty(true) {
        
        @Override
        public void set(final boolean newValue) {
            
            if (isGameStarted()) {
                
                super.set(newValue);
                
            } else {
                
                throw new IllegalStateException("cannot change pause property until game has started");
                
            }
            
        }
        
    };

    protected final ObservableList<P> gamePlayers = FXCollections.observableArrayList();
    
    protected final ReadOnlyIntegerWrapper turnCountProperty;
    
    protected GameBoard(final T node, final int initialTurn) {
        
        super(node);

        turnCountProperty = new ReadOnlyIntegerWrapper(initialTurn);
        
    }
    
    public final ReadOnlyBooleanProperty gameStartedProperty() {
        
        return gameStartedProperty.getReadOnlyProperty();
    }
    
    public final boolean isGameStarted() {
        
        return gameStartedProperty.get();
    }
    
    protected final void checkStarted() {
        
        if (isGameStarted()) {
            
            throw new IllegalStateException("game has already started");
            
        }
        
    }
    
    public void startGame() {

        gameStartedProperty.set(true);
        
        pausedProperty.set(false);
        
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