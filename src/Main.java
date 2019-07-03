import java.util.SortedMap;

public class Main {
    public static void main(String[] args) {
        //construct new matrices with 2d double array
        double[][] arrA = {{-1,1,-2},{2,0,1}};
        double[][] arrB = {{-3,0},{1,2},{1,-2}};
        //call methods on Matrix object, ex m.inverse(true)
        Matrix A = new Matrix(arrA);
        Matrix B = new Matrix(arrB);
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

        System.out.println(A.multiply(B));
        System.out.println(A.multiply(B).transpose());
        System.out.println(A.transpose());
        System.out.println(B.transpose());
        System.out.println(B.transpose().multiply(A.transpose()));
    }

}
