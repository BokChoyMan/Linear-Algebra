public class Matrix {

    private double[][] matrix;
    private Dimension size;
    private int row, col;

    private static final boolean DEBUGGING = false;

    private class Dimension {

        public final int row;
        public final int col;

        public Dimension(int row, int col) {
            this.row = row;
            this.col = col;
        }

        @Override
        public boolean equals(Object other) {
            return this.col == ((Dimension) other).col && this.row == ((Dimension) other).row;
        }

        @Override
        public String toString() {
            return "";
        }

    }

    public Matrix(int row, int col) {
        matrix = new double[row][col];
        size = new Dimension(row, col);
        this.row = row;
        this.col = col;
    }

    public Matrix(Dimension dimension) {
        this(dimension.row, dimension.col);
    }

    public Matrix(double[][] matrix) {
        this.matrix = matrix;
        size = new Dimension(matrix.length, matrix[0].length);
        this.row = matrix.length;
        this.col = matrix[0].length;
    }

//    public Matrix(AppendMatrix mode, Matrix... matrices) throws MatrixDimensionMismatchException {
//
//        switch (mode){
//
//            case VERTICAL:
//
//                int col = matrices[0].row;
//                int row = 0;
//                for (int i = 0; i < matrices.length; i++) {
//                    if(matrices[i].col != col)
//                        throw new MatrixDimensionMismatchException("Matrix size invalid, expected columns: " + col + ". Received : " + matrices[i].col);
//                    row += matrices[i].row;
//                }
//
//
//
//            break;
//
//            case HORIZONTAL:
//
//            break;
//
//        }
//
//    }

    public enum AppendMatrix {

        VERTICAL,

        HORIZONTAL,

    }

    public static Matrix identityMatrix(int size) {
        Matrix temp = new Matrix(size, size);
        for (int i = 0; i < size; i++)
            temp.matrix[i][i] = 1;

        return temp;
    }

    public static Matrix add(Matrix from, Matrix to) throws MatrixDimensionMismatchException {

        if (!from.size.equals(to.size))
            throw new MatrixDimensionMismatchException("Operation undefined, expected size : " + from.size + ". Received : " + to.size);

        Matrix temp = new Matrix(from.size);

        for (int i = 0; i < from.row; i++)
            for (int j = 0; j < from.col; j++)
                temp.matrix[i][j] = from.matrix[i][j] + to.matrix[i][j];

        return temp;
    }

    public static Matrix mulitply(Matrix from, Matrix to) throws MatrixDimensionMismatchException {
        if (from.size.col != to.size.row)
            throw new MatrixDimensionMismatchException("Operation undefined, expected columns of second matrix : " + from.size.col + ". Received : " + to.size.row);

        Matrix temp = new Matrix(from.size.row, to.size.col);

        for (int i = 0; i < from.size.row; i++)
            for (int j = 0; j < to.size.col; j++)
                for (int k = 0; k < from.size.col; k++)
                    temp.matrix[i][j] += from.matrix[i][k] * to.matrix[k][j];

        return temp;
    }

    public static Matrix rref(Matrix m, boolean doShowStep) {
        Matrix temp = m.clone();

        for (int j = 0; j < temp.row; j++) {
            debug("Starting column " + j);
            int firstNonZeroRow = firstNonZeroRow(temp, j, j);
            if (firstNonZeroRow == -1) {
                showSteps(doShowStep, "Current column can not be further simplified, moving to next column");
                continue;
            }
            rowOp_Swap(temp, j, firstNonZeroRow);
            showSteps(doShowStep, "Swapping row " + j + " with row " + firstNonZeroRow + " : \n" + temp.toString());
            double divisor = temp.matrix[j][j];
            rowOp_Divide(temp, j, temp.matrix[j][j]);
            showSteps(doShowStep, "Dividing row " + j + " by " + divisor + " :\n" + temp.toString());
            for (int i = 0; i < temp.row; i++) {
                debug("Starting row " + i);
                if (i == j) {
                    debug("Current row, skipping to next row");
                    continue;
                }
                double multiplier = -temp.matrix[i][j] / temp.matrix[j][j];
                rowOp_Multiply(temp, j, multiplier);
                showSteps(doShowStep, "Multiplying row " + j + " by " + multiplier + " : \n" + temp.toString());
                rowOp_Add(temp, j, i);
                showSteps(doShowStep, "Adding row " + j + " to row " + i + " : \n" + temp.toString());
                rowOp_Divide(temp, j, multiplier);
                showSteps(doShowStep, "Dividing row " + i + " by " + multiplier + " :\n" + temp.toString());
                debug("row " + i + " is Complete");
            }
            debug("column " + j + " is Complete");
        }

        return temp;
    }

    private static int firstNonZeroRow(Matrix m, int col, int startRow) {
        for (int i = startRow; i < m.matrix.length; i++)
            if (m.matrix[i][col] != 0)
                return i;

        return -1;
    }

    private static void rowOp_Swap(Matrix m, int from, int to) {
        double temp;
        for (int j = 0; j < m.col; j++) {
            temp = m.matrix[from][j];
            m.matrix[from][j] = m.matrix[to][j];
            m.matrix[to][j] = temp;
        }
    }

    private static void rowOp_Multiply(Matrix m, int row, double multiplier) {
        for (int j = 0; j < m.col; j++)
            m.matrix[row][j] *= multiplier;
    }

    private static void rowOp_Divide(Matrix m, int row, double divisor) {
        for (int j = 0; j < m.col; j++)
            m.matrix[row][j] /= divisor;
    }

    private static void rowOp_Add(Matrix m, int from, int to) {
        for (int j = 0; j < m.col; j++)
            m.matrix[to][j] += m.matrix[from][j];
    }

    private static void showSteps(boolean doShowStep, String step) {
        if (doShowStep)
            System.out.println(step);
    }

    private static void debug(String str) {
        if (DEBUGGING)
            System.out.println(str);
    }

    public Matrix clone() {
        Matrix temp = new Matrix(this.row, this.col);
        for (int i = 0; i < row; i++)
            for (int j = 0; j < col; j++)
                temp.matrix[i][j] = this.matrix[i][j];

        return temp;
    }


    @Override
    public String toString () {
        String temp = "";
        for (int i = 0; i < row; i++) {

            String line = "";
            line += "\u239c";
            for (int j = 0; j < col; j++)
                line += String.format("%6.2f", matrix[i][j]);
            line += "\u239f";

            if (i == 0)
                line = "\u23a1" + line.substring(1, line.length() - 1) + "\u23a4";
            if (i == row - 1)
                line = "\u23a3" + line.substring(1, line.length() - 1) + "\u23a6";
            temp += line + "\n";

        }
        return temp;
    }

    public static class MatrixOperationException extends RuntimeException {
        public MatrixOperationException(String message) {
            super(message);
        }
    }

    public static class MatrixDimensionMismatchException extends MatrixOperationException {
        public MatrixDimensionMismatchException(String message) {
            super(message);
        }
    }

}

