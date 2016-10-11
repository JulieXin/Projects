package editor;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import java.util.Stack;
import java.io.File;

public class Editor extends Application {
    private static final String DEBUG = "debug";
    public static Group root = new Group();
    private LinkedListD text = new LinkedListD();
    private Cursor cursor = new Cursor(root, text);
    private final Rectangle textBox = new Rectangle(0, 0);
    private Scroll scroll;

    @Override
    public void start(Stage primaryStage) {
        
        Scene scene = new Scene(root, Configuration.DEFAULT_WINDOW_WIDTH, Configuration.DEFAULT_WINDOW_HEIGHT, Color.WHITE);
        KeyEventHandler keyEventHandler = new KeyEventHandler(root, text, cursor);
        Configuration.handleWindowSize(scene);
        MouseClickHandler mouseClickHandler = new MouseClickHandler(root);
        Configuration.cursorMemory = new Stack<int[]>();
        Configuration.cursorMemorySec = new Stack<int[]>();
        Configuration.textMemory = new Stack<Text>();
        Configuration.textMemorySec = new Stack<Text>();

        scene.setOnKeyTyped(keyEventHandler);
        scene.setOnKeyPressed(keyEventHandler);
        scene.setOnMouseClicked(mouseClickHandler);
        root.getChildren().add(textBox);
        Scroll scroll = new Scroll(root);
        primaryStage.setTitle("Text Editor");
        primaryStage.setScene(scene);
        primaryStage.show();
        OpenAndSave.open(root, Configuration.fileName, text);
        text.render(Configuration.windowWidth, Configuration.windowHeight, Configuration.fontName, Configuration.fontSize);    
    }

    public static void main(String[] args) {
    	if (args.length < 1) {
            System.out.println("Expected usage: File <source filename>");
            System.exit(1);
        }
        
        Configuration.setFile(new File(args[0]));
        if (args.length > 1) {
            if (args[1].equals("debug")) {
                System.out.println("Facilitating Debugging");
            }
        }
        launch(args);
    }
}