package game;

import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.ReadOnlyIntegerWrapper;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public abstract class GamePiece<G extends GameBoard<?, ?>, T extends GamePlayer<?>>
        extends GameBoardWidget<G, ImageView> {
    
    protected final T gamePlayer;
    
    protected final ReadOnlyIntegerWrapper moveCountProperty = new ReadOnlyIntegerWrapper(0);
    
    protected GamePiece(final G gameBoard, final T gamePlayer, final Image image) {
        
        super(gameBoard, new ImageView(image));
        
        this.gamePlayer = gamePlayer;
        
    }

    public final T getGamePlayer() {

        return gamePlayer;
    }

    public final ReadOnlyIntegerProperty moveCountProperty() {

        return moveCountProperty.getReadOnlyProperty();
    }
    
    public final int getMoveCount() {
        
        return moveCountProperty.get();
    }
    
    protected void incrementMoveCount() {
        
        moveCountProperty.set(moveCountProperty.get() + 1);
        
    }
    
}