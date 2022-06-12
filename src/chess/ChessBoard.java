package chess;

import javafx.beans.property.DoubleProperty;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;

public final class ChessBoard {
    
    public static final int BOARD_LENGTH = 8;
    
    private final GridPane chessBoardWidget = new GridPane();
    
    private final ChessTile[][] chessTiles = new ChessTile[BOARD_LENGTH][BOARD_LENGTH];
    
    private final ChessPlayer chessPlayerOne = new ChessPlayer(true);
    
    private final ChessPlayer chessPlayerTwo = new ChessPlayer(false);
    
    public ChessBoard() {
        
        for (int x = 0; x < BOARD_LENGTH; x++) {

            for (int y = 0; y < BOARD_LENGTH; y++) {
                
                final var chessTile = new ChessTile(x, y, chessBoardWidget);

                chessTiles[x][y] = chessTile;
                
                chessBoardWidget.add(chessTile.getChessTileWidget(), x, y);
                
            }
            
        }
        
        chessBoardWidget.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
        chessBoardWidget.setMinSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
        
    }
    
    public GridPane getChessBoardWidget() {
        
        return chessBoardWidget;
    }

    public ChessTile getChessTile(final int x, final int y) {

        return chessTiles[x][y];
    }

    public ChessPlayer getChessPlayerOne() {

        return chessPlayerOne;
    }

    public ChessPlayer getChessPlayerTwo() {

        return chessPlayerTwo;
    }

    public DoubleProperty widthProperty() {
        
        return chessBoardWidget.prefWidthProperty();
    }

    public DoubleProperty heightProperty() {

        return chessBoardWidget.prefHeightProperty();
    }
    
}