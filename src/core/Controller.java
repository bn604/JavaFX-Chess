package core;

import chess.ChessBoard;
import chess.ChessPiece;
import chess.ChessPlayer;
import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.effect.Effect;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.util.Duration;

import java.util.EnumSet;
import java.util.Set;

final class Controller {
    
    private final Scene scene;
    
    private final ChessBoard chessBoard;
    
    Controller(final Scene scene, final ChessBoard chessBoard) {
        
        this.scene = scene;
        
        this.chessBoard = chessBoard;
        
    }
    
    @FXML
    private Button startGameButton;
    
    private boolean paused = true;
    
    private final Set<KeyCode> pressedKeys = EnumSet.noneOf(KeyCode.class);
    
    @FXML
    private void initialize() {
        
        scene.addEventFilter(KeyEvent.KEY_PRESSED, keyEvent -> {
            
            final KeyCode keyCode = keyEvent.getCode();
            
            if (pressedKeys.add(keyCode)) {
                
                keyPress(keyCode);
                
            }
            
        });
        
        scene.addEventFilter(KeyEvent.KEY_RELEASED, keyEvent -> pressedKeys.remove(keyEvent.getCode()));

        setPaused(paused);
        
        final var fadeTransition = new FadeTransition(Duration.millis(200), startGameButton);
        
        fadeTransition.setByValue(-1.0);
        
        fadeTransition.setOnFinished(actionEvent -> {
            
            startGameButton.setVisible(false);
            
            paused = false;
            
            setPaused(false);
            
        });
        
        startGameButton.opacityProperty().addListener((observable, oldOpacity, newOpacity) -> {

            chessBoard.getChessBoardWidget().setEffect(new GaussianBlur(newOpacity.doubleValue() * MAX_BLUR_RADIUS));
            
        });
        
        startGameButton.setOnAction(actionEvent -> {
            
            if (fadeTransition.getStatus() != Animation.Status.RUNNING) {

                fadeTransition.play();
                
            }
            
        });
        
        initializeChessBoard();
        
    }
    
    private void keyPress(final KeyCode keyCode) {
        
        if (keyCode == KeyCode.E) {

            if (!startGameButton.isVisible()) {

                paused = !paused;

                setPaused(paused);
                
            }
            
        }
        
    }
    
    private static final double MAX_BLUR_RADIUS = 10.0;
    
    private static final Effect PAUSED_EFFECT = new GaussianBlur(MAX_BLUR_RADIUS);
    
    private void setPaused(final boolean paused) {

        final GridPane chessBoardWidget = chessBoard.getChessBoardWidget();
        
        if (paused) {
            
            chessBoardWidget.setEffect(PAUSED_EFFECT);
            
        } else {

            chessBoardWidget.setEffect(null);
            
        }
        
    }
    
