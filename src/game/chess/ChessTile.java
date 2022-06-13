package game.chess;

import game.GameBoardWidget;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

public final class ChessTile
        extends GameBoardWidget<ChessBoard, StackPane> {
    
    private final int x;
    
    private final int y;
    
    private final ObjectProperty<ChessPiece> chessPieceProperty = new SimpleObjectProperty<>();
    
    private final InnerShadow innerShadow;
    
    public ChessTile(final ChessBoard chessBoard, final int x, final int y) {
        
        super(chessBoard, new StackPane());

        this.x = x;

        this.y = y;
        
        innerShadow = new InnerShadow(25.0, Color.WHITE);
        
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

        chessTilePane.getStyleClass().add(((x + y) % 2 == 0) ? "white-chess-tile" : "black-chess-tile");

        chessTilePane.hoverProperty().addListener((observable, wasHover, nowHover) -> {
            
            if (nowHover) {
                
                getEffectTypes().add(innerShadow);
                
            } else {
                
                getEffectTypes().remove(innerShadow);
                
            }
            
        });
        
        chessTilePane.addEventFilter(MouseEvent.MOUSE_PRESSED, mouseEvent -> chessBoard.setSelectedChessTile(this));
        
    }

    public int getX() {

        return x;
    }

    public int getY() {

        return y;
    }
    
    public ObjectProperty<ChessPiece> chessPieceProperty() {
        
        return chessPieceProperty;
    }
    
    public ChessPiece getChessPiece() {
        
        return chessPieceProperty.get();
    }
    
    public void setChessPiece(final ChessPiece chessPiece) {
        
        chessPieceProperty.set(chessPiece);
        
    }
    
    ObjectProperty<Color> innerShadowColorProperty() {
        
        return innerShadow.getFXEffect().colorProperty();
    }
    
}