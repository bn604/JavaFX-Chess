package game.chess;

import game.GameBoardWidget;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;

public final class ChessTile
        extends GameBoardWidget<ChessBoard, StackPane> {
    
    private final int x;
    
    private final int y;
    
    private final ReadOnlyObjectWrapper<ChessPiece> chessPieceProperty = new ReadOnlyObjectWrapper<>();
    
    public ChessTile(final ChessBoard chessBoard, final int x, final int y) {
        
        super(chessBoard, new StackPane());

        this.x = x;

        this.y = y;
        
        final StackPane chessTilePane = getNode();
        
        chessTilePane.setMinSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
        chessTilePane.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
        
        chessPieceProperty.addListener((observable, oldChessPiece, newChessPiece) -> {
            
            if (oldChessPiece != null) {
                
                final ImageView imageView = oldChessPiece.getNode();
                
                imageView.fitWidthProperty().unbind();
                imageView.fitHeightProperty().unbind();
                
                chessTilePane.getChildren().remove(imageView);
                
            }

            if (newChessPiece != null) {

                final ImageView imageView = newChessPiece.getNode();

                imageView.fitWidthProperty().bind(chessTilePane.widthProperty());
                imageView.fitHeightProperty().bind(chessTilePane.heightProperty());

                chessTilePane.getChildren().add(imageView);

            }
            
        });
        
        chessTilePane.hoverProperty().addListener((observable, wasHover, nowHover) -> {
            
            if (nowHover) {
                
                chessBoard.setHoveringChessTile(this);
                
            }
            
        });
        
        chessTilePane.addEventFilter(MouseEvent.MOUSE_PRESSED, mouseEvent -> chessBoard.setSelectedChessTile(this));

        chessTilePane.getStyleClass().add(((x + y) % 2 == 0) ? "white-chess-tile" : "black-chess-tile");
        
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