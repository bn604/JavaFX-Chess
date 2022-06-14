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

        PAWN {
            
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

        ROOK {

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

        FLIP_ROOK {

            @Override
            protected boolean checkMove(final ChessBoard chessBoard,
                                        final ChessTile sourceTile, final ChessTile destinationTile) {

                return ROOK.checkMove(chessBoard, sourceTile, destinationTile);
            }

        },

        KNIGHT {

            @Override
            protected boolean checkMove(final ChessBoard chessBoard,
                                        final ChessTile sourceTile, final ChessTile destinationTile) {
                
                return false;
            }

        },

        BISHOP {

            @Override
            protected boolean checkMove(final ChessBoard chessBoard,
                                        final ChessTile sourceTile, final ChessTile destinationTile) {

                return false;
            }

        },

        QUEEN {

            @Override
            protected boolean checkMove(final ChessBoard chessBoard,
                                        final ChessTile sourceTile, final ChessTile destinationTile) {
                
                return ROOK.checkMove(chessBoard, sourceTile, destinationTile)
                        || BISHOP.checkMove(chessBoard, sourceTile, destinationTile);
            }

        },

        KING {
            
            @Override
            protected boolean checkMove(final ChessBoard chessBoard,
                                        final ChessTile sourceTile, final ChessTile destinationTile) {
                
                return false;
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
                                             final ChessTile sourceTile, final ChessTile destinationTile);
        
    }
    
    private final Type type;
    
    private final ReadOnlyObjectWrapper<ChessTile> chessTileProperty = new ReadOnlyObjectWrapper<>();
    
    public ChessPiece(final ChessBoard chessBoard, final ChessPlayer chessPlayer, final Type type) {
        
        super(chessBoard, chessPlayer, type.getImage(chessPlayer.isPlayerOne()));
        
        this.type = type;
        
        final ImageView chessPieceNode = getNode();
        
        chessPieceNode.setViewOrder(-1);
        
        chessPieceNode.setCache(true);
        
        chessPieceNode.getStyleClass().add("chess-tile");
        
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
    
}