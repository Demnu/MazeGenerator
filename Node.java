import java.util.Random;

public class Node {
    private static final int RIGHT_ONLY_OPEN = 1;
    private static final int BOTTOM_ONLY_OPEN = 2;
    private static final int BOTH_OPEN = 3;
    private Random rn = new Random();
    private int value;
    private Node top;
    private Node right;
    private Node bottom;
    private Node left;
    private Node parent;
    private boolean visited;
    private int cellOpenness = 0;

    Node(int value) {
        this.value = value;
        this.visited = false;
    }

    public void calculateCellOpenness(Node currentNode, Node nextNode) {
        // if node goes to bottom
        if (currentNode.bottom == nextNode) {
            if (cellOpenness == RIGHT_ONLY_OPEN) {
                cellOpenness = BOTH_OPEN;
            } else {
                cellOpenness = BOTTOM_ONLY_OPEN;
            }
        }
        // if node goes to right
        else if (currentNode.right == nextNode) {
            if (cellOpenness == BOTTOM_ONLY_OPEN) {
                cellOpenness = BOTH_OPEN;
            } else {
                cellOpenness = RIGHT_ONLY_OPEN;
            }
        }
        // if node goes to left
        else if (nextNode.right == currentNode) {
            if (nextNode.cellOpenness == BOTTOM_ONLY_OPEN) {
                nextNode.cellOpenness = BOTH_OPEN;
            } else {
                nextNode.cellOpenness = RIGHT_ONLY_OPEN;
            }
        }
        // if node goes to top
        else if (nextNode.bottom == currentNode) {
            if (nextNode.cellOpenness == RIGHT_ONLY_OPEN) {
                nextNode.cellOpenness = BOTH_OPEN;
            } else {
                nextNode.cellOpenness = BOTTOM_ONLY_OPEN;
            }
        }
    }

    public boolean hasUnvisitedNeighbor() {
        if (top != null && top.getVisited() == false) {
            return true;
        }
        if (right != null && right.getVisited() == false) {
            return true;

        }
        if (bottom != null && bottom.getVisited() == false) {
            return true;

        }
        if (left != null && left.getVisited() == false) {
            return true;

        }
        return false;
    }

    public Node getRandomUnvistedNeighbor() {
        int[] array = new int[4];
        int count = 0;
        if (top != null && top.getVisited() == false) {
            array[count] = 0;
            count++;
        }
        if (right != null && right.getVisited() == false) {
            array[count] = 1;
            count++;
        }
        if (bottom != null && bottom.getVisited() == false) {
            array[count] = 2;
            count++;
        }
        if (left != null && left.getVisited() == false) {
            array[count] = 3;
            count++;
        }
        if (count == 0) {
            return null;
        }
        int rndNumber = rn.nextInt(count);

        if (array[rndNumber] == 0) {
            return top;
        } else if (array[rndNumber] == 1) {
            return right;
        } else if (array[rndNumber] == 2) {
            return bottom;
        } else if (array[rndNumber] == 3) {
            return left;
        }
        return null;
    }

    public boolean getVisited() {
        return visited;
    }

    public Node getBottom() {
        return bottom;
    }

    public int getValue() {
        return value;
    }

    public Node getLeft() {
        return left;
    }

    public Node getRight() {
        return right;
    }

    public Node getTop() {
        return top;
    }

    public void setVisited(boolean visited) {
        this.visited = visited;
    }

    public void setBottom(Node bottom) {
        this.bottom = bottom;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public void setLeft(Node left) {
        this.left = left;
    }

    public void setRight(Node right) {
        this.right = right;
    }

    public void setTop(Node top) {
        this.top = top;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public Node getParent() {
        return parent;
    }

    public void setCellOpenness(int cellOpenness) {
        this.cellOpenness = cellOpenness;
    }

    public int getCellOpenness() {
        return cellOpenness;
    }
}
