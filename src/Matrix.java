import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class Matrix {

    //holds the entries of the matrix
    Fraction[][] matrix;
    //size information of the matrix
    Dimension size;

    //toggle for printing of debugging info
    static final boolean DEBUGGING = false;

    class Dimension {

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

        for (int i = 0; i < row; i++)
            for (int j = 0; j < col; j++)
                matrix[i][j] = new Fraction(0);

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
     * constructs the identity matrix of this matrix
     * @return
     */
    public Matrix identityMatrix(){

        if(!isSquareMatrix())
            throw new MatrixDimensionMismatchException("This matrix do not have an identity matrix. Square matrix expected, recieved: " + this.size);

        Matrix temp = new Matrix(this.size);
        for (int i = 0; i < this.size.row; i++)
            for (int j = 0; j < this.size.col; j++) {
                if(i == j)
                    temp.matrix[i][j] = new Fraction(1);
                else
                    temp.matrix[i][j] = new Fraction(0);
            }

        return temp;
    }

    /**
     * combine 2 matrices that have the same number of rows, m1 is placed to the left of m2 in the returned matrix
     * @return
     */
    public Matrix augment(Matrix m){

        //throws unchecked exception if rows dont match
        if(this.size.row != m.size.row)
            throw new MatrixDimensionMismatchException("Matrix size invalid, expected rows :" + this.size.row);

        Matrix temp = new Matrix(this.size.row, this.size.col + m.size.col);
        for (int i = 0; i < this.size.row; i++) {
            for (int j = 0; j < this.size.col; j++) {
                temp.matrix[i][j] = this.matrix[i][j];
            }
            for (int j = 0; j < m.size.col; j++) {
                temp.matrix[i][j+this.size.col] = m.matrix[i][j];
            }
        }

        return temp;
    }

    /**
     * extract a subsection of a matrix
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
    public Matrix subMatrix(int tli, int tlj, int bri, int brj){
        Matrix temp = new Matrix(bri-tli, brj-tlj);
        for (int i = 0; i < bri-tli; i++)
            for (int j = 0; j < brj-tlj ; j++)
                temp.matrix[i][j] = this.matrix[i+tli][j+tlj];

        return temp;
    }

    /**
     * finds the transpose of a matrix by swapping the row and col of a matrix
     * @return
     */
    public Matrix transpose(){
        Matrix temp = new Matrix(this.size.col, this.size.row);
        for (int i = 0; i < temp.size.row; i++)
            for (int j = 0; j < temp.size.col; j++)
                temp.matrix[i][j] = this.matrix[j][i];

        return temp;
    }

    /**
     * adding each corresponding elements form 2 identically sized matrices
     * @param m
     * @return
     */
    public Matrix add(Matrix m) {

        //throws unchecked exception if size dont match
        if (!this.size.equals(m.size))
            throw new MatrixDimensionMismatchException("Operation undefined, expected size : " + this.size + ". Received : " + m.size);

        Matrix temp = new Matrix(this.size);

        for (int i = 0; i < this.size.row; i++)
            for (int j = 0; j < this.size.col; j++)
                temp.matrix[i][j] = this.matrix[i][j].add(m.matrix[i][j]);

        return temp;
    }

    /**
     * adding each corresponding elements form 2 identically sized matrices
     * @param m
     * @return
     */
    public Matrix subtract(Matrix m) {

        //throws unchecked exception if size dont match
        if (!this.size.equals(m.size))
            throw new MatrixDimensionMismatchException("Operation undefined, expected size : " + this.size + ". Received : " + m.size);

        Matrix temp = new Matrix(this.size);

        for (int i = 0; i < this.size.row; i++)
            for (int j = 0; j < this.size.col; j++)
                temp.matrix[i][j] = this.matrix[i][j].subtract(m.matrix[i][j]);

        return temp;
    }

    /**
     * multiply 2 matrices that according to rules of matrix multiplication.
     * Each element of the product matrix, p[i][j] is the sum of m1[i][0]*m2[1][0], m1[i][1]*m2[1][j],
     * m1[i][2]*m2[2][j], etc
     * @param m
     * @return the resultant matrix of a axb matrix multiplied to a bxc matrix should be axc
     */
    public Matrix multiply(Matrix m) {

        //2 matrices axb and cxd can only be multiplied if b == c
        if (this.size.col != m.size.row)
            throw new MatrixDimensionMismatchException("Operation undefined, expected columns of second matrix : " + this.size.col + ". Received : " + m.size.row);

        Matrix temp = new Matrix(this.size.row, m.size.col);

        for (int i = 0; i < this.size.row; i++)
            for (int j = 0; j < m.size.col; j++)
                for (int k = 0; k < this.size.col; k++)
                    temp.matrix[i][j] = (matrix[i][k].multiply( m.matrix[k][j])).add(temp.matrix[i][j]);

        return temp;
    }

    /**
     * scalar multiplication of matrix
     * @param f
     * @return
     */
    public Matrix multiply(Fraction f){
        Matrix temp = new Matrix(this.size);

        for (int i = 0; i < this.size.row; i++)
            for (int j = 0; j < this.size.col; j++)
                temp.matrix[i][j] = matrix[i][j].multiply(f);

        return temp;
    }

    public Matrix multiply(int f){
        return multiply(new Fraction(f));
    }

    public Matrix multiply(double f){
        return multiply(new Fraction(f));
    }

    public Matrix multiply(String f){
        return multiply(new Fraction(f));
    }

    /**
     * perform row reduction on a matrix
     * @param mode reduced row echelon, or lower triangle reduction
     * @param doShowStep whether to display intermediate steps
     * @return a reduced matrix
     */
    public Matrix reduce(Reduction mode, boolean doShowStep) {
        showSteps(doShowStep, "Reducing this matrix: \n" + this.toString());
        //creates a copy of the matrix to be operated on
        Matrix temp = this.clone();
        //start from column 0
        for (int j = 0; j < temp.size.row; j++) {
            debug("Starting column " + j);
            //finds the first row that does not contain 0 in a given element.
            //reduced form is anchored on non-zero diagonal elements, thus search always begin with an element on the diagonal.
            int firstNonZeroRow = temp.firstNonZeroRow(j, j);
            //if no such element can be found, then move on to the next column
            if (firstNonZeroRow == -1) {
                showSteps(doShowStep, "Current column is simplified, moving to next column");
                continue;
            }
            if(firstNonZeroRow != j) {
                //swap the current row with first non zero element row.
                temp.rowOp_Swap(j, firstNonZeroRow);
                showSteps(doShowStep, "Swapping row " + j + " with row " + firstNonZeroRow + " : \n" + temp.toString());
            }
            //if rref is desired, finds the multiplier that would reduce the current row
            if(mode == Reduction.RREF) {
                Fraction divisor = temp.matrix[j][j];
                //divide row to reduce, so that the first non zero element is 1
                temp.rowOp_Divide(j, divisor);
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
                if(temp.matrix[i][j].equals(new Fraction(0))){
                    debug("Row already simplified, skipping to next row");
                    continue;
                }
                //compute the multiplier that would reduce the next row to zero in the current column
                Fraction multiplier = new Fraction(-1).multiply( temp.matrix[i][j].divide(temp.matrix[j][j]));
                //multiply the current row by multiplier
                temp.rowOp_Multiply(j, multiplier);
                //add the multiplied current row to the next row
                temp.rowOp_Add(j, i);
                //divide the current row by multiplier to its original reduced form
                temp.rowOp_Divide(j, multiplier);
                showSteps(doShowStep, "Multiplying row " + j + " by  " + multiplier  +  " and adding row " + j + " to row " + i + " : \n" + temp.toString());
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
     * @param from
     * @param to
     */
    private Matrix rowOp_Swap(int from, int to) {
        Fraction temp;
        for (int j = 0; j < this.size.col; j++) {
            temp = this.matrix[from][j];
            this.matrix[from][j] = this.matrix[to][j];
            this.matrix[to][j] = temp;
        }
        return this;
    }

    /**
     * multiply entire row by multiplier
     * @param row
     * @param multiplier
     */
    private Matrix rowOp_Multiply(int row, Fraction multiplier) {
        for (int j = 0; j < this.size.col; j++)
            this.matrix[row][j] = this.matrix[row][j].multiply(multiplier);
        return this;
    }

    /**
     * divide entire row by divisor
     * @param row
     * @param divisor
     */
    private Matrix rowOp_Divide(int row, Fraction divisor) {
        for (int j = 0; j < this.size.col; j++)
            this.matrix[row][j] = this.matrix[row][j].divide(divisor);
        return this;
    }

    /**
     * add row from to row to
     * @param from
     * @param to
     */
    private Matrix rowOp_Add(int from, int to) {
        for (int j = 0; j < this.size.col; j++)
            this.matrix[to][j] = this.matrix[to][j].add(this.matrix[from][j]);
        return this;
    }

    /**
     * returns the row index in a given column below a row, including that row, where the first non zero element is
     * @param col
     * @param startRow
     * @return returns -1 if no such element is found
     */
    private int firstNonZeroRow(int col, int startRow) {
        for (int i = startRow; i < this.matrix.length; i++)
            if (!this.matrix[i][col].equals(new Fraction(0)))
                return i;

        return -1;
    }

    /**
     * finds the determinant of a matrix by reducing it to lower triangular form and multiply the diagonal
     * additional methods may be added
     * @param doShowSteps
     * @return
     */
    public Fraction det(boolean doShowSteps){
        if(!this.isSquareMatrix())
            throw new MatrixOperationException("Operation undefined, determinant must be performed on a square matrix");

        Matrix temp = reduce(Reduction.UTRIANGLE, doShowSteps);
        Fraction det = new Fraction(1);

        for (int i = 0; i < temp.size.row; i++)
            det = det.multiply(temp.matrix[i][i]);
        showSteps(doShowSteps, "Multiplying diagonal to find determinant");

        return det;
    }

    /**
     * finds the inverse of a square matrix by reducing that matrix augmented with an identity matrix of the same size
     * @param doShowStep
     * @return
     */
    public Matrix inverse(boolean doShowStep){
        //throws unchecked exception if the matrix is not a square matrix
        if(!this.isSquareMatrix())
            throw new MatrixOperationException("Operation undefined, inverse must be performed on a square matrix");

        //first augment the matrix with identity matrix of same size
        Matrix aug = this.augment(this.identityMatrix());
        //then reduce till the left most largest square elements are completely reduced
        Matrix inverse = aug.reduce(Reduction.RREF, doShowStep);

        return inverse.subMatrix(0, this.size.row, this.size.row, this.size.row * 2);
    }

    public ArrayList<Matrix> factor(boolean doShowStep){
        showSteps(doShowStep, "Factoring this matrix : +\n" + this.toString());
        ArrayList<Matrix> elementaries = new ArrayList<>();
        //creates a copy of the matrix to be operated on
        Matrix temp = this.clone();
        //start from column 0
        Matrix element;
        for (int j = 0; j < temp.size.row; j++) {
            debug("Starting column " + j);
            //finds the first row that does not contain 0 in a given element.
            //reduced form is anchored on non-zero diagonal elements, thus search always begin with an element on the diagonal.
            int firstNonZeroRow = temp.firstNonZeroRow(j, j);
            //if no such element can be found, then move on to the next column
            if (firstNonZeroRow == -1) {
                showSteps(doShowStep, "Current column is simplified, moving to next column");
                continue;
            }
            if(firstNonZeroRow != j) {
                //swap the current row with first non zero element row.
                temp.rowOp_Swap(j, firstNonZeroRow);
                element = temp.identityMatrix().rowOp_Swap(j, firstNonZeroRow);
                showSteps(doShowStep, "Swapping row " + j + " with row " + firstNonZeroRow + " : \n");
                showSteps(doShowStep, "New elementary matrix : \n" + element.toString());
                elementaries.add(element);
            }

            Fraction divisor = temp.matrix[j][j];
            if(!divisor.equals(new Fraction(1))) {
                //divide row to reduce, so that the first non zero element is 1
                temp.rowOp_Divide(j, divisor);
                element = temp.identityMatrix().rowOp_Divide(j, divisor);
                showSteps(doShowStep, "Dividing row " + j + " by " + divisor + " :\n" + temp.toString());
                showSteps(doShowStep, "New elementary matrix : \n" + element.toString());
                elementaries.add(element);
            }

            for (int i = 0; i < temp.size.row; i++) {
                debug("Starting row " + i);
                //if the current row is compared to itself, skip to the next row
                if (i == j) {
                    debug("Current row, skipping to next row");
                    continue;
                }
                if(temp.matrix[i][j].equals(new Fraction(0))){
                    debug("Row already simplified, skipping to next row");
                    continue;
                }
                element = temp.identityMatrix();
                //compute the multiplier that would reduce the next row to zero in the current column
                Fraction multiplier = new Fraction(-1).multiply( temp.matrix[i][j].divide(temp.matrix[j][j]));

                //multiply the current row by multiplier
                temp.rowOp_Multiply(j, multiplier);
                element.rowOp_Multiply(j, multiplier);
                showSteps(doShowStep, "Multiplying row " + j + " by " + multiplier + " : \n" + temp.toString());

                //add the multiplied current row to the next row
                temp.rowOp_Add(j, i);
                element.rowOp_Add(j,i);
                showSteps(doShowStep, "Adding row " + j + " to row " + i + " : \n" + temp.toString());

                //divide the current row by multiplier to its original reduced form
                temp.rowOp_Divide(j, multiplier);
                element.rowOp_Divide(j,multiplier);
                showSteps(doShowStep, "Dividing row " + j + " by " + multiplier + " :\n" + temp.toString());
                showSteps(doShowStep, "New elementary matrix : \n" + element.toString());

                elementaries.add(element);
                debug("row " + i + " is Complete");
            }
            debug("column " + j + " is Complete");
        }

        ArrayList<Matrix> inverses = new ArrayList<>();
        for (int i = 0; i < elementaries.size(); i++)
            inverses.add(elementaries.get(i).inverse(false));

        System.out.println("Elementary matrices factorization of matrix : \n" + "\n" + printQueue(inverses));

        return inverses;
    }

    private static String printQueue(ArrayList<Matrix> list){

        String str = "";
        int[] longests = new int[list.size()];
        for (int i = 0; i < longests.length; i++)
            longests[i] = list.get(i).longestNum() + 1;

        for (int i = 0; i < list.get(0).size.row; i++) {
            String line = "";
            for (int k = 0; k < list.size(); k++) {
                String section = "";
                int length = longests[k];
                section += "\u239c";
                for (int j = 0; j < list.get(k).size.col; j++)
                    section += String.format("%"+ length + "s", list.get(k).matrix[i][j].toString());
                section += "\u239f";
                if (i == 0)
                    section = "\u23a1" + section.substring(1, section.length() - 1) + "\u23a4";
                if (i == list.get(k).size.row - 1)
                    section = "\u23a3" + section.substring(1, section.length() - 1) + "\u23a6";
                line += section;
            }
            str += line + "\n";
        }

        return str;
    }

    /**
     * prints steps to a solution if specified
     * @param doShowStep whether to show steps
     * @param step steps to be printed
     */
    private void showSteps(boolean doShowStep, String step) {
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

