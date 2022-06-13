package game;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public abstract class GamePlayer<T extends GamePiece<?, ?>> {
    
    protected final ObservableList<T> gamePieces = FXCollections.observableArrayList();
    
    protected GamePlayer() {
        
        super();
        
    }

    public final ObservableList<T> getGamePieces() {

        return gamePieces;
    }
    
}