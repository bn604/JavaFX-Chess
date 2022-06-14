package core;

import game.chess.ChessBoard;
import game.chess.ChessPiece;
import game.chess.ChessPlayer;
import game.chess.ChessTile;
import javafx.animation.FadeTransition;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.util.Duration;

import java.util.EnumSet;
import java.util.Set;
import java.util.function.Function;

final class Controller {
    
    private final Scene scene;
    
    private final ChessBoard chessBoard;
    
    Controller(final Scene scene, final ChessBoard chessBoard) {
        
        this.scene = scene;
        
        this.chessBoard = chessBoard;
        
    }
    
    private final Set<KeyCode> pressedKeys = EnumSet.noneOf(KeyCode.class);
    
    private final BooleanProperty overlayVisibleProperty = new SimpleBooleanProperty(false);
    
    @FXML
    private Button startGameButton;
    
    @FXML
    private Label playerLabel;
    
    @FXML
    private Label pieceLabel;
    
    @FXML
    private void initialize() {
        
        scene.addEventFilter(KeyEvent.KEY_PRESSED, keyEvent -> {
            
            final KeyCode keyCode = keyEvent.getCode();
            
            if (pressedKeys.add(keyCode)) {
                
                keyPress(keyCode);
                
            }
            
        });
        
        scene.addEventFilter(KeyEvent.KEY_RELEASED, keyEvent -> pressedKeys.remove(keyEvent.getCode()));
        
        final Function<ChessPlayer, String> nameFunction = chessPlayer -> "Player " + (chessPlayer.isPlayerOne() ? "1" : "2");
        
        playerLabel.textProperty().bind(new ObjectBinding<>() {

            { super.bind(chessBoard.currentChessPlayerProperty()); }
            
            @Override
            protected String computeValue() {
                
                return nameFunction.apply(chessBoard.getCurrentChessPlayer());
            }
            
        });
        
        pieceLabel.textProperty().bind(new ObjectBinding<>() {

            { super.bind(chessBoard.hoveringChessTileProperty()); }
            
            @Override
            protected String computeValue() {
                
                final ChessTile hoveringChessTile = chessBoard.getHoveringChessTile();
                
                if (hoveringChessTile == null) {
                    
                    return null;
                    
                }
                
                final ChessPiece hoveringChessPiece = hoveringChessTile.getChessPiece();
                
                if (hoveringChessPiece == null) {
                    
                    return null;
                    
                }

                return nameFunction.apply(hoveringChessPiece.getGamePlayer()) + " " + hoveringChessPiece.getType().getDisplayName();
            }
            
        });
        
        final var startButtonFade = new FadeTransition(Duration.millis(ChessBoard.START_ANIMATION_MILLISECONDS), startGameButton);
        
        startButtonFade.setByValue(-1.0);
        
        startButtonFade.setOnFinished(actionEvent -> {
            
            overlayVisibleProperty.set(true);
            
            startGameButton.setVisible(false);

            chessBoard.startGame();
            
        });
        
        playerLabel.visibleProperty().bind(overlayVisibleProperty);
        pieceLabel.visibleProperty().bind(overlayVisibleProperty);
        
        startGameButton.setOnAction(actionEvent -> startButtonFade.playFromStart());
        
    }
    
    private void keyPress(final KeyCode keyCode) {
        
        if (chessBoard.isGameStarted()) {

            switch (keyCode) {
                
                case E -> chessBoard.setPaused(!chessBoard.isPaused());
                
                case O -> overlayVisibleProperty.set(!overlayVisibleProperty.get());
                
            }
            
        }
        
    }
    
}