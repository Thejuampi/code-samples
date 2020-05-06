import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

public class Grid implements Cloneable {

    public static final int SQUARES = 81;
    public static final int BOXES = 9;
    public static final int ROWS = 9;
    public static final int COLUMNS = 9;

    private Grid() {
    }

    private int[] squares = new int[SQUARES];
    private int[] colsSet = new int[COLUMNS];
    private int[] rowsSet = new int[ROWS];
    private int[] boxesSet = new int[BOXES];

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

    public static Grid create(Reader rd) {
        Grid grid = new Grid();
        try {
            for (int loc = 0; loc < grid.squares.length; ) {
                int ch = rd.read();

                if (ch < 0) {
                    return null;
                }

                if (ch == '#') { // skip to end-of-line indicator
                    while (ch >= 0
                            && ch != '\n'
                            && ch != '\r') {
                        ch = rd.read();
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

    public int findEmptyCell() {
        for (int i = 0; i < squares.length; i++) {
            if (squares[i] == 0) {
                return i;
            }
        }
        return -1; // not found
    }

    public boolean set(int loc, int num) {
        int r = loc / 9;
        int c = loc % 9;
        int blockLoc = (r / 3) * 3 + c / 3;

        boolean canSet = squares[loc] == 0
                && (colsSet[c] & (1 << num)) == 0 //obvious!
                && (rowsSet[r] & (1 << num)) == 0
                && (boxesSet[blockLoc] & (1 << num)) == 0;

        if (!canSet)
            return false;

        squares[loc] = num;
        colsSet[c] |= (1 << num);
        rowsSet[r] |= (1 << num);
        boxesSet[blockLoc] |= (1 << num);

        return true;
    }

    public void clear(int loc) {
        int r = loc / 9;
        int c = loc % 9;
        int blockLoc = (r / 3) * 3 + c / 3;

        int num = squares[loc];
        squares[loc] = 0; // clear value
        colsSet[c] ^= (1 << num); // bitwise XOR
        rowsSet[r] ^= (1 << num);
        boxesSet[blockLoc] ^= (1 << num);
    }

    @Override
    protected Grid clone() {
        Grid g = new Grid();
        g.squares = squares.clone();
        g.colsSet = colsSet.clone();
        g.rowsSet = rowsSet.clone();
        g.boxesSet = boxesSet.clone();

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
