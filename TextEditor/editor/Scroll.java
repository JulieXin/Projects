package editor;


import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Orientation;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.Scene;
import javafx.scene.control.ScrollBar;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Scroll {
	private static ScrollBar scrollBar;


    public Scroll(final Group root) {
        
        scrollBar = new ScrollBar();
        scrollBar.setMin(0);
        scrollBar.setMax(0);
        scrollBar.setPrefHeight(Configuration.windowHeight);
        scrollBar.setLayoutX(Configuration.windowWidth - scrollBar.getWidth());
        scrollBar.setOrientation(Orientation.VERTICAL);
        double windowSpace = Configuration.windowWidth - scrollBar.getLayoutBounds().getWidth();
        scrollBar.setLayoutX(windowSpace);
        root.getChildren().add(scrollBar);

        scrollBar.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
            	root.setLayoutY(-(Configuration.fontSize) * (int) Math.round(newValue.doubleValue()));
            }
        });
    }

    public static void scrollRender(Group root) {
        double cursorY = Cursor.displayedCursor.getY();


        double lowerBound = root.getLayoutY();
        double upperBound = lowerBound - Configuration.windowWidth + Configuration.fontSize;

        if (cursorY < lowerBound * -1) {
            scrollBar.setValue(cursorY);
        }
        else if (cursorY > upperBound * -1) {
            scrollBar.setValue(cursorY - Configuration.windowWidth + Configuration.fontSize);
        }
    }
}
