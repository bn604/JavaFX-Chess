package game.chess;

import game.GameBoard;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.event.EventType;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;

import java.util.function.BiConsumer;

public final class ChessBoard
        extends GameBoard<ChessPlayer, GridPane> {
    
    private static final int CHESS_PLAYER_ONE_INDEX = 0;
    
    private static final int CHESS_PLAYER_TWO_INDEX = 1;
    
    public static final int CHESS_BOARD_LENGTH = 8;
    
    private final ChessTile[][] chessTiles = new ChessTile[CHESS_BOARD_LENGTH][CHESS_BOARD_LENGTH];

    private final ReadOnlyObjectWrapper<ChessTile> selectedChessTileProperty = new ReadOnlyObjectWrapper<>();
    
    public ChessBoard() {
        
        super(new GridPane());
        
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
        
        chessBoardPane.addEventFilter(EventType.ROOT, event -> {
            
            if (pausedProperty.get()) {
                
                event.consume();
                
            }
            
        });
        
        // Board pieces setup:
        
        record PlayerTypePair(ChessPlayer chessPlayer, ChessPiece.Type chessPieceType) { }
        
        record Coordinate(int x, int y) { }
        
        final BiConsumer<PlayerTypePair, Coordinate> setupConsumer = (playerTypePair, coordinate) -> {
            
            final ChessPlayer chessPlayer = playerTypePair.chessPlayer;
            
            final var chessPiece = new ChessPiece(ChessBoard.this, chessPlayer, playerTypePair.chessPieceType);
            
            chessTiles[coordinate.x][coordinate.y].setChessPiece(chessPiece);
            
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
    
    public ChessPlayer getChessPlayerOne() {
        
        return gamePlayers.get(CHESS_PLAYER_ONE_INDEX);
    }

    public ChessPlayer getChessPlayerTwo() {

        return gamePlayers.get(CHESS_PLAYER_TWO_INDEX);
    }
    
    public ChessTile getChessTile(final int x, final int y) {
        
        return chessTiles[x][y];
    }
    
    public ReadOnlyObjectProperty<ChessTile> selectedChessTileProperty() {
        
        return selectedChessTileProperty.getReadOnlyProperty();
    }
    
    public ChessTile getSelectedChessTile() {
        
        return selectedChessTileProperty.get();
    }
    
    void setSelectedChessTile(final ChessTile chessTile) {
        
        final ChessTile newSelectedChessTile = handleSelection(selectedChessTileProperty.get(), chessTile);
        
        selectedChessTileProperty.set(newSelectedChessTile);
        
    }
    
    private ChessTile handleSelection(final ChessTile oldSelectedChessTile, final ChessTile newSelectedChessTile) {
        
        return newSelectedChessTile;
    }
    
}