package game;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;

public abstract class GameBoard<P extends GamePlayer<?>, T extends Node>
        extends GameWidget<T> {
    
    protected final ObservableList<P> gamePlayers = FXCollections.observableArrayList();
    
    protected final BooleanProperty pausedProperty = new SimpleBooleanProperty(true);
    
    protected GameBoard(final T node) {
        
        super(node);
        
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
    
}