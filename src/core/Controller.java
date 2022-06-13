package core;

import game.chess.ChessBoard;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;

final class Controller {
    
    private final Scene scene;
    
    private final ChessBoard chessBoard;
    
    Controller(final Scene scene, final ChessBoard chessBoard) {
        
        this.scene = scene;
        
        this.chessBoard = chessBoard;
        
    }
    
    @FXML
    private Button startGameButton;
    
    @FXML
    private void initialize() {
        
        startGameButton.setOnAction(actionEvent -> {
            
            startGameButton.setVisible(false);
            
            chessBoard.setPaused(false);
            
        });
        
    }
    
}