package game.chess;

import game.GamePiece;
import javafx.scene.image.Image;

public final class ChessPiece
        extends GamePiece<ChessBoard, ChessPlayer> {
    
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

        private Image getImage(final boolean playerOne) {

            return playerOne ? playerOneImage : playerTwoImage;
        }

    }
    
    private final Type type;
    
    public ChessPiece(final ChessBoard chessBoard, final ChessPlayer chessPlayer, final Type type) {
        
        super(chessBoard, chessPlayer, type.getImage(chessPlayer.isPlayerOne()));
        
        this.type = type;
        
        getNode().getStyleClass().add("chess-tile");
        
    }

    public Type getType() {

        return type;
    }
    
}