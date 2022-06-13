package game.chess;

import game.GamePiece;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.scene.image.Image;

public final class ChessPiece
        extends GamePiece<ChessBoard, ChessPlayer> {
    
    public enum Type {

        PAWN {
            
            @Override
            protected boolean checkMove(final ChessBoard chessBoard,
                                        final int sourceX, final int sourceY,
                                        final int destinationX, final int destinationY,
                                        final boolean playerOne) {
                
                return false;
            }
            
        },

        ROOK {

            @Override
            protected boolean checkMove(final ChessBoard chessBoard,
                                        final int sourceX, final int sourceY,
                                        final int destinationX, final int destinationY,
                                        final boolean playerOne) {

                return false;
            }

        },

        FLIP_ROOK {

            @Override
            protected boolean checkMove(final ChessBoard chessBoard,
                                        final int sourceX, final int sourceY,
                                        final int destinationX, final int destinationY,
                                        final boolean playerOne) {

                return ROOK.checkMove(chessBoard, sourceX, sourceY, destinationX, destinationY, playerOne);
            }

        },

        KNIGHT {

            @Override
            protected boolean checkMove(final ChessBoard chessBoard,
                                        final int sourceX, final int sourceY,
                                        final int destinationX, final int destinationY,
                                        final boolean playerOne) {

                return false;
            }

        },

        BISHOP {

            @Override
            protected boolean checkMove(final ChessBoard chessBoard,
                                        final int sourceX, final int sourceY,
                                        final int destinationX, final int destinationY,
                                        final boolean playerOne) {

                return false;
            }

        },

        QUEEN {

            @Override
            protected boolean checkMove(final ChessBoard chessBoard,
                                        final int sourceX, final int sourceY,
                                        final int destinationX, final int destinationY,
                                        final boolean playerOne) {

                return false;
            }

        },

        KING {

            @Override
            protected boolean checkMove(final ChessBoard chessBoard,
                                        final int sourceX, final int sourceY,
                                        final int destinationX, final int destinationY,
                                        final boolean playerOne) {

                return false;
            }

        };

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

        protected abstract boolean checkMove(final ChessBoard chessBoard,
                                             final int sourceX, final int sourceY,
                                             final int destinationX, final int destinationY,
                                             final boolean playerOne);
        
    }
    
    private final Type type;
    
    private final ReadOnlyObjectWrapper<ChessTile> chessTileProperty = new ReadOnlyObjectWrapper<>();
    
    public ChessPiece(final ChessBoard chessBoard, final ChessPlayer chessPlayer, final Type type) {
        
        super(chessBoard, chessPlayer, type.getImage(chessPlayer.isPlayerOne()));
        
        this.type = type;
        
        getNode().getStyleClass().add("chess-tile");
        
    }

    public Type getType() {

        return type;
    }
    
    public ReadOnlyObjectProperty<ChessTile> chessTileProperty() {
        
        return chessTileProperty.getReadOnlyProperty();
    }
    
    public ChessTile getChessTile() {
        
        return chessTileProperty.get();
    }
    
    void setChessTile(final ChessTile chessTile) {
        
        chessTileProperty.set(chessTile);
        
    }
    
    @Override
    protected void incrementMoveCount() {
        
        super.incrementMoveCount();
        
    }

    boolean checkMove(final int sourceX, final int sourceY,
                      final int destinationX, final int destinationY) {
        
        return type.checkMove(getGameBoard(), sourceX, sourceY, destinationX, destinationY, getGamePlayer().isPlayerOne());
    }
    
}