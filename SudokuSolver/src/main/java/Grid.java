import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

public class Grid implements Cloneable {

    public static final int SQUARES = 81;
    public static final int BOXES = 9;
    public static final int ROWS = 9;
    public static final int COLUMNS = 9;
    public static final int EMPTY_SQUARE_NOT_FOUND = -1;

    private Grid() {
    }

    private int[] squares = new int[SQUARES];
    private int[] rowsBitSet = new int[ROWS];
    private int[] columnsBitSet = new int[COLUMNS];
    private int[] boxBitSet = new int[BOXES];

    public static Grid create(int[][] numbers) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < numbers.length; i++) {
            for (int j = 0; j < numbers.length; j++) {
                int n = numbers[i][j];
                sb.append(n == 0 ? "." : String.valueOf(n));
            }
        }
        return create(new StringReader(sb.toString()));
    }

    public static Grid create(Reader reader) {
        Grid grid = new Grid();
        try {
            for (int loc = 0; loc < grid.squares.length; ) {
                int ch = reader.read();

                if (ch < 0) {
                    return null;
                }

                if (ch == '#') { // skip to end-of-line indicator
                    while (ch >= 0
                            && ch != '\n'
                            && ch != '\r') {
                        ch = reader.read();
                    }
                } else if (ch >= '1' && ch <= '9') { // a "given" value
                    grid.set(loc, ch - '0');
                    loc++;
                } else if (ch == '.' || ch == '0') { // empty cell indicator
                    loc++;
                }
            }
            return grid;
        } catch (IOException e) {
            return null; // silent fail!
        }
    }

    public int findEmptySquare() {
        for (int i = 0; i < squares.length; i++) {
            if (squares[i] == 0) {
                return i;
            }
        }
        return EMPTY_SQUARE_NOT_FOUND;
    }

    public boolean set(int squareIndex, int num) {
        int rowIndex = rowIndex(squareIndex);
        int columnIndex = columnIndex(squareIndex);
        int boxIndex = boxIndex(rowIndex, columnIndex);

        boolean canSet = squares[squareIndex] == 0
                && (columnsBitSet[columnIndex] & (1 << num)) == 0 //obvious!
                && (rowsBitSet[rowIndex] & (1 << num)) == 0
                && (boxBitSet[boxIndex] & (1 << num)) == 0;

        if (!canSet)
            return false;

        squares[squareIndex] = num;
        columnsBitSet[columnIndex] |= (1 << num);
        rowsBitSet[rowIndex] |= (1 << num);
        boxBitSet[boxIndex] |= (1 << num);

        return true;
    }

    private int columnIndex(int squareIndex) {
        return squareIndex % 9;
    }

    private int rowIndex(int squareIndex) {
        return squareIndex / 9;
    }

    public void clear(int loc) {
        int rowIndex = rowIndex(loc);
        int columnIndex = columnIndex(loc);
        int boxIndex = boxIndex(rowIndex, columnIndex);

        int num = squares[loc];
        squares[loc] = 0; // clear value
        columnsBitSet[columnIndex] ^= (1 << num); // bitwise XOR
        rowsBitSet[rowIndex] ^= (1 << num);
        boxBitSet[boxIndex] ^= (1 << num);
    }

    private int boxIndex(int rowIndex, int columnIndex) {
        return (rowIndex / 3) * 3 + columnIndex / 3;
    }

    @Override
    protected Grid clone() {
        Grid g = new Grid();
        g.squares = squares.clone();
        g.columnsBitSet = columnsBitSet.clone();
        g.rowsBitSet = rowsBitSet.clone();
        g.boxBitSet = boxBitSet.clone();

        return g;
    }

    public int[] getSquares() {
        return squares;
    }

    @Override
    public String toString() {
        StringBuilder buf = new StringBuilder();
        for (int r = 0; r < 9; r++) {
            if (r % 3 == 0) {
                buf.append("-------------------------\n");
            }
            for (int c = 0; c < 9; c++) {
                if (c % 3 == 0) {
                    buf.append("| ");
                }
                int num = squares[r * 9 + c];
                if (num == 0) {
                    buf.append(". ");
                } else {
                    buf.append(num);
                    buf.append(" ");
                }
            }
            buf.append("|\n");
        }
        buf.append("-------------------------");
        return buf.toString();
    }

}
