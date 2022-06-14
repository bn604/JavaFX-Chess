package core;

import game.chess.ChessBoard;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;

public final class Main
        extends Application {

    public static void main(final String[] args) {
        
        launch(args);
        
    }
    
    @Override
    public void start(final Stage primaryStage)
            throws IOException {

        final var chessboard = new ChessBoard();
        
        final var scene = new Scene(new StackPane());
        
        final var fxmlLoader = new FXMLLoader(Main.class.getResource("chess.fxml"));
        
        fxmlLoader.setController(new Controller(scene, chessboard));
        
        final StackPane root = fxmlLoader.load();
        
        scene.setRoot(root);
        
        final GridPane chessboardWidget = chessboard.getNode();
        
        root.getChildren().add(0, chessboardWidget);
        
        scene.getStylesheets().add(Main.class.getResource("chess.css").toExternalForm());
        
        primaryStage.setScene(scene);
        
        final double widthPadding = 250.0;
        final double heightPadding = 100.0;
        
        final double width = 800.0;
        final double height = 600.0;
        
        chessboardWidget.setPrefSize(width, height);
        
        primaryStage.setWidth(width + widthPadding);
        primaryStage.setHeight(height + heightPadding);
        
        primaryStage.setMinWidth(width);
        primaryStage.setMinHeight(height + 30.0);
        
        primaryStage.centerOnScreen();
        
        primaryStage.show();
        
    }
    
}