package implementations;

public class TheMatrix {

    private char[][] matrix;

    private char fillChar;

    private char startCharacter;

    private int startRow;

    private int startCol;

    public TheMatrix(char[][] matrix, char fillChar, int startRow, int startCol) {
        this.matrix = matrix;
        this.fillChar = fillChar;
        this.startRow = startRow;
        this.startCol = startCol;
        startCharacter = matrix[startRow][startCol];
    }

    //TODO: make bfs implementation, the implementatiom=n with queue bfs will be slower because it will store more elements
    public void solve() {
        fillMatrix(startRow, startCol);
    }

    private void fillMatrix(int row, int col) {
        if (!isIndexValid(row, col)) {
            return;
        }

        matrix[row][col] = fillChar;

        fillMatrix(row + 1, col);
        fillMatrix(row, col + 1);
        fillMatrix(row - 1, col);
        fillMatrix(row, col - 1);
    }

    private boolean isIndexValid(int row, int col) {
        return isInsideMatrix(row, col)
                && matrix[row][col] == startCharacter;
    }

    private boolean isInsideMatrix(int row, int col) {
        return row >= 0 && row < matrix.length
                && col >= 0 && col < matrix[row].length;
    }

    public String toOutputString() {
        StringBuilder outputMatrix = new StringBuilder();
        for (int row = 0; row < matrix.length; row++) {
            for (int col = 0; col < matrix[row].length; col++) {
                outputMatrix.append(matrix[row][col]);
            }
            outputMatrix.append(System.lineSeparator());
        }
        return outputMatrix.toString()
                .trim();
    }
}
