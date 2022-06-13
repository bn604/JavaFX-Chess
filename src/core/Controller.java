package core;

import game.chess.ChessBoard;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.util.EnumSet;
import java.util.Set;

final class Controller {
    
    private final Scene scene;
    
    private final ChessBoard chessBoard;
    
    Controller(final Scene scene, final ChessBoard chessBoard) {
        
        this.scene = scene;
        
        this.chessBoard = chessBoard;
        
    }
    
    private final Set<KeyCode> pressedKeys = EnumSet.noneOf(KeyCode.class);
    
    @FXML
    private Button startGameButton;
    
    @FXML
    private void initialize() {
        
        scene.addEventFilter(KeyEvent.KEY_PRESSED, keyEvent -> {
            
            final KeyCode keyCode = keyEvent.getCode();
            
            if (pressedKeys.add(keyCode)) {
                
                keyPress(keyCode);
                
            }
            
        });
        
        scene.addEventFilter(KeyEvent.KEY_RELEASED, keyEvent -> pressedKeys.remove(keyEvent.getCode()));
        
        chessBoard.setPaused(true);
        
        startGameButton.setOnAction(actionEvent -> {
            
            startGameButton.setVisible(false);
            
            chessBoard.setPaused(false);
            
        });
        
    }
    
    private void keyPress(final KeyCode keyCode) {
        
        if ((keyCode == KeyCode.E) && !startGameButton.isVisible()) {
            
            chessBoard.setPaused(!chessBoard.isPaused());
            
        }
        
    }
    
}