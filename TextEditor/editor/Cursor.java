package editor;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.util.Duration;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;


public class Cursor {
    private static final double DELAY = 0.5;
    public static Rectangle displayedCursor;
    private static LinkedListD text;

    public Cursor(final Group root, LinkedListD text) {
        this.text = text;
        displayedCursor = new Rectangle(Configuration.LEFT_MARGIN, Configuration.TOP_MARGIN, 1, Configuration.fontSize);
        root.getChildren().add(displayedCursor);
        blink();
    }


    private class CursorBlinkEventHandler implements EventHandler<ActionEvent> {
        private int currentColorIndex = 0;
        private Color[] cursorColors = {Color.BLACK, Color.TRANSPARENT};

        CursorBlinkEventHandler() {
            changeColor();
        }

        private void changeColor() {
            displayedCursor.setFill(cursorColors[currentColorIndex]);
            currentColorIndex = (currentColorIndex + 1) % 2;
        }

        @Override
        public void handle(ActionEvent event) {
            changeColor();
        }       
    }

    public void blink() {
        final Timeline timeline = new Timeline();
        timeline.setCycleCount(Timeline.INDEFINITE);
        CursorBlinkEventHandler cursorChange = new CursorBlinkEventHandler();
        KeyFrame keyFrame = new KeyFrame(Duration.seconds(DELAY), cursorChange);
        timeline.getKeyFrames().add(keyFrame);
        timeline.play();
    }

    public static void renderCursor() {
        Text current = text.getItem();
        int textWidth;
        int textX;
        int textY;
        if (text.isAtStart()) {
            textWidth = 0;
            textX = Configuration.LEFT_MARGIN;
            textY = Configuration.TOP_MARGIN;
        } else {
            textWidth = (int) Math.round(current.getLayoutBounds().getWidth());
            textX = (int) Math.round(current.getX());
            textY = (int) Math.round(current.getY());
        }
        displayedCursor.setHeight(Configuration.fontSize);
        displayedCursor.setX(textX + textWidth);
        displayedCursor.setY(textY);
    }
}
