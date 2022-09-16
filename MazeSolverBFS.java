import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Scanner;

public class MazeSolverBFS {
    private static final int BOTH_CLOSED = 0;
    private static final int RIGHT_ONLY_OPEN = 1;
    private static final int BOTTOM_ONLY_OPEN = 2;
    private static final int BOTH_OPEN = 3;
    private static Node startNode;
    private static Node endNode;
    private static int rows;
    private static int columns;
    private static Node grid[][];
    private static Queue<Node> currentLayer;
    private static Queue<Node> nextLayer;
    private static Queue<Node> visitedNodes;
    private static int steps;

    public static void main(String[] args) {
        // get maze from file
        String input = "";
        try {
            Scanner scanner = new Scanner(new File("data.txt"));
            input = scanner.nextLine();
        } catch (Exception e) {
            System.out.println(e);
            System.exit(1);
        }
        currentLayer = new LinkedList<Node>();
        nextLayer = new LinkedList<Node>();
        visitedNodes = new LinkedList<Node>();
        steps = 0;
        createGrid(input);
        connectNodes();
        solveMazeUsingBFS();
        outputBFSSolution();

    }

    public static void outputBFSSolution() {
        Node tempNode = endNode.getParent();
        while (tempNode != startNode) {
            tempNode.setCorrectStep(true);
            tempNode = tempNode.getParent();
        }
        // display top border
        String horizontalBorder = "-";
        for (int i = 0; i < columns; i++) {
            horizontalBorder += "---";
        }
        System.out.println(horizontalBorder);
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
                } else if (cell.getCorrectStep() == true) {
                    line1 += "*";
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

    public static void solveMazeUsingBFS() {
        boolean foundEnd = false;
        currentLayer.add(startNode);
        while (!foundEnd) {
            for (Node nextNode : currentLayer) {
                foundEnd = checkNode(nextNode);
                if (foundEnd) {
                    break;
                }
            }
            currentLayer = nextLayer;
            nextLayer = new LinkedList<Node>();
        }

    }

    public static boolean checkNode(Node nextNode) {
        visitedNodes.add(nextNode);
        nextNode.setVisited(true);
        steps++;

        if (nextNode == endNode) {
            return true;
        } else {
            for (Node tempNode : nextNode.getAvailableNodes()) {
                tempNode.setParent(nextNode);
                nextLayer.add(tempNode);
            }
        }
        return false;

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

    public static void createGrid(String input) {
        try {
            String[] arrSplit = input.split("");
            int i = 0;
            String rowsStr = "";
            // get n
            while (!arrSplit[i].equals(",")) {
                rowsStr += arrSplit[i];
                i++;
            }
            rows = Integer.parseInt(rowsStr);
            i++;
            // get m
            String columnsStr = "";
            while (!arrSplit[i].equals(":")) {
                columnsStr += arrSplit[i];
                i++;
            }
            columns = Integer.parseInt(columnsStr);
            i++;
            // get startValue
            String startValueStr = "";
            while (!arrSplit[i].equals(":")) {
                startValueStr += arrSplit[i];
                i++;
            }
            int startValue = Integer.parseInt(startValueStr);
            i++;
            // get endValue
            String endValueStr = "";
            while (!arrSplit[i].equals(":")) {
                endValueStr += arrSplit[i];
                i++;
            }
            int endValue = Integer.parseInt(endValueStr);
            i++;

            // create grid from cell_openness_list
            grid = new Node[rows][columns];

            int rowIndex = 0;
            int columnIndex = 0;
            int value = 1;
            int cellOpenness;
            for (int j = i; j < arrSplit.length; j++) {
                cellOpenness = Integer.parseInt(arrSplit[j]);
                Node tempNode = new Node(value, cellOpenness);
                // check if node is startNode or endNode
                if (value == startValue) {
                    startNode = tempNode;
                } else if (value == endValue) {
                    endNode = tempNode;
                }
                grid[rowIndex][columnIndex] = tempNode;
                columnIndex++;
                if ((columnIndex + 1) % columns == 1) {
                    rowIndex++;
                    columnIndex = 0;
                }
                value++;
            }
        } catch (Exception e) {
            System.out.println("Error: Input file has incorrect format");
            System.out.println(e);
            System.exit(1);
        }

    }
}
