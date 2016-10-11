/**
 * Created by Julie on 4/4/16.
 */
public class Connection {
    private Node node;
    private String name;


    public Connection(Node node, String name) {
        this.node = node;
        this.name = name;
    }

    public Node getNode() {
        return node;
    }
}
