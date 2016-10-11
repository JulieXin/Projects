package editor;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.VPos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class MouseClickHandler implements EventHandler<MouseEvent> {

	MouseClickHandler(Group root) { 

	}

	@Override
	public void handle(MouseEvent mouseEvent) {
		int mouseX = (int) Math.round(mouseEvent.getX());
		int mouseY = (int) Math.round(mouseEvent.getY()) - Configuration.fontSize;
		LinkedListD.moveArrow(mouseX, mouseY);
		Cursor.renderCursor();
	}
}