import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class SudokuSolver {

    public static void main(String[] args) throws Exception {
        new SudokuSolver().go(args[0]);
    }

    private void go(String s) throws Exception {
        FileReader rd = new FileReader(s);

        Grid grid = Grid.create(rd);

        ArrayList<Grid> solutions = new ArrayList<>();
        solve(grid, solutions);

        System.out.println("Original");
        System.out.println(grid);


        if (solutions.size() == 0) {
            System.out.println("Unsolvable");
        } else if (solutions.size() == 1) {
            System.out.println("Solved");
            System.out.println(solutions.get(0));
        } else {
            System.out.println("At least two solutions");
            System.out.println(solutions.get(0));
            System.out.println(solutions.get(1));
        }

    }

    private static void solve(Grid grid, List<Grid> solutions) {
        if (solutions.size() >= 2) {
            return;
        }

        int loc = grid.findEmptySquare();
        if (loc < 0) {
            solutions.add(grid.clone());
            return;
        }

        for (int n = 1; n < 10; n++) {
            if (grid.set(loc, n)) {
                solve(grid, solutions);
                grid.clear(loc);
            }
        }
    }
}
