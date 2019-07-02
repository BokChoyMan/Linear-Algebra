public class Main {
    public static void main(String[] args) {
        //construct new matrices with 2d double array
        double[][] arr = {{-2,4},{5,-14}};
        //call methods on Matrix object, ex m.inverse(true)
        Matrix m = new Matrix(arr);
        /*
        Supported operations:
            -addition
            -multiplication
            -reduce to reduced row echelon or upper triangular form with work
            -determinants with work
            -inverses with work
            -writing matrix as product of elementary matrices
            -transpose
            -different basic row operations
        Answers are given in reduced fraction form.
         */
        m.factor(true);
    }

}
