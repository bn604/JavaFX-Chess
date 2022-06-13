package game;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public abstract class GamePiece<G extends GameBoard<?, ?>, T extends GamePlayer<?>>
        extends GameBoardWidget<G, ImageView> {
    
    private final T gamePlayer;
    
    protected GamePiece(final G gameBoard, final T gamePlayer, final Image image) {
        
        super(gameBoard, new ImageView(image));
        
        this.gamePlayer = gamePlayer;
        
    }

    public final T getGamePlayer() {

        return gamePlayer;
    }
    
}