import java.io.File;
import java.util.Scanner;

public class MazeSolverBFS {
    private static Node startNode;
    private static Node endNode;
    private static int rows;
    private static int columns;
    private static Node grid[][];

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
        createGrid(input);
        connectNodes();
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
