import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

public class Grid implements Cloneable {

    public static final int SQUARES = 81;
    public static final int BOXES = 9;
    public static final int ROWS = 9;
    public static final int COLUMNS = 9;
    public static final int EMPTY_SQUARE_NOT_FOUND = -1;
    public static final int VALUE_NOT_FOUND = 0;
    public static final int BOX_SIZE = 3;

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
                int character = reader.read();

                if (character < 0) {
                    return null;
                }

                if (isComment(character)) { // skip to end-of-line indicator
                    skipUntilEndOfLine(reader, character);
                } else if (isGivenValue(character)) {
                    grid.set(loc, character - '0');
                    loc++;
                } else if (isEmptySquare(character)) {
                    loc++;
                }
            }
            return grid;
        } catch (IOException e) {
            return null; // silent fail!
        }
    }

    private static void skipUntilEndOfLine(Reader reader, int character) throws IOException {
        while (character >= 0
                && character != '\n'
                && character != '\r') {
            character = reader.read();
        }
    }

    private static boolean isComment(int character) {
        return character == '#';
    }

    private static boolean isEmptySquare(int character) {
        return character == '.' || character == '0';
    }

    private static boolean isGivenValue(int character) {
        return character >= '1' && character <= '9';
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

        boolean allowed = squares[squareIndex] == VALUE_NOT_FOUND
                && (columnsBitSet[columnIndex] & (1 << num)) == VALUE_NOT_FOUND //obvious!
                && (rowsBitSet[rowIndex] & (1 << num)) == VALUE_NOT_FOUND
                && (boxBitSet[boxIndex] & (1 << num)) == VALUE_NOT_FOUND;

        if (!allowed)
            return false;

        squares[squareIndex] = num;
        rowsBitSet[rowIndex] |= (1 << num);
        columnsBitSet[columnIndex] |= (1 << num);
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
        return (rowIndex / BOX_SIZE) * BOX_SIZE + columnIndex / BOX_SIZE;
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
