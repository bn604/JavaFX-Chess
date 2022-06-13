package game.chess;

import game.GamePlayer;

public final class ChessPlayer
        extends GamePlayer<ChessPiece> {
    
    private final boolean playerOne;
    
    ChessPlayer(final boolean playerOne) {
        
        this.playerOne = playerOne;
        
    }
    
    public boolean isPlayerOne() {

        return playerOne;
    }
    
}