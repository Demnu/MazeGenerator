import java.util.Random;
import java.util.Stack;

public class A1UsingNodes {
    private static final int BOTH_CLOSED = 0;
    private static final int RIGHT_ONLY_OPEN = 1;
    private static final int BOTTOM_ONLY_OPEN = 2;
    private static final int BOTH_OPEN = 3;
    private static Node[][] grid;
    private static Node startNode;
    private static Node endNode;
    private static int rows;
    private static int columns;
    private static int numberOfNodes;
    private static int numberOfNodesVisted = 0;
    public static Stack<Node> stack;
    static Random rn = new Random();

    public static void main(String[] args) {
        stack = new Stack<Node>();
        rows = 1;
        columns = 5;
        numberOfNodes = rows * columns;
        generateNodes();
        connectNodes();
        generateMaze();
        displayMaze();
    }

    public static void generateMaze() {
        // get starting point
        int startX = rn.nextInt(rows);
        int startY = rn.nextInt(columns);
        startNode = grid[startX][startY];
        generateCell(startNode);
        while (!stack.isEmpty()) {
            backtrack();
        }
    }

    public static void generateCell(Node cell) {
        if (cell.getVisited() == false) {
            numberOfNodesVisted++;
            if (numberOfNodesVisted == numberOfNodes) {
                endNode = cell;
            }
        }
        cell.setVisited(true);
        if (cell.hasUnvisitedNeighbor()) {
            stack.push(cell);
            Node randomNeighbor = cell.getRandomUnvistedNeighbor();
            cell.calculateCellOpenness(cell, randomNeighbor);
            generateCell(randomNeighbor);
        }
    }

    public static void backtrack() {
        if (!stack.isEmpty()) {
            generateCell(stack.pop());
        }
    }

    public static void connectNodes() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {

                // if node has right neighbor
                if (columns > 1 && j != (columns - 1)) {
                    grid[i][j].setRight(grid[i][j + 1]);
                }
                // if node has top neighbor
                if (i != 0) {
                    grid[i][j].setTop(grid[i - 1][j]);
                }
                // if node has left neighbor
                if (columns > 1 && j != 0) {
                    grid[i][j].setLeft(grid[i][j - 1]);
                }
                // if node has bottom neighbor
                if (i != (rows - 1)) {
                    grid[i][j].setBottom(grid[i + 1][j]);
                }
            }
        }
    }

    public static void generateNodes() {
        grid = new Node[rows][columns];
        int count = 1;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                grid[i][j] = new Node(count);
                count++;
            }
        }
    }

    public static void displayMaze() {
        // display top border
        String horizontalBorder = "-";
        for (int i = 0; i < columns; i++) {
            horizontalBorder += "---";
        }
        System.out.println(horizontalBorder);
        String body = "";
        // display body
        for (int i = 0; i < rows; i++) {
            String line1 = "";
            String line2 = "";
            for (int j = 0; j < columns; j++) {
                Node cell = grid[i][j];
                // add left horizontal wall
                if (cell.getLeft() == null) {
                    line1 += "|";
                    line2 += "|";
                }
                if (cell == startNode) {
                    line1 += "S";
                } else if (cell == endNode) {
                    line1 += "F";
                } else {
                    line1 += " ";
                }
                line1 += " ";
                if (cell.getCellOpenness() == BOTH_CLOSED) {
                    line1 += "|";
                    line2 += "--|";
                } else if (cell.getCellOpenness() == BOTTOM_ONLY_OPEN) {
                    line1 += "|";
                    line2 += "  |";
                } else if (cell.getCellOpenness() == RIGHT_ONLY_OPEN) {
                    line1 += " ";
                    line2 += "--|";
                } else if (cell.getCellOpenness() == BOTH_OPEN) {
                    line1 += " ";
                    line2 += "  |";
                }

            }
            System.out.println(line1);
            if (i == rows - 1) {
                System.out.println(horizontalBorder);
            } else {
                System.out.println(line2);
            }
        }

    }

    public static void outputGrid() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                System.out.print(grid[i][j].getValue() + " - ");
                if (grid[i][j].getCellOpenness() == BOTH_CLOSED) {
                    System.out.print("BOTH CLOSED");
                }
                if (grid[i][j].getCellOpenness() == BOTH_OPEN) {
                    System.out.print("BOTH OPEN");
                }
                if (grid[i][j].getCellOpenness() == RIGHT_ONLY_OPEN) {
                    System.out.print("RIGHT ONLY OPEN");
                }
                if (grid[i][j].getCellOpenness() == BOTTOM_ONLY_OPEN) {
                    System.out.print("BOTTOM ONLY OPEN");
                }
                System.out.println();
            }
        }
    }
}
