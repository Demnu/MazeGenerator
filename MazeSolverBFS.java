
/* Name: Harrison Collins
 * Student Number: c3282352
 * File: MazeSolverBFS.java
 * Description: 
 * Retrieves a given maze file that reads then solves the maze using breadth first search
 */
import java.io.File;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;
import java.util.Stack;

public class MazeSolverBFS {
    // global variables
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
    private static int totalSteps;
    private static int solutionSteps;
    private static long startTime;
    private static long timeTaken;

    public static void main(String[] args) {
        // get maze from file
        String input = "";
        try {
            Scanner scanner = new Scanner(new File(args[0]));
            input = scanner.nextLine();
        } catch (Exception e) {
            System.out.println(e);
            System.exit(1);
        }
        // two layers to emulate BFS
        currentLayer = new LinkedList<Node>();
        nextLayer = new LinkedList<Node>();
        visitedNodes = new LinkedList<Node>();
        solutionSteps = 0;
        totalSteps = 0;
        startTime = System.currentTimeMillis();
        createGrid(input);
        connectNodes();
        solveMazeUsingBFS();
        outputBFSSolution();

    }

    public static void outputBFSSolution() {
        // display path solution
        displayPathSolution();
        // display number of steps in solution
        System.out.println(solutionSteps);
        // display number of total steps in solution
        System.out.println(totalSteps);
        // display time taken to solve maze
        System.out.println(timeTaken);
        // display solution on maze
        if (rows * columns <= 25) {
            displaySolutionOnMaze();
        }

    }

    public static void displayPathSolution() {
        String path = "(";
        Stack<Integer> stack = new Stack<>();
        Node tempNode = endNode;
        // start at the end node and trace back to the start node
        while (tempNode != startNode) {
            tempNode.setCorrectStep(true);
            // push visited node to a stack that will then be read to output the results in
            // the right order
            stack.push(tempNode.getValue());
            tempNode = tempNode.getParent();
            solutionSteps++;
        }
        String value;
        // output the path taken to the end node
        while (!stack.isEmpty()) {
            value = Integer.toString(stack.pop());
            if (Integer.parseInt(value) != endNode.getValue()) {
                path += value + ",";
            } else {
                path += value + ")";
            }
        }
        System.out.println(path);
    }

    public static void displaySolutionOnMaze() {

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

    // solves the maze using BFS
    public static void solveMazeUsingBFS() {
        boolean foundEnd = false;
        currentLayer.add(startNode);
        // while loop that will finish once the end node is found
        while (!foundEnd) {
            for (Node nextNode : currentLayer) {
                foundEnd = checkNode(nextNode);
                if (foundEnd) {
                    timeTaken = System.currentTimeMillis() - startTime;
                    break;
                }
            }
            // start searching the next layer
            currentLayer = nextLayer;
            nextLayer = new LinkedList<Node>();
        }
    }

    // checks the current node to see if it is the end node otherwise add
    // neighboring available nodes to the next layer
    public static boolean checkNode(Node nextNode) {
        visitedNodes.add(nextNode);
        nextNode.setVisited(true);
        totalSteps++;

        if (nextNode == endNode) {
            return true;
        } else {
            // add neighbouring available nodes to the next layer
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

    // method that recieves the data from the given file that then creates a maze
    // using a multidimensional array of nodes
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