    private void initializeChessBoard() {
        
        final ChessPlayer chessPlayerOne = chessBoard.getChessPlayerOne();
        
        final ChessPlayer chessPlayerTwo = chessBoard.getChessPlayerTwo();
        
        // Player 1 pieces:
        
        // Pawns:
        
        chessPlayerOne
                .addChessPiece(ChessPiece.Type.PAWN)
                .setChessTile(chessBoard.getChessTile(0, 6));
        chessPlayerOne
                .addChessPiece(ChessPiece.Type.PAWN)
                .setChessTile(chessBoard.getChessTile(1, 6));
        chessPlayerOne
                .addChessPiece(ChessPiece.Type.PAWN)
                .setChessTile(chessBoard.getChessTile(2, 6));
        chessPlayerOne
                .addChessPiece(ChessPiece.Type.PAWN)
                .setChessTile(chessBoard.getChessTile(3, 6));
        chessPlayerOne
                .addChessPiece(ChessPiece.Type.PAWN)
                .setChessTile(chessBoard.getChessTile(4, 6));
        chessPlayerOne
                .addChessPiece(ChessPiece.Type.PAWN)
                .setChessTile(chessBoard.getChessTile(5, 6));
        chessPlayerOne
                .addChessPiece(ChessPiece.Type.PAWN)
                .setChessTile(chessBoard.getChessTile(6, 6));
        chessPlayerOne
                .addChessPiece(ChessPiece.Type.PAWN)
                .setChessTile(chessBoard.getChessTile(7, 6));

        // Pieces behind the pawns:

        chessPlayerOne
                .addChessPiece(ChessPiece.Type.ROOK)
                .setChessTile(chessBoard.getChessTile(0, 7));
        chessPlayerOne
                .addChessPiece(ChessPiece.Type.KNIGHT)
                .setChessTile(chessBoard.getChessTile(1, 7));
        chessPlayerOne
                .addChessPiece(ChessPiece.Type.BISHOP)
                .setChessTile(chessBoard.getChessTile(2, 7));
        chessPlayerOne
                .addChessPiece(ChessPiece.Type.QUEEN)
                .setChessTile(chessBoard.getChessTile(3, 7));
        chessPlayerOne
                .addChessPiece(ChessPiece.Type.KING)
                .setChessTile(chessBoard.getChessTile(4, 7));
        chessPlayerOne
                .addChessPiece(ChessPiece.Type.BISHOP)
                .setChessTile(chessBoard.getChessTile(5, 7));
        chessPlayerOne
                .addChessPiece(ChessPiece.Type.KNIGHT)
                .setChessTile(chessBoard.getChessTile(6, 7));
        chessPlayerOne
                .addChessPiece(ChessPiece.Type.ROOK)
                .setChessTile(chessBoard.getChessTile(7, 7));

        // Player 2 pieces:

        // Pawns:

        chessPlayerTwo
                .addChessPiece(ChessPiece.Type.PAWN)
                .setChessTile(chessBoard.getChessTile(0, 1));
        chessPlayerTwo
                .addChessPiece(ChessPiece.Type.PAWN)
                .setChessTile(chessBoard.getChessTile(1, 1));
        chessPlayerTwo
                .addChessPiece(ChessPiece.Type.PAWN)
                .setChessTile(chessBoard.getChessTile(2, 1));
        chessPlayerTwo
                .addChessPiece(ChessPiece.Type.PAWN)
                .setChessTile(chessBoard.getChessTile(3, 1));
        chessPlayerTwo
                .addChessPiece(ChessPiece.Type.PAWN)
                .setChessTile(chessBoard.getChessTile(4, 1));
        chessPlayerTwo
                .addChessPiece(ChessPiece.Type.PAWN)
                .setChessTile(chessBoard.getChessTile(5, 1));
        chessPlayerTwo
                .addChessPiece(ChessPiece.Type.PAWN)
                .setChessTile(chessBoard.getChessTile(6, 1));
        chessPlayerTwo
                .addChessPiece(ChessPiece.Type.PAWN)
                .setChessTile(chessBoard.getChessTile(7, 1));

        // Pieces behind the pawns:

        chessPlayerTwo
                .addChessPiece(ChessPiece.Type.ROOK)
                .setChessTile(chessBoard.getChessTile(0, 0));
        chessPlayerTwo
                .addChessPiece(ChessPiece.Type.KNIGHT)
                .setChessTile(chessBoard.getChessTile(1, 0));
        chessPlayerTwo
                .addChessPiece(ChessPiece.Type.BISHOP)
                .setChessTile(chessBoard.getChessTile(2, 0));
        chessPlayerTwo
                .addChessPiece(ChessPiece.Type.QUEEN)
                .setChessTile(chessBoard.getChessTile(3, 0));
        chessPlayerTwo
                .addChessPiece(ChessPiece.Type.KING)
                .setChessTile(chessBoard.getChessTile(4, 0));
        chessPlayerTwo
                .addChessPiece(ChessPiece.Type.BISHOP)
                .setChessTile(chessBoard.getChessTile(5, 0));
        chessPlayerTwo
                .addChessPiece(ChessPiece.Type.KNIGHT)
                .setChessTile(chessBoard.getChessTile(6, 0));
        chessPlayerTwo
                .addChessPiece(ChessPiece.Type.ROOK)
                .setChessTile(chessBoard.getChessTile(7, 0));

    }
    
}