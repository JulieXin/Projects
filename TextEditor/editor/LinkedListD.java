package editor;


import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.Group;
import javafx.geometry.VPos;

public class LinkedListD {

	class Node {
		public Text item;
		public Node next;
		public Node prev;
		public Node(Node p, Text i, Node n) {
			prev = p;
			item = i;
			next = n;
		}
	}

	private static int size;
	private static Node sentinel;
	private static Node cursorNode;

	public LinkedListD() {
		size = 0;
		sentinel = new Node(null, null, null);
		sentinel.next = sentinel;
		sentinel.prev = sentinel;
		cursorNode = sentinel;
	}
	
	public void add(Text item) {
		size += 1;
		Node newNext = cursorNode.next;
		Node addedNode = new Node(cursorNode, item, newNext);
		cursorNode.next = addedNode;
		newNext.prev = addedNode;
		cursorNode = addedNode;
	}

	public Text delete() {
		Text deletedChar = null;
		if (cursorNode != sentinel) {
			deletedChar = getItem();

			cursorNode.prev.next = cursorNode.next;
			cursorNode.next.prev = cursorNode.prev;
			cursorNode = cursorNode.prev;
			size -= 1;
		} 
		return deletedChar;
	}

	public static Text getItem() {
		return cursorNode.item;
	}

	public static Text getNextItem() {
		return cursorNode.next.item;
	}

	public static int size() {
		return size;
	}

	public static void moveNext() {
		cursorNode = cursorNode.next;
	}


	public static void moveRight() {
		if (cursorNode != sentinel.prev) {		
			cursorNode = cursorNode.next;
			if (getItem().getText() == Configuration.STRING_ENTER) {
				cursorNode = cursorNode.next;
			
			}
		} else {
			cursorNode = sentinel.prev;
		}
	}

	public static void moveLeft() { 
		if (cursorNode != sentinel) {
			
			cursorNode = cursorNode.prev;
			if (getItem().getText() == Configuration.STRING_ENTER) {
				cursorNode = cursorNode.prev;
			}
		} else {
			cursorNode = sentinel;
		}
	}


	public void moveUp() {
		int cursorX = (int) Cursor.displayedCursor.getX();
		int cursorY = (int) Cursor.displayedCursor.getY();
		if (getItem().getY() > 0) {
			int height;
			if (getItem().getText().equals(Configuration.ENTER)) {
				height = (int) Math.round(getItem().getLayoutBounds().getHeight()) / 2;
			} else {
				height = (int) Math.round(getItem().getLayoutBounds().getHeight());
			}
			moveArrow(cursorX, cursorY - height);
		} else {
			moveArrow(cursorX, cursorY);
		}
	}

	public void moveDown() {
		
		int cursorX = (int) Cursor.displayedCursor.getX(); 
		int cursorY = (int) Cursor.displayedCursor.getY();
		if (getItem().getY() < sentinel.prev.item.getY()) {
			moveArrow(cursorX, cursorY + (int) Math.round(getItem().getLayoutBounds().getHeight()/2));
		} else {
			moveArrow(cursorX, cursorY);
		}
	}
	public static void moveArrow(int mouseX, int mouseY) {
		Text lastText = sentinel.prev.item;
		double mostBottom = lastText.getY() + lastText.getLayoutBounds().getHeight();
		if ((mouseY > mostBottom) || (mouseX > lastText.getX() && lastText.getY() <= mouseY)) {
			cursorNode = sentinel.prev;
		} else {
			cursorNode = sentinel.next;
			if (mouseX < (Configuration.LEFT_MARGIN + getItem().getLayoutBounds().getWidth()/2) && mouseY <= getItem().getLayoutBounds().getHeight()/2) {
				cursorNode = sentinel;
			} else {
				cursorNode = sentinel.next;
				while (getItem().getY() < mouseY) {
					cursorNode = cursorNode.next;
				}
				double height = getItem().getY();
				while ((height == getNextItem().getY()) && 
					(!(getItem().getX() <= mouseX && mouseX < getItem().getX() + getItem().getLayoutBounds().getWidth()))
					&& (cursorNode.next != sentinel)) {
					cursorNode = cursorNode.next;
				} 
				if (mouseX - getItem().getX() < getItem().getX() + getItem().getLayoutBounds().getWidth() - mouseX) {
   					cursorNode = cursorNode.prev;
   				}
   			}
		}
	}

    public static void render(int windowWidth, int windowHeight, String fontName, int fontSize) {
    	if (size > 0) {
    		int cursorX = Configuration.LEFT_MARGIN;
    		int cursorY = Configuration.TOP_MARGIN;
    		Node tempNode = sentinel.next;
    		Text tempText = tempNode.item;
    		int findHeight = (int) Math.round(tempText.getLayoutBounds().getHeight());
    		renderHelper(cursorX, cursorY, tempText, Configuration.fontName, Configuration.fontSize);
    		while (tempNode.next != sentinel) {
    			cursorX += (int) Math.round(tempText.getLayoutBounds().getWidth());
    			tempNode = tempNode.next;
    			tempText = tempNode.item;
    			int tempWidth = (int) Math.round(tempText.getLayoutBounds().getWidth());
    			if (tempText.getText().equals(Configuration.STRING_ENTER)) {
    				cursorX = Configuration.LEFT_MARGIN;
    				cursorY += Configuration.fontSize;
    			}
    			if ((cursorX + tempWidth) > (windowWidth - Configuration.RIGHT_MARGIN)) {
    				Node storeNode = tempNode;
    				while (!(tempText.getText().equals(" ")) && ((int) Math.round(tempText.getX()) != Configuration.LEFT_MARGIN)) {
    					tempNode = tempNode.prev;
    					tempText = tempNode.item;
    				} 
    				if ((int) Math.round(tempText.getX()) == Configuration.LEFT_MARGIN) {
    					tempNode = storeNode.prev;
    				}
    				tempNode = tempNode.next;
    				tempText = tempNode.item;
    				cursorX = Configuration.LEFT_MARGIN;
    				cursorY += Configuration.fontSize;

    			}
    			renderHelper(cursorX, cursorY, tempText, Configuration.fontName, Configuration.fontSize);    			
    		} 
    	}
    }

    public static void renderHelper(int x, int y, Text text, String fontName, int fontSize) {
    	text.setX(x);
    	text.setY(y);
    	text.setTextOrigin(VPos.TOP);
    	text.setFont(Font.font(fontName, fontSize));
    }

	public static void printCoordinate() {
		Text current = getItem();
        int cursorX = Configuration.LEFT_MARGIN;
		int cursorY = Configuration.TOP_MARGIN;
        if (size > 0) {
        	cursorX = (int) Math.round(Cursor.displayedCursor.getX());
        	cursorY = (int) Math.round(Cursor.displayedCursor.getY());
        }
		System.out.println(cursorX + ", " + cursorY);
	}	

    public void pointAtStart() {
    	cursorNode = sentinel;
    }

    public static boolean isAtStart() {
    	return (cursorNode == sentinel);
    }
}
