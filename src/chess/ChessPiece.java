package chess;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

public final class ChessPiece {
    
    public enum Type {
        
        PAWN,
        
        ROOK,
        
        FLIP_ROOK,
        
        KNIGHT,
        
        BISHOP,
        
        QUEEN,
        
        KING;
        
        private final Image playerOneImage;
        
        private final Image playerTwoImage;
        
        Type() {
            
            final String name = name()
                    .toLowerCase()
                    .replaceFirst("_", "-");
            
            playerOneImage = new Image(ChessPiece.class.getResource("images/" + name + "-player-1.png").toExternalForm(), true);
            
            playerTwoImage = new Image(ChessPiece.class.getResource("images/" + name + "-player-2.png").toExternalForm(), true);
            
        }

        private Image getImage(final ChessPlayer chessPlayer) {

            return chessPlayer.isPlayerOne() ? playerOneImage : playerTwoImage;
        }
        
    }
    
    private final ChessPlayer chessPlayer;
    
    private final Type type;
    
    private final ImageView chessPieceWidget = new ImageView();
    
    private final ObjectProperty<ChessTile> chessTileProperty = new SimpleObjectProperty<>(null);
    
    public ChessPiece(final ChessPlayer chessPlayer, final Type type) {
        
        this.chessPlayer = chessPlayer;
        
        this.type = type;
        
        chessPieceWidget.setImage(type.getImage(chessPlayer));
        
        chessPieceWidget.visibleProperty().bind(chessTileProperty.isNotNull());
        
        chessTileProperty.addListener((observable, oldChessTile, newChessTile) -> {
            
            if (oldChessTile != null) {
                
                oldChessTile.setChessPiece(null);
                
            }

            if (newChessTile != null) {
                
                newChessTile.setChessPiece(this);
                
                final StackPane newChessTileWidget = newChessTile.getChessTileWidget();

                chessPieceWidget.fitWidthProperty().bind(newChessTileWidget.widthProperty());
                chessPieceWidget.fitHeightProperty().bind(newChessTileWidget.heightProperty());

            } else {

                chessPieceWidget.fitWidthProperty().unbind();
                chessPieceWidget.fitHeightProperty().unbind();

            }
            
        });
        
    }
    
    public ChessPlayer getChessPlayer() {

        return chessPlayer;
    }

    public Type getType() {

        return type;
    }

    public ImageView getChessPieceWidget() {

        return chessPieceWidget;
    }
    
    public ObjectProperty<ChessTile> chessTileProperty() {
        
        return chessTileProperty;
    }
    
    public ChessTile getChessTile() {
        
        return chessTileProperty.get();
    }
    
    public void setChessTile(final ChessTile chessTile) {
        
        chessTileProperty.set(chessTile);
        
    }
    
}