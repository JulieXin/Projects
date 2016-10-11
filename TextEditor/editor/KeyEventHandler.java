package editor;

import javafx.event.EventHandler;
import javafx.geometry.VPos;
import javafx.scene.Group;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.geometry.VPos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.shape.Rectangle;
import java.util.Stack;
import java.io.File;

public class KeyEventHandler implements EventHandler<KeyEvent> {
	private Group root; 
	private LinkedListD text;  
	private Cursor cursor; 
	public static Text displayedText = new Text(Configuration.DEFAULT_WINDOW_WIDTH, Configuration.DEFAULT_WINDOW_HEIGHT, "");
	public static int cursorWidth;

	KeyEventHandler(final Group root, LinkedListD text, Cursor cursor) {
		this.root = root;
		this.text = text;
		this.cursor = cursor;
	}

	@Override
  	public void handle(KeyEvent keyEvent) {
		KeyCode code = keyEvent.getCode();
		if (keyEvent.getEventType() == KeyEvent.KEY_TYPED) {
            String characterTyped = keyEvent.getCharacter();
            
            if (characterTyped.length() > 0 && characterTyped.charAt(0) != 8 && !keyEvent.isShortcutDown()) {
    			displayedText = new Text(characterTyped);

				displayedText.setTextOrigin(VPos.TOP);
    			displayedText.setFont(Font.font(Configuration.fontName, Configuration.fontSize));
            	text.add(displayedText);
            	
            	text.render(Configuration.windowWidth, Configuration.windowHeight, Configuration.fontName, Configuration.fontSize);
				root.getChildren().add(text.getItem());
                keyEvent.consume();
                Cursor.renderCursor();
                Scroll.scrollRender(root);
                Configuration.textMemory.add(null);
                Configuration.cursorMemory.add(new int[] 
                    {(int) Math.round(Cursor.displayedCursor.getX()), (int) Math.round(Cursor.displayedCursor.getY())});

            }
        } else if (keyEvent.getEventType() == KeyEvent.KEY_PRESSED) {
            if (code == KeyCode.BACK_SPACE) {
            	if (text.size() != 0) {
            		Text deletedChar = text.delete();
            		root.getChildren().remove(deletedChar);
            		text.render(Configuration.windowWidth, Configuration.windowHeight, Configuration.fontName, Configuration.fontSize);
            		Cursor.renderCursor();
                    Configuration.textMemory.add(deletedChar);
                    Configuration.cursorMemory.add(new int[] 
                        {(int) Math.round(Cursor.displayedCursor.getX()), (int) Math.round(Cursor.displayedCursor.getY())});
                    Scroll.scrollRender(root);
	            }

            } else if (code == KeyCode.RIGHT) {
                text.moveRight();
                Cursor.renderCursor(); 
                Scroll.scrollRender(root);            
            } else if (code == KeyCode.LEFT) {
                text.moveLeft();
                Cursor.renderCursor();
                Scroll.scrollRender(root);
			} else if (code == KeyCode.UP) {
                text.moveUp();
                Cursor.renderCursor();   
                Scroll.scrollRender(root);          
            } else if (code == KeyCode.DOWN) {
                text.moveDown();
                Cursor.renderCursor();
                Scroll.scrollRender(root);
            } else if (code == KeyCode.P && keyEvent.isShortcutDown()) {
            	text.printCoordinate();
            } else if (code == KeyCode.S && keyEvent.isShortcutDown()) {
            	OpenAndSave.save(Configuration.fileName, text);
            } else if (code == KeyCode.Z && keyEvent.isShortcutDown()) {
            	if (Configuration.textMemory.size() > 0) {
                    Text temp = Configuration.textMemory.pop();
                    Configuration.undoRedo();
                    if (temp == null) { 
                        temp = text.delete();
                        root.getChildren().remove(temp);   
                    } else {                      
                        text.add(temp);
                        root.getChildren().add(temp);
                        temp = null;                        
                        }
                    text.render(Configuration.windowWidth, Configuration.windowHeight, Configuration.fontName, Configuration.fontSize);
                    Cursor.renderCursor();
                    Configuration.textMemorySec.add(temp);
                }
            } else if (code == KeyCode.Y && keyEvent.isShortcutDown()) {
            	if (Configuration.textMemorySec.size() > 0) {
                    Text temp = Configuration.textMemorySec.pop();
                    Configuration.undoRedo();
                    if (temp == null) { 
                        temp = text.delete();
                        root.getChildren().remove(temp);   
                    } else {                       
                        text.add(temp);
                        root.getChildren().add(temp);
                        temp = null;                        
                    }
                    text.render(Configuration.windowWidth, Configuration.windowHeight, Configuration.fontName, Configuration.fontSize);
                    Cursor.renderCursor();
                    Configuration.textMemorySec.add(temp);
                }
            // /* EXTRACREDIT: COPY/PASTE */ 	
            // } else if (code == KeyCode.C && keyEvent.isShortcutDown()) {
            // 	//copy
            // } else if (code == KeyCode.P && keyEvent.isShortcutDown()) {
            //	//paste

            } else if (code == KeyCode.MINUS && keyEvent.isShortcutDown()) { //FIX FONT CHANGING
            	Configuration.fontSize = Math.max(4, Configuration.fontSize - 4);
        		text.render(Configuration.windowWidth, Configuration.windowHeight, Configuration.fontName, Configuration.fontSize);
            	Cursor.renderCursor();
                Scroll.scrollRender(root);
            } else if ((code == KeyCode.PLUS || code == KeyCode.EQUALS) && keyEvent.isShortcutDown()) {
    			Configuration.fontSize += 4;
        		text.render(Configuration.windowWidth, Configuration.windowHeight, Configuration.fontName, Configuration.fontSize);
            	Cursor.renderCursor();
                Scroll.scrollRender(root);
        	}
        }
    }
}

