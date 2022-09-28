
/* Name: Harrison Collins
 * Student Number: c3282352
 * File: MazeGenerator.java
 * Description: 
 * Generates a maze and saves to a file
 * Must input amount of rows and columns
 */
import java.util.Random;
import java.util.Stack;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class MazeGenerator {
    // global variables
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
    private static String cell_openness_list = "";
    private static Stack<Node> stack;
    static Random rn = new Random();

    public static void main(String[] args) {
        // read inputted values for size of rows and columns
        try {
            rows = Integer.parseInt(args[0]);
            columns = Integer.parseInt(args[1]);
            if (rows < 1 || columns < 1) {
                System.out.println("Error! Row and column values must be more than 0");
                System.exit(1);
            }
            if (rows * columns < 2) {
                System.out.println("Error! There must be more than 1 cell");
                System.exit(1);
            }
        } catch (Exception e) {
            System.out.println("Error! Row and column values must be integers");
            System.exit(1);
        }
        // read inputted file name
        String fileName = "";
        try {
            fileName = args[2];
        } catch (Exception e) {
            System.out.println("Error! No file name was given");
            System.exit(-1);
        }
        stack = new Stack<Node>();
        numberOfNodes = rows * columns;
        generateNodes();
        connectNodes();
        generateMaze();
        if (rows * columns <= 25) {
            displayMaze();
        }
        calculateCellOpennessList();
        saveMazeToFile(fileName);
    }

    public static void calculateCellOpennessList() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                cell_openness_list += grid[i][j].getCellOpenness();
            }
        }
    }

    // method that picks a random node as the start and then selects a random path
    public static void generateMaze() {
        // get a random starting point
        int startX = rn.nextInt(rows);
        int startY = rn.nextInt(columns);
        startNode = grid[startX][startY];
        // walk a random path from the start node
        generateCell(startNode);
        // once random path is completed backtrack to visit the remaining unvisited
        // nodes
        while (!stack.isEmpty()) {
            backtrack();
        }
    }

    public static void generateCell(Node cell) {
        if (cell.getVisited() == false) {
            numberOfNodesVisted++;
            // check if node visiting is the last node to be visited
            if (numberOfNodesVisted == numberOfNodes) {
                endNode = cell;
            }
        }
        cell.setVisited(true);

        if (cell.hasUnvisitedNeighbor()) {
            // add node to stack to be revisted when backtracking
            stack.push(cell);
            Node randomNeighbor = cell.getRandomUnvistedNeighbor();
            cell.calculateCellOpenness(cell, randomNeighbor);
            // call generateCell with the randomNeighbor
            generateCell(randomNeighbor);
        }
    }

    public static void backtrack() {
        // visit nodes that had more than one neighbor
        if (!stack.isEmpty()) {
            generateCell(stack.pop());
        }
    }

    // method that will connect the nodes to each other based on their location on
    // the grid
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
        // generates a multidimensional array of Nodes
        grid = new Node[rows][columns];
        int count = 1;
        // instantiates a new node for each cell in the grid
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
        // display body
        for (int i = 0; i < rows; i++) {
            String line1 = "";
            String line2 = "";
            for (int j = 0; j < columns; j++) {
                Node cell = grid[i][j];
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

    public static void saveMazeToFile(String fileName) {
        String line;
        line = rows + "," + columns + ":" + startNode.getValue() + ":" + endNode.getValue() + ":" + cell_openness_list;
        // create new file
        try {
            File file = new File(fileName);
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // write to new file
        try {
            FileWriter myWriter = new FileWriter(fileName);
            myWriter.write(line);
            myWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
