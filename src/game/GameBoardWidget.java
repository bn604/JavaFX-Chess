package game;

import javafx.scene.Node;

public abstract class GameBoardWidget<G extends GameBoard<?, ?>, T extends Node>
        extends GameWidget<T> {
    
    protected final G gameBoard;
    
    protected GameBoardWidget(final G gameBoard, final T node) {
        
        super(node);
        
        this.gameBoard = gameBoard;
        
    }

    public final G getGameBoard() {

        return gameBoard;
    }
    
}