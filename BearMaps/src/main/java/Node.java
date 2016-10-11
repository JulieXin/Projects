import java.util.ArrayList;
import java.util.List;

/**
 * Created by Julie on 4/4/16.
 */
public class Node {
    private double lon, lat;
    private long id;
    private List<Connection> connectionList;

    public Node(double lon, double lat, long id) {
        this.lon = lon;
        this.lat = lat;
        this.id = id;
        this.connectionList = new ArrayList<>();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double getLon() {
        return lon;
    }

    public double getLat() {
        return lat;
    }

    public List<Connection> getConnectionList() {
        return connectionList;
    }

    public void addConnection(Connection connection) {
        connectionList.add(connection);
    }


}

