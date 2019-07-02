public class Matrix {

    private Fraction[][] matrix;
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
        matrix = new Fraction[row][col];
        size = new Dimension(row, col);
        this.row = row;
        this.col = col;
    }

    public Matrix(Dimension dimension) {
        this(dimension.row, dimension.col);
    }

    public Matrix(double[][] matrix) {
        this.matrix = new Fraction[matrix.length][matrix[0].length];
        for (int i = 0; i < matrix.length; i++)
            for (int j = 0; j < matrix[0].length; j++)
                this.matrix[i][j] = new Fraction(matrix[i][j]);

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
            temp.matrix[i][i] = new Fraction(1);

        return temp;
    }

    public static Matrix add(Matrix from, Matrix to) throws MatrixDimensionMismatchException {

        if (!from.size.equals(to.size))
            throw new MatrixDimensionMismatchException("Operation undefined, expected size : " + from.size + ". Received : " + to.size);

        Matrix temp = new Matrix(from.size);

        for (int i = 0; i < from.row; i++)
            for (int j = 0; j < from.col; j++)
                temp.matrix[i][j] = Fraction.add(from.matrix[i][j], to.matrix[i][j]);

        return temp;
    }

    public static Matrix mulitply(Matrix from, Matrix to) throws MatrixDimensionMismatchException {
        if (from.size.col != to.size.row)
            throw new MatrixDimensionMismatchException("Operation undefined, expected columns of second matrix : " + from.size.col + ". Received : " + to.size.row);

        Matrix temp = new Matrix(from.size.row, to.size.col);

        for (int i = 0; i < from.size.row; i++)
            for (int j = 0; j < to.size.col; j++)
                for (int k = 0; k < from.size.col; k++)
                    temp.matrix[i][j] = Fraction.add(Fraction.multiply(from.matrix[i][k], to.matrix[k][j]),temp.matrix[i][j]);

        return temp;
    }

    public static Matrix reduce(Matrix m, Reduction mode, boolean doShowStep) {
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
            Fraction divisor = temp.matrix[j][j];
            if(mode == Reduction.RREF) {
                rowOp_Divide(temp, j, temp.matrix[j][j]);
                showSteps(doShowStep, "Dividing row " + j + " by " + divisor + " :\n" + temp.toString());
            }

            int startI = 0, endI = temp.row;
            switch (mode){
                case RREF:
                    break;
                case UTRIANGLE:
                    startI = j;
                    endI = temp.row;
            }

            for (int i = startI; i < endI; i++) {
                debug("Starting row " + i);
                if (i == j) {
                    debug("Current row, skipping to next row");
                    continue;
                }
                Fraction multiplier = Fraction.multiply(new Fraction(-1), Fraction.divide(temp.matrix[i][j], temp.matrix[j][j]));
                rowOp_Multiply(temp, j, multiplier);
                showSteps(doShowStep, "Multiplying row " + j + " by " + multiplier + " : \n" + temp.toString());
                rowOp_Add(temp, j, i);
                showSteps(doShowStep, "Adding row " + j + " to row " + i + " : \n" + temp.toString());
                rowOp_Divide(temp, j, multiplier);
                showSteps(doShowStep, "Dividing row " + j + " by " + multiplier + " :\n" + temp.toString());
                debug("row " + i + " is Complete");
            }
            debug("column " + j + " is Complete");
        }

        return temp;
    }

    public enum Reduction{
        RREF,
        UTRIANGLE
    }

    private static int firstNonZeroRow(Matrix m, int col, int startRow) {
        for (int i = startRow; i < m.matrix.length; i++)
            if (!m.matrix[i][col].equals(new Fraction(0)))
                return i;

        return -1;
    }

    public static Fraction det(Matrix m, boolean doShowSteps){

        if(m.size.row != m.size.col)
            throw new MatrixOperationException("Operation undefined, determinant must be performed on a square matrix");

        Matrix temp = reduce(m,Reduction.UTRIANGLE, doShowSteps);
        Fraction det = new Fraction(1);

        for (int i = 0; i < temp.size.row; i++)
            det = Fraction.multiply(det, temp.matrix[i][i]);

        return det;
    }

    private static void rowOp_Swap(Matrix m, int from, int to) {
        Fraction temp;
        for (int j = 0; j < m.col; j++) {
            temp = m.matrix[from][j];
            m.matrix[from][j] = m.matrix[to][j];
            m.matrix[to][j] = temp;
        }
    }

    private static void rowOp_Multiply(Matrix m, int row, Fraction multiplier) {
        for (int j = 0; j < m.col; j++)
            m.matrix[row][j] = Fraction.multiply(m.matrix[row][j], multiplier);
    }

    private static void rowOp_Divide(Matrix m, int row, Fraction divisor) {
        for (int j = 0; j < m.col; j++)
            m.matrix[row][j] = Fraction.divide(m.matrix[row][j], divisor);
    }

    private static void rowOp_Add(Matrix m, int from, int to) {
        for (int j = 0; j < m.col; j++)
            m.matrix[to][j] = Fraction.add(m.matrix[to][j], m.matrix[from][j]);
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
                line += String.format("%6s", matrix[i][j].toString());
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

