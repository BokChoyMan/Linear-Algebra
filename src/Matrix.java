public class Matrix {

    //holds the entries of the matrix
    private Fraction[][] matrix;
    //size information of the matrix
    private Dimension size;

    //toggle for printing of debugging info
    private static final boolean DEBUGGING = false;

    private class Dimension {

        final int row;
        final int col;

        Dimension(int row, int col) {
            this.row = row;
            this.col = col;
        }

        /**
         * compare the row and col of dimensions
         * @param other
         * @return true if both row and col are equal
         */
        boolean equals(Dimension other) {
            return this.col == other.col && this.row == other.row;
        }

        @Override
        public String toString() {
            return row + "x" + col;
        }

    }

    /**
     * constructs a new matrix with given row and col
     * @param row
     * @param col
     */
    public Matrix(int row, int col) {
        matrix = new Fraction[row][col];
        size = new Dimension(row, col);
    }

    /**
     * constructs a new matrix with given dimension object
     * @param dimension
     */
    public Matrix(Dimension dimension) {
        this(dimension.row, dimension.col);
    }

    /**
     * constructs a new matrix with give 2d array of doubles. Auto converts double to 2d Fraction array
     * @param matrix
     */
    public Matrix(double[][] matrix) {
        this.matrix = new Fraction[matrix.length][matrix[0].length];
        for (int i = 0; i < matrix.length; i++)
            for (int j = 0; j < matrix[0].length; j++)
                this.matrix[i][j] = new Fraction(matrix[i][j]);

        size = new Dimension(matrix.length, matrix[0].length);
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

    /**
     * checks is the matrix a n*n matrix
     * @return
     */
    public boolean isSquareMatrix(){
        return size.row == size.col;
    }

    /**
     * constructs an Identity whose diagonal is filled with 1's, and 0's elsewhere
     * @param size
     * @return
     */
    public static Matrix identityMatrix(int size) {
        Matrix temp = new Matrix(size, size);
        for (int i = 0; i < size; i++)
            for (int j = 0; j < size; j++) {
                if(i == j)
                    temp.matrix[i][j] = new Fraction(1);
                else
                    temp.matrix[i][j] = new Fraction(0);
            }

        return temp;
    }

    /**
     * combine 2 matrices that have the same number of rows, m1 is placed to the left of m2 in the returned matrix
     * @param m1
     * @param m2
     * @return
     */
    public static Matrix augment(Matrix m1, Matrix m2){

        //throws unchecked exception if rows dont match
        if(m1.size.row != m2.size.row)
            throw new MatrixDimensionMismatchException("Matrix size invalid, expected rows :" + m1.size.row);

        Matrix temp = new Matrix(m1.size.row, m1.size.col + m2.size.col);
        for (int i = 0; i < m1.size.row; i++) {
            for (int j = 0; j < m1.size.col; j++) {
                temp.matrix[i][j] = m1.matrix[i][j];
            }
            for (int j = 0; j < m2.size.col; j++) {
                temp.matrix[i][j+m1.size.col] = m2.matrix[i][j];
            }
        }

        return temp;
    }

    /**
     * extract a subsection of a matrix
     * @param m
     * @param tli top left i coordinate, inclusive
     * @param tlj top left j coordinate, inclusive
     * @param bri bottom right i coordinate, exclusive
     * @param brj bottom right j coordinate, exclusive
     * ex. calling submatrix(m, 0, 3, 3, 6) where m is 1,2,3,4,5,6
     *                                                 1,2,3,4,5,6
     *                                                 1,3,3,4,5,6
     *  would return 4,5,6
     *               4,5,6
     *               4,5,6
     * @return
     */
    public static Matrix subMatrix(Matrix m, int tli, int tlj, int bri, int brj){
        Matrix temp = new Matrix(bri-tli, brj-tlj);
        for (int i = 0; i < bri-tli; i++)
            for (int j = 0; j < brj-tlj ; j++)
                temp.matrix[i][j] = m.matrix[i+tli][j+tlj];

        return temp;
    }

    /**
     * finds the transpose of a matrix by swapping the row and col of a matrix
     * @param m
     * @return
     */
    public static Matrix transpose(Matrix m){
        Matrix temp = new Matrix(m.size.col, m.size.row);
        for (int i = 0; i < temp.size.row; i++)
            for (int j = 0; j < temp.size.col; j++)
                temp.matrix[i][j] = m.matrix[j][i];

        return temp;
    }

    /**
     * adding each corresponding elements form 2 identically sized matrices
     * @param from
     * @param to
     * @return
     */
    public static Matrix add(Matrix from, Matrix to) {

        //throws unchecked exception if size dont match
        if (!from.size.equals(to.size))
            throw new MatrixDimensionMismatchException("Operation undefined, expected size : " + from.size + ". Received : " + to.size);

        Matrix temp = new Matrix(from.size);

        for (int i = 0; i < from.size.row; i++)
            for (int j = 0; j < from.size.col; j++)
                temp.matrix[i][j] = Fraction.add(from.matrix[i][j], to.matrix[i][j]);

        return temp;
    }

    /**
     * multiply 2 matrices that according to rules of matrix multiplication.
     * Each element of the product matrix, p[i][j] is the sum of m1[i][0]*m2[1][0], m1[i][1]*m2[1][j],
     * m1[i][2]*m2[2][j], etc
     * @param from
     * @param to
     * @return the resultant matrix of a axb matrix multiplied to a bxc matrix should be axc
     */
    public static Matrix mulitply(Matrix from, Matrix to) {

        //2 matrices axb and cxd can only be multiplied if b == c
        if (from.size.col != to.size.row)
            throw new MatrixDimensionMismatchException("Operation undefined, expected columns of second matrix : " + from.size.col + ". Received : " + to.size.row);

        Matrix temp = new Matrix(from.size.row, to.size.col);

        for (int i = 0; i < from.size.row; i++)
            for (int j = 0; j < to.size.col; j++)
                for (int k = 0; k < from.size.col; k++)
                    temp.matrix[i][j] = Fraction.add(Fraction.multiply(from.matrix[i][k], to.matrix[k][j]),temp.matrix[i][j]);

        return temp;
    }

    /**
     * perform row reduction on a matrix
     * @param m
     * @param mode reduced row echelon, or lower triangle reduction
     * @param doShowStep whether to display intermediate steps
     * @return a reduced matrix
     */
    public static Matrix reduce(Matrix m, Reduction mode, boolean doShowStep) {
        //creates a copy of the matrix to be operated on
        Matrix temp = m.clone();
        //start from column 0
        for (int j = 0; j < temp.size.row; j++) {
            debug("Starting column " + j);
            //finds the first row that does not contain 0 in a given element.
            //reduced form is anchored on non-zero diagonal elements, thus search always begin with an element on the diagonal.
            int firstNonZeroRow = firstNonZeroRow(temp, j, j);
            //if no such element can be found, then move on to the next column
            if (firstNonZeroRow == -1) {
                showSteps(doShowStep, "Current column can not be further simplified, moving to next column");
                continue;
            }
            //swap the current row with first non zero element row. The row may be swapped with itself, if the current row is non-zero
            rowOp_Swap(temp, j, firstNonZeroRow);
            showSteps(doShowStep, "Swapping row " + j + " with row " + firstNonZeroRow + " : \n" + temp.toString());
            //if rref is desired, finds the multiplier that would reduce the current row
            if(mode == Reduction.RREF) {
                Fraction divisor = temp.matrix[j][j];
                //divide row to reduce, so that the first non zero element is 1
                rowOp_Divide(temp, j, temp.matrix[j][j]);
                showSteps(doShowStep, "Dividing row " + j + " by " + divisor + " :\n" + temp.toString());
            }

            //determine if the entire matrix should be reduced
            int startI = 0, endI = temp.size.row;
            switch (mode){
                case RREF:
                    break;
                case UTRIANGLE:
                    //reduce the bottom half of the matrix, below row j
                    startI = j;
                    endI = temp.size.row;
            }

            for (int i = startI; i < endI; i++) {
                debug("Starting row " + i);
                //if the current row is compared to itself, skip to the next row
                if (i == j) {
                    debug("Current row, skipping to next row");
                    continue;
                }
                //compute the multiplier that would reduce the next row to zero in the current column
                Fraction multiplier = Fraction.multiply(new Fraction(-1), Fraction.divide(temp.matrix[i][j], temp.matrix[j][j]));
                //multiply the current row by multiplier
                rowOp_Multiply(temp, j, multiplier);
                showSteps(doShowStep, "Multiplying row " + j + " by " + multiplier + " : \n" + temp.toString());
                //add the multiplied current row to the next row
                rowOp_Add(temp, j, i);
                showSteps(doShowStep, "Adding row " + j + " to row " + i + " : \n" + temp.toString());
                //divide the current row by multiplier to its original reduced form
                rowOp_Divide(temp, j, multiplier);
                showSteps(doShowStep, "Dividing row " + j + " by " + multiplier + " :\n" + temp.toString());
                debug("row " + i + " is Complete");
            }
            debug("column " + j + " is Complete");
        }

        return temp;
    }

    /**
     * Supported reduction procedures
     */
    public enum Reduction{
        //completely reduces all elements in left most largest square elements of the matrix
        RREF,
        //reduces all elements in bottom left most largest triangular elements of the matrix
        UTRIANGLE
    }

    /**
     * swap row from, with row to
     * @param m
     * @param from
     * @param to
     */
    private static void rowOp_Swap(Matrix m, int from, int to) {
        Fraction temp;
        for (int j = 0; j < m.size.col; j++) {
            temp = m.matrix[from][j];
            m.matrix[from][j] = m.matrix[to][j];
            m.matrix[to][j] = temp;
        }
    }

    /**
     * multiply entire row by multiplier
     * @param m
     * @param row
     * @param multiplier
     */
    private static void rowOp_Multiply(Matrix m, int row, Fraction multiplier) {
        for (int j = 0; j < m.size.col; j++)
            m.matrix[row][j] = Fraction.multiply(m.matrix[row][j], multiplier);
    }

    /**
     * divide entire row by divisor
     * @param m
     * @param row
     * @param divisor
     */
    private static void rowOp_Divide(Matrix m, int row, Fraction divisor) {
        for (int j = 0; j < m.size.col; j++)
            m.matrix[row][j] = Fraction.divide(m.matrix[row][j], divisor);
    }

    /**
     * add row from to row to
     * @param m
     * @param from
     * @param to
     */
    private static void rowOp_Add(Matrix m, int from, int to) {
        for (int j = 0; j < m.size.col; j++)
            m.matrix[to][j] = Fraction.add(m.matrix[to][j], m.matrix[from][j]);
    }

    /**
     * returns the row index in a given column below a row, including that row, where the first non zero element is
     * @param m
     * @param col
     * @param startRow
     * @return returns -1 if no such element is found
     */
    private static int firstNonZeroRow(Matrix m, int col, int startRow) {
        for (int i = startRow; i < m.matrix.length; i++)
            if (!m.matrix[i][col].equals(new Fraction(0)))
                return i;

        return -1;
    }

    /**
     * finds the determinant of a matrix by reducing it to lower triangular form and multiply the diagonal
     * additional methods may be added
     * @param m
     * @param doShowSteps
     * @return
     */
    public static Fraction det(Matrix m, boolean doShowSteps){
        if(!m.isSquareMatrix())
            throw new MatrixOperationException("Operation undefined, determinant must be performed on a square matrix");

        Matrix temp = reduce(m,Reduction.UTRIANGLE, doShowSteps);
        Fraction det = new Fraction(1);

        for (int i = 0; i < temp.size.row; i++)
            det = Fraction.multiply(det, temp.matrix[i][i]);

        return det;
    }

    /**
     * finds the inverse of a square matrix by reducing that matrix augmented with an identity matrix of the same size
     * @param m
     * @param doShowStep
     * @return
     */
    public static Matrix inverse(Matrix m, boolean doShowStep){
        //throws unchecked exception if the matrix is not a square matrix
        if(!m.isSquareMatrix())
            throw new MatrixOperationException("Operation undefined, inverse must be performed on a square matrix");

        //first augment the matrix with identity matrix of same size
        Matrix augment = Matrix.augment(m, Matrix.identityMatrix(m.size.row));
        //then reduce till the left most largest square elements are completely reduced
        Matrix inverse = reduce(augment, Reduction.RREF, doShowStep);

        return Matrix.subMatrix(inverse, 0, m.size.row, m.size.row, m.size.row * 2);
    }

    /**
     * prints steps to a solution if specified
     * @param doShowStep whether to show steps
     * @param step steps to be printed
     */
    private static void showSteps(boolean doShowStep, String step) {
        if (doShowStep)
            System.out.println(step);
    }

    /**
     * debug program specific info if specified
     * @param str
     */
    private static void debug(String str) {
        if (DEBUGGING)
            System.out.println(str);
    }

    /**
     * creates a matrix that contains exactly the same elements of the current matrix
     * @return
     */
    public Matrix clone() {
        Matrix temp = new Matrix(this.size.row, this.size.col);
        for (int i = 0; i < size.row; i++)
            for (int j = 0; j < size.col; j++)
                temp.matrix[i][j] = this.matrix[i][j];

        return temp;
    }

    /**
     * return a string representation based on the length of each elements' String
     * @return
     */
    @Override
    public String toString () {
        //unicode characters in ths
        String temp = "";
        //identify the longest element, and determine the String length of all elements
        int length = longestNum() + 1;
        for (int i = 0; i < size.row; i++) {

            String line = "";
            line += "\u239c";
            for (int j = 0; j < size.col; j++)
                line += String.format("%"+ length + "s", matrix[i][j].toString());
            line += "\u239f";

            if (i == 0)
                line = "\u23a1" + line.substring(1, line.length() - 1) + "\u23a4";
            if (i == size.row - 1)
                line = "\u23a3" + line.substring(1, line.length() - 1) + "\u23a6";
            temp += line + "\n";

        }
        return temp;
    }

    /**
     * identify the element with the longest String in a matrix
     * @return
     */
    private int longestNum(){
        int longest = 0;
        for (int i = 0; i < size.row; i++)
            for (int j = 0; j < size.col; j++)
                if(matrix[i][j].toString().length() >= longest)
                    longest = matrix[i][j].toString().length();

        return longest;
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

