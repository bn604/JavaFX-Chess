package chess;

import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

public final class ChessTile {
    
    private static final Background WHITE_BACKGROUND = new Background(new BackgroundFill(Color.web("#D1CCBE"), null, null));
    
    private static final Background BLACK_BACKGROUND = new Background(new BackgroundFill(Color.web("#262520"), null, null));
    
    private final StackPane chessTileWidget = new StackPane();
    
    private final int x;
    
    private final int y;
    
    private final ReadOnlyObjectWrapper<ChessPiece> chessPieceProperty = new ReadOnlyObjectWrapper<>();
    
    public ChessTile(final int x, final int y, final GridPane chessBoardWidget) {
        
        this.x = x;
        
        this.y = y;

        chessTileWidget.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
        chessTileWidget.setMinSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
        
        chessTileWidget.prefWidthProperty().bind(chessBoardWidget.prefWidthProperty().divide(ChessBoard.BOARD_LENGTH));
        chessTileWidget.prefHeightProperty().bind(chessBoardWidget.prefHeightProperty().divide(ChessBoard.BOARD_LENGTH));
        
        if (x % 2 == ((y % 2 == 0) ? 0 : 1)) {

            chessTileWidget.setBackground(WHITE_BACKGROUND);
            
        } else {

            chessTileWidget.setBackground(BLACK_BACKGROUND);
            
        }
        
        chessPieceProperty.addListener((observable, oldChessPiece, newChessPiece) -> {

            if (oldChessPiece != null) {

                chessTileWidget.getChildren().remove(oldChessPiece.getChessPieceWidget());

            }
            
            if (newChessPiece != null) {
                
                chessTileWidget.getChildren().add(newChessPiece.getChessPieceWidget());
                
            }
            
        });
        
    }

    public StackPane getChessTileWidget() {

        return chessTileWidget;
    }

    public int getX() {

        return x;
    }

    public int getY() {

        return y;
    }
    
    public ReadOnlyObjectProperty<ChessPiece> chessPieceProperty() {
        
        return chessPieceProperty.getReadOnlyProperty();
    }
    
    public ChessPiece getChessPiece() {
        
        return chessPieceProperty.get();
    }
    
    void setChessPiece(final ChessPiece chessPiece) {
        
        chessPieceProperty.set(chessPiece);
        
    }
    
}