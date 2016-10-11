import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

/**
 * Created by Julie on 4/4/16.
 */
public class QuadTree {
    private QuadTreeNode root;

    public class QuadTreeNode {
        private double ullon, ullat, lrlon, lrlat;
        private int imageId;
        private QuadTreeNode nw, ne, sw, se;
        private int depth;

        public QuadTreeNode(double ullon, double ullat, double lrlon, double lrlat, int imageId) {
            this.ullon = ullon;
            this.ullat = ullat;
            this.lrlon = lrlon;
            this.lrlat = lrlat;
            this.imageId = imageId;

            if (imageId == 0) {
                this.depth = 0;
            } else {
                this.depth = (int) Math.log10(imageId) + 1;
            }
        }

        public double getUllon() {
            return ullon;
        }

        public double getUllat() {
            return ullat;
        }

        public double getLrlon() {
            return lrlon;
        }

        public double getLrlat() {
            return lrlat;
        }

        public int getDepth() {
            return depth;
        }

        public int getImageId() {
            return imageId;
        }
    }


    public QuadTree(double ullon, double ullat, double lrlon, double lrlat) {
        root = new QuadTreeNode(ullon, ullat, lrlon, lrlat, 0);
        buildTree(root);
    }

    /* Recursively constructs the QuadTree to contain all the four children nodes
    *  Stops when there are no more images that have children (before one million aka 10^6) */
    public void buildTree(QuadTreeNode node) {
        if (node.imageId < Math.pow(10, 6)) {
            double centerlon = (node.ullon + node.lrlon) / 2;
            double centerlat = (node.ullat + node.lrlat) / 2;

            node.nw = new QuadTreeNode(node.ullon, node.ullat, centerlon, centerlat,
                    node.imageId * 10 + 1);
            node.ne = new QuadTreeNode(centerlon, node.ullat, node.lrlon, centerlat,
                    node.imageId * 10 + 2);
            node.sw = new QuadTreeNode(node.ullon, centerlat, centerlon, node.lrlat,
                    node.imageId * 10 + 3);
            node.se = new QuadTreeNode(centerlon, centerlat, node.lrlon, node.lrlat,
                    node.imageId * 10 + 4);

            buildTree(node.nw);
            buildTree(node.ne);
            buildTree(node.sw);
            buildTree(node.se);
        }
    }

    public QuadTreeNode getRoot() {
        return root;
    }


    /* Collects an arraylist of QuadTreeNodes that intersect with the query window */
    public ArrayList<QuadTreeNode> traverse(Rectangle2D queryWindow, QuadTreeNode node, int level) {
        double queryULLON = queryWindow.getX();
        double queryULLAT = queryWindow.getY();
        double queryLRLON = queryWindow.getX() + queryWindow.getWidth();
        double queryLRLAT = queryWindow.getY() - queryWindow.getHeight();
        if (queryULLON < root.getUllon()) {
            queryULLON = root.getUllon();
        }
        if (queryULLAT > root.getUllat()) {
            queryULLAT = root.getUllat();
        }
        if (queryLRLON > root.getLrlon()) {
            queryLRLON = root.getUllon();
        }
        if (queryLRLAT < root.getLrlat()) {
            queryLRLAT = root.getLrlat();
        }

        ArrayList<QuadTreeNode> imageNodes = new ArrayList<>();

        /* finding ulNode to base the ArrayList off of */
        QuadTreeNode starter = findNode(node, queryULLON, queryULLAT, level);

        double nodeWidth = starter.getLrlat() - starter.getUllon();
        double nodeHeight = starter.getUllat() - starter.getLrlat();


        for (double j = starter.getUllat(); queryLRLAT < j; j -= nodeHeight) {
            for (double i = starter.getUllon(); i < queryLRLON; i += nodeWidth) {

                QuadTreeNode nodeRaster = findNode(node, i, j, level);

                /* Finds new nodeWidth/Height based off of most recently found node */
                nodeWidth = nodeRaster.getLrlon() - nodeRaster.getUllon();
                nodeHeight = nodeRaster.getUllat() - nodeRaster.getLrlat();

                /* Adds to ArrayList*/
                imageNodes.add(nodeRaster);
            }
        }

        return imageNodes;
    }



    /* Returns true if point x, y are in the QuadTreeNode */
    private boolean contains(QuadTreeNode node, double x, double y) {
        if (node.ullon <= x && x < node.lrlon && node.lrlat < y && y <= node.ullat) {
            return true;
        }
        return false;
    }
    private QuadTreeNode starterNode = null;
    public QuadTreeNode findNode(QuadTreeNode node, double ullon, double ullat,
                                 int level) {
        //horrible name, but based off of origional use
        //actual use: finds node given ULPoint and correct level/depth

        if (level > 7) {
            level = 7;
        }

        if (contains(node, ullon, ullat)) {
            if (level <= node.depth) {
                starterNode = node;
            } else if (contains(node.nw, ullon, ullat)) {
                findNode(node.nw, ullon, ullat, level);
            } else if (contains(node.ne, ullon, ullat)) {
                findNode(node.ne, ullon, ullat, level);
            } else if (contains(node.sw, ullon, ullat)) {
                findNode(node.sw, ullon, ullat, level);
            } else if (contains(node.se, ullon, ullat)) {
                findNode(node.se, ullon, ullat, level);
            }
        }
        return starterNode;
    }

}
