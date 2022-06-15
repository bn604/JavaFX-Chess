package game.chess;

import game.GamePiece;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.function.Predicate;

public final class ChessPiece
        extends GamePiece<ChessBoard, ChessPlayer> {
    
    public enum Type {

        PAWN("Pawn") {
            
            @Override
            protected boolean checkMove(final ChessBoard chessBoard,
                                        final ChessTile sourceTile, final ChessTile destinationTile) {
                
                final int yDelta = sourceTile
                        .getChessPiece()
                        .getGamePlayer()
                        .isPlayerOne() ? 1 : -1;
                
                if (sourceTile.getX() == destinationTile.getX()) {

                    // Pawns can move one space forward, or if
                    // it's the piece's first move, Pawns can move two spaces forward

                    return !piecePresent(destinationTile)
                            && ((sourceTile.getY() == (destinationTile.getY() + yDelta))
                            || ((sourceTile.getChessPiece().getMoveCount() == 0)
                            && (sourceTile.getY() == (destinationTile.getY() + (2 * yDelta)))
                            && (!piecePresent(chessBoard, sourceTile.getX(), sourceTile.getY() - yDelta))));
                    
                } else if ((sourceTile.getX() == destinationTile.getX() + 1)
                        || (sourceTile.getX() == destinationTile.getX() - 1)) {

                    return ((destinationTile.getChessPiece() != null)
                            && (sourceTile.getY() == (destinationTile.getY() + yDelta)));
                    
                }
                
                return false;
            }

        },

        ROOK("Rook") {

            @Override
            protected boolean checkMove(final ChessBoard chessBoard,
                                        final ChessTile sourceTile, final ChessTile destinationTile) {
                
                @FunctionalInterface
                interface QuadFunction {
                    
                    boolean check(int coordinate1, int coordinate2, int invariant, boolean xInvariant);
                    
                }
                
                final QuadFunction pathFinder = (coordinate1, coordinate2, invariant, xInvariant) -> {

                    final int min;
                    final int max;
                    
                    if (coordinate1 < coordinate2) {

                        min = coordinate1;

                        max = coordinate2;

                    } else {

                        min = coordinate2;

                        max = coordinate1;

                    }
                    
                    final Predicate<Integer> piecePresent = xInvariant
                            ? coordinate -> piecePresent(chessBoard, invariant, coordinate)
                            : coordinate -> piecePresent(chessBoard, coordinate, invariant);

                    for (int i = (min + 1); i < max; i++) {

                        if (piecePresent.test(i)) {

                            return false;

                        }

                    }
                    
                    return true;
                };
                
                if (sourceTile.getX() == destinationTile.getX()) {

                    return pathFinder.check(sourceTile.getY(), destinationTile.getY(), sourceTile.getX(), true);
                    
                } else if (sourceTile.getY() == destinationTile.getY()) {

                    return pathFinder.check(sourceTile.getX(), destinationTile.getX(), sourceTile.getY(), false);
                    
                }
                
                return false;
            }

        },

        FLIP_ROOK("Flip Rook") {

            @Override
            protected boolean checkMove(final ChessBoard chessBoard,
                                        final ChessTile sourceTile, final ChessTile destinationTile) {
                
                // TODO
                
                return false;
            }

        },

        KNIGHT("Knight") {

            @Override
            protected boolean checkMove(final ChessBoard chessBoard,
                                        final ChessTile sourceTile, final ChessTile destinationTile) {
                
                final int srcX = sourceTile.getX();
                final int srcY = sourceTile.getY();

                final int destX = destinationTile.getX();
                final int destY = destinationTile.getY();
                
                if ((destX == (srcX + 1)) || (destX == (srcX - 1))) {

                    return (destY == (srcY + 2))
                            || (destY == (srcY - 2));
                    
                } else if ((destY == (srcY + 1)) || (destY == (srcY - 1))) {

                    return (destX == (srcX + 2))
                            || (destX == (srcX - 2));
                    
                }
                
                return false;
            }

        },

        BISHOP("Bishop") {

            @Override
            protected boolean checkMove(final ChessBoard chessBoard,
                                        final ChessTile sourceTile, final ChessTile destinationTile) {
                
                int currentX = sourceTile.getX();
                int currentY = sourceTile.getY();
                
                final int destinationX = destinationTile.getX();
                final int destinationY = destinationTile.getY();

                final int xDistance = Math.abs(destinationX - currentX);
                final int yDistance = Math.abs(destinationY - currentY);
                
                if (xDistance != yDistance) {
                    
                    return false;
                    
                }
                
                boolean done = false;
                
                do {

                    if (currentX < destinationX) {

                        currentX++;

                    } else {

                        currentX--;

                    }
                    
                    if (currentY < destinationY) {

                        currentY++;

                    } else {

                        currentY--;

                    }
                    
                    if ((currentX == destinationX)
                            || (currentY == destinationY)) {

                        done = true;
                        
                    }
                    
                    if (!done && piecePresent(chessBoard, currentX, currentY)) {

                        return false;

                    }

                } while (!done);
                
                return true;
            }

        },

        QUEEN("Queen") {
            
            @Override
            protected boolean checkMove(final ChessBoard chessBoard,
                                        final ChessTile sourceTile, final ChessTile destinationTile) {
                
                return ROOK.checkMove(chessBoard, sourceTile, destinationTile)
                        || BISHOP.checkMove(chessBoard, sourceTile, destinationTile);
            }

        },

        KING("King") {
            
            @Override
            protected boolean checkMove(final ChessBoard chessBoard,
                                        final ChessTile sourceTile, final ChessTile destinationTile) {

                final int srcX = sourceTile.getX();
                final int srcY = sourceTile.getY();

                final int destX = destinationTile.getX();
                final int destY = destinationTile.getY();

                return ((destX == (srcX - 1))
                        || (destX == (srcX + 1))
                        || (destX == srcX))
                        && (Math.abs(destY - srcY) <= 1);
            }
            
        };

        private static boolean piecePresent(final ChessBoard chessBoard, final int x, final int y) {

            return (x >= 0)
                    && (x < 8)
                    && (y >= 0)
                    && (y < 8)
                    && (chessBoard.getChessTile(x, y).getChessPiece() != null);
        }
        
        private static boolean piecePresent(final ChessTile chessTile) {
            
            return (chessTile.getChessPiece() != null);
        }
        
        private final Image playerOneImage;

        private final Image playerTwoImage;
        
        private final String displayName;
        
        Type(final String displayName) {

            final String name = name()
                    .toLowerCase()
                    .replaceFirst("_", "-");

            playerOneImage = new Image(ChessPiece.class.getResource("images/" + name + "-player-1.png").toExternalForm(), true);

            playerTwoImage = new Image(ChessPiece.class.getResource("images/" + name + "-player-2.png").toExternalForm(), true);
            
            this.displayName = displayName;
            
        }

        private Image getImage(final boolean playerOne) {

            return playerOne ? playerOneImage : playerTwoImage;
        }

        public final String getDisplayName() {

            return displayName;
        }

        protected abstract boolean checkMove(final ChessBoard chessBoard,
                                             final ChessTile sourceTile, final ChessTile destinationTile);
        
        @Override
        public String toString() {
            
            return displayName;
        }
        
    }
    
    private final Type type;
    
    private final ReadOnlyObjectWrapper<ChessTile> chessTileProperty = new ReadOnlyObjectWrapper<>();
    
    public ChessPiece(final ChessBoard chessBoard, final ChessPlayer chessPlayer, final Type type) {
        
        super(chessBoard, chessPlayer, type.getImage(chessPlayer.isPlayerOne()));
        
        this.type = type;
        
        final ImageView chessPieceNode = getNode();
        
        chessPieceNode.setViewOrder(-1);
        
        chessPieceNode.getStyleClass().add("chess-piece");
        
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

    boolean checkMove(final ChessTile sourceTile, final ChessTile destinationTile) {
        
        final ChessPiece destinationChessPiece = destinationTile.getChessPiece();
        
        if ((destinationChessPiece != null) && destinationChessPiece.gamePlayer == getGamePlayer()) {
            
            return false;
            
        }
        
        return type.checkMove(getGameBoard(), sourceTile, destinationTile);
    }

    @Override
    public String toString() {
        
        return String.format("Player %d %s", gamePlayer.isPlayerOne() ? 1 : 2, type);
    }
    
}