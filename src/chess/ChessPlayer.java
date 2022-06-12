package chess;

import javafx.collections.FXCollections;
import javafx.collections.ObservableSet;

public final class ChessPlayer {
    
    private final boolean playerOne;
    
    private final ObservableSet<ChessPiece> chessPieces = FXCollections.observableSet();
    
    public ChessPlayer(final boolean playerOne) {
        
        this.playerOne = playerOne;
        
    }
    
    public boolean isPlayerOne() {

        return playerOne;
    }
    
    public ObservableSet<ChessPiece> getChessPieces() {
        
        return chessPieces;
    }
    
    public ChessPiece addChessPiece(final ChessPiece.Type type) {

        final ChessPiece chessPiece = new ChessPiece(this, type);
        
        chessPieces.add(chessPiece);
        
        return chessPiece;
    }
    
}