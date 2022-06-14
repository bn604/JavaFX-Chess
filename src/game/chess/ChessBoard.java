package game.chess;

import game.GameBoard;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import java.util.function.BiConsumer;

public final class ChessBoard
        extends GameBoard<ChessPlayer, GridPane> {
    
    private static final int CHESS_PLAYER_ONE_INDEX = 0;
    
    private static final int CHESS_PLAYER_TWO_INDEX = 1;
    
    public static final int CHESS_BOARD_LENGTH = 8;
    
    private final ChessTile[][] chessTiles = new ChessTile[CHESS_BOARD_LENGTH][CHESS_BOARD_LENGTH];
    
    private final ReadOnlyObjectWrapper<ChessTile> hoveringChessTileProperty = new ReadOnlyObjectWrapper<>();
    
    private final ReadOnlyObjectWrapper<ChessTile> selectedChessTileProperty = new ReadOnlyObjectWrapper<>();
    
    private final ObjectBinding<ChessPlayer> currentChessPlayerProperty;
    
    private final BooleanBinding validMoveProperty;
    
    private static final Color INVALID_MOVE_HOVER_COLOR = Color.CADETBLUE;
    
    private static final Color VALID_MOVE_HOVER_COLOR = Color.ORANGE;
    
    private final InnerShadow hoveringTileEffect = new InnerShadow(25.0, null);
    
    private final InnerShadow selectedTileEffect = new InnerShadow(35.0, Color.MEDIUMPURPLE);
    
    private boolean pieceAnimating = false;

    private final TranslateTransition pieceTransition = new TranslateTransition(Duration.millis(500));
    
    private static final double PAUSED_EFFECT_RADIUS = 5.0;
    
    private static final GaussianBlur PAUSED_EFFECT = new GaussianBlur(PAUSED_EFFECT_RADIUS);
    
    public ChessBoard() {
        
        super(new GridPane(), 0);
        
        final GridPane chessBoardPane = getNode();
        
        chessBoardPane.getStyleClass().add("chess-board");
        
        final var chessPlayerOne = new ChessPlayer(true);
        
        final var chessPlayerTwo = new ChessPlayer(false);
        
        gamePlayers.add(CHESS_PLAYER_ONE_INDEX, chessPlayerOne);
        
        gamePlayers.add(CHESS_PLAYER_TWO_INDEX, chessPlayerTwo);

        chessBoardPane.setMinSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
        chessBoardPane.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
        
        final DoubleBinding widthBinding = chessBoardPane.widthProperty().divide(CHESS_BOARD_LENGTH);
        final DoubleBinding heightBinding = chessBoardPane.heightProperty().divide(CHESS_BOARD_LENGTH);
        
        for (int x = 0; x < CHESS_BOARD_LENGTH; x++) {

            for (int y = 0; y < CHESS_BOARD_LENGTH; y++) {
                
                final var chessTile = new ChessTile(this, x, y);
                
                final StackPane chessTilePane = chessTile.getNode();
                
                chessTilePane.prefWidthProperty().bind(widthBinding);
                chessTilePane.prefHeightProperty().bind(heightBinding);
                
                chessBoardPane.add(chessTilePane, x, y);
                
                chessTiles[x][y] = chessTile;
                
            }
            
        }
        
        pausedProperty.addListener((observable, wasPaused, nowPaused) -> {

            if (nowPaused) {
                
                getEffectTypes().add(PAUSED_EFFECT);
                
            } else {
                
                getEffectTypes().remove(PAUSED_EFFECT);
                
            }
            
        });
        
        if (isPaused()) {
            
            getEffectTypes().add(PAUSED_EFFECT);
            
        }
        
        chessBoardPane.mouseTransparentProperty().bind(pausedProperty());
        
        chessBoardPane.hoverProperty().addListener((observable, wasHover, nowHover) -> {
            
            if (!nowHover) {
                
                hoveringChessTileProperty.set(null);
                
            }
            
        });
        
        hoveringChessTileProperty.addListener((observable, oldHoveringChessTile, newHoveringChessTile) -> {

            if (oldHoveringChessTile != null) {

                oldHoveringChessTile.getEffectTypes().remove(hoveringTileEffect);

            }

            if (newHoveringChessTile != null) {

                newHoveringChessTile.getEffectTypes().add(hoveringTileEffect);

            }

        });
        
        selectedChessTileProperty.addListener((observable, oldSelectedChessTile, newSelectedChessTile) -> {
            
            if (oldSelectedChessTile != null) {
                
                oldSelectedChessTile.getEffectTypes().remove(selectedTileEffect);
                
            }
            
            if (newSelectedChessTile != null) {
                
                newSelectedChessTile.getEffectTypes().add(0, selectedTileEffect);
                
            }
            
        });
        
        validMoveProperty = new BooleanBinding() {
            
            { super.bind(hoveringChessTileProperty, selectedChessTileProperty); }
            
            @Override
            protected boolean computeValue() {
                
                final ChessTile sourceChessTile = getSelectedChessTile();

                final ChessTile destinationChessTile = getHoveringChessTile();
                
                if ((sourceChessTile == null)
                        || (destinationChessTile == null)
                        || (sourceChessTile == destinationChessTile)) {
                    
                    return false;
                    
                }
                
                return sourceChessTile.getChessPiece().checkMove(sourceChessTile, destinationChessTile);
            }
            
        };
        
        chessBoardPane.cacheProperty().bind(pausedProperty());
        
        hoveringTileEffect.getFXEffect().setColor(validMoveProperty.get() ? VALID_MOVE_HOVER_COLOR : INVALID_MOVE_HOVER_COLOR);
        
        validMoveProperty.addListener((observable, wasValidMove, nowValidMove) -> hoveringTileEffect.getFXEffect().setColor(nowValidMove ? VALID_MOVE_HOVER_COLOR : INVALID_MOVE_HOVER_COLOR));

        currentChessPlayerProperty = new ObjectBinding<>() {

            { super.bind(turnCountProperty); }

            @Override
            protected ChessPlayer computeValue() {

                return (getTurnCount() % 2 == 0) ? getChessPlayerOne() : getChessPlayerTwo();
            }

        };
        
        // Board pieces setup:
        
        record PlayerTypePair(ChessPlayer chessPlayer, ChessPiece.Type chessPieceType) { }
        
        record Coordinate(int x, int y) { }
        
        final BiConsumer<PlayerTypePair, Coordinate> setupConsumer = (playerTypePair, coordinate) -> {
            
            final ChessPlayer chessPlayer = playerTypePair.chessPlayer;
            
            final var chessPiece = new ChessPiece(ChessBoard.this, chessPlayer, playerTypePair.chessPieceType);
            
            final ChessTile chessTile = chessTiles[coordinate.x][coordinate.y];
            
            chessTile.setChessPiece(chessPiece);
            
            chessPiece.setChessTile(chessTile);
            
            chessPlayer.getGamePieces().add(chessPiece);
            
        };
        
        // Player 1 Pawns:
        for (int i = 0; i < 8; i++) {

            setupConsumer.accept(new PlayerTypePair(chessPlayerOne, ChessPiece.Type.PAWN), new Coordinate(i, 6));
            
        }
        
        // Player 1 Rooks:
        setupConsumer.accept(new PlayerTypePair(chessPlayerOne, ChessPiece.Type.ROOK), new Coordinate(0, 7));
        setupConsumer.accept(new PlayerTypePair(chessPlayerOne, ChessPiece.Type.ROOK), new Coordinate(7, 7));
        
        // Player 1 Knights:
        setupConsumer.accept(new PlayerTypePair(chessPlayerOne, ChessPiece.Type.KNIGHT), new Coordinate(1, 7));
        setupConsumer.accept(new PlayerTypePair(chessPlayerOne, ChessPiece.Type.KNIGHT), new Coordinate(6, 7));

        // Player 1 Bishops:
        setupConsumer.accept(new PlayerTypePair(chessPlayerOne, ChessPiece.Type.BISHOP), new Coordinate(2, 7));
        setupConsumer.accept(new PlayerTypePair(chessPlayerOne, ChessPiece.Type.BISHOP), new Coordinate(5, 7));

        // Player 1 Queen:
        setupConsumer.accept(new PlayerTypePair(chessPlayerOne, ChessPiece.Type.QUEEN), new Coordinate(3, 7));

        // Player 1 King:
        setupConsumer.accept(new PlayerTypePair(chessPlayerOne, ChessPiece.Type.KING), new Coordinate(4, 7));
        
        // Player 2 Pawns:
        for (int i = 0; i < 8; i++) {

            setupConsumer.accept(new PlayerTypePair(chessPlayerTwo, ChessPiece.Type.PAWN), new Coordinate(i, 1));

        }

        // Player 2 Rooks:
        setupConsumer.accept(new PlayerTypePair(chessPlayerTwo, ChessPiece.Type.ROOK), new Coordinate(0, 0));
        setupConsumer.accept(new PlayerTypePair(chessPlayerTwo, ChessPiece.Type.ROOK), new Coordinate(7, 0));

        // Player 2 Knights:
        setupConsumer.accept(new PlayerTypePair(chessPlayerTwo, ChessPiece.Type.KNIGHT), new Coordinate(1, 0));
        setupConsumer.accept(new PlayerTypePair(chessPlayerTwo, ChessPiece.Type.KNIGHT), new Coordinate(6, 0));

        // Player 2 Bishops:
        setupConsumer.accept(new PlayerTypePair(chessPlayerTwo, ChessPiece.Type.BISHOP), new Coordinate(2, 0));
        setupConsumer.accept(new PlayerTypePair(chessPlayerTwo, ChessPiece.Type.BISHOP), new Coordinate(5, 0));

        // Player 2 Queen:
        setupConsumer.accept(new PlayerTypePair(chessPlayerTwo, ChessPiece.Type.QUEEN), new Coordinate(3, 0));

        // Player 2 King:
        setupConsumer.accept(new PlayerTypePair(chessPlayerTwo, ChessPiece.Type.KING), new Coordinate(4, 0));
        
    }
    
    public static final int START_ANIMATION_MILLISECONDS = 100;
    
    @Override
    public void startGame() {
        
        checkStarted();
        
        final var startGameAnimation = new Timeline(60.0, new KeyFrame(Duration.millis(START_ANIMATION_MILLISECONDS),
                new KeyValue(PAUSED_EFFECT.getFXEffect().radiusProperty(), 0.0)));
        
        startGameAnimation.setOnFinished(actionEvent -> {

            super.startGame();
            
            PAUSED_EFFECT.getFXEffect().setRadius(PAUSED_EFFECT_RADIUS);
            
        });
        
        startGameAnimation.playFromStart();
        
    }

    public ChessPlayer getChessPlayerOne() {
        
        return gamePlayers.get(CHESS_PLAYER_ONE_INDEX);
    }

    public ChessPlayer getChessPlayerTwo() {

        return gamePlayers.get(CHESS_PLAYER_TWO_INDEX);
    }
    
    public ChessTile getChessTile(final int x, final int y) {
        
        return chessTiles[x][y];
    }
    
    public ReadOnlyObjectProperty<ChessTile> hoveringChessTileProperty() {
        
        return hoveringChessTileProperty.getReadOnlyProperty();
    }
    
    public ChessTile getHoveringChessTile() {
        
        return hoveringChessTileProperty.get();
    }
    
    void setHoveringChessTile(final ChessTile hoveringChessTile) {
        
        hoveringChessTileProperty.set(hoveringChessTile);
        
    }
    
    public ReadOnlyObjectProperty<ChessTile> selectedChessTileProperty() {
        
        return selectedChessTileProperty.getReadOnlyProperty();
    }
    
    public ChessTile getSelectedChessTile() {
        
        return selectedChessTileProperty.get();
    }
    
    void setSelectedChessTile(final ChessTile chessTile) {
        
        if (!pieceAnimating) {

            final ChessTile newSelectedChessTile = handleSelection(selectedChessTileProperty.get(), chessTile);

            selectedChessTileProperty.set(newSelectedChessTile);
            
        }
        
    }
    
    public ObjectBinding<ChessPlayer> currentChessPlayerProperty() {
        
        return currentChessPlayerProperty;
    }
    
    public ChessPlayer getCurrentChessPlayer() {
        
        return currentChessPlayerProperty.get();
    }
    
    private ChessTile handleSelection(final ChessTile oldSelectedChessTile, final ChessTile newSelectedChessTile) {
        
        // newSelectedChessTile should always be non-null here
        
        final ChessPiece newTileChessPiece = newSelectedChessTile.getChessPiece();
        
        if (oldSelectedChessTile == newSelectedChessTile) {
            
            // Deselect selected chess tile
            
            return null;
            
        } else if (oldSelectedChessTile == null) {
            
            if ((newTileChessPiece != null)
                    && (newTileChessPiece.getGamePlayer() == getCurrentChessPlayer())) {

                // Selecting a piece that belongs to current player
                
                return newSelectedChessTile;
                
            } else {
                
                // Piece doesn't belong to current player
                
                return null;
                
            }
            
        }
        
        // oldSelectedChessTile.getChessPiece() should always be non-null here
        
        if ((newTileChessPiece != null)
                && (newSelectedChessTile.getChessPiece().getGamePlayer() == getCurrentChessPlayer())) {
            
            return newSelectedChessTile;
            
        } else if (validMoveProperty.get()) {
            
            pieceAnimating = true;
            
            final Point2D from = oldSelectedChessTile.getNode().localToParent(0.0, 0.0);
            
            pieceTransition.setFromX(from.getX());
            pieceTransition.setFromY(from.getY());

            final Point2D to = newSelectedChessTile.getNode().localToParent(0.0, 0.0);

            pieceTransition.setToX(to.getX());
            pieceTransition.setToY(to.getY());
            
            final ChessPlayer currentChessPlayer = getCurrentChessPlayer();

            final ChessPiece movingChessPiece = oldSelectedChessTile.getChessPiece();
            
            final Node movingChessPieceNode = movingChessPiece.getNode();
            
            pieceTransition.setNode(movingChessPieceNode);

            movingChessPieceNode.setViewOrder(-2);
            
            movingChessPieceNode.setManaged(false);
            
            getNode().getChildren().add(movingChessPieceNode);
            
            movingChessPiece.setChessTile(null);
            
            oldSelectedChessTile.setChessPiece(null);
            
            pieceTransition.setOnFinished(actionEvent -> {
                
                movingChessPieceNode.setViewOrder(-1);

                movingChessPieceNode.setManaged(true);
                
                movingChessPieceNode.setTranslateX(0.0);
                movingChessPieceNode.setTranslateY(0.0);
                
                if (newTileChessPiece != null) {

                    currentChessPlayer.getCapturedPieces().add(newTileChessPiece);

                    newTileChessPiece.setChessTile(null);

                }

                newSelectedChessTile.setChessPiece(movingChessPiece);

                movingChessPiece.setChessTile(newSelectedChessTile);

                movingChessPiece.incrementMoveCount();

                incrementTurnCount();

                pieceAnimating = false;
                
            });

            pieceTransition.playFromStart();
            
            return null;
            
        }
        
        return oldSelectedChessTile;
    }
    
}