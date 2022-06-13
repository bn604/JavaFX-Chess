package game.chess;

import game.GamePlayer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public final class ChessPlayer
        extends GamePlayer<ChessPiece> {
    
    private final boolean playerOne;
    
    private final ObservableList<ChessPiece> capturedPieces = FXCollections.observableArrayList();
    
    ChessPlayer(final boolean playerOne) {
        
        this.playerOne = playerOne;
        
    }
    
    public boolean isPlayerOne() {

        return playerOne;
    }

    public ObservableList<ChessPiece> getCapturedPieces() {

        return capturedPieces;
    }
    
}