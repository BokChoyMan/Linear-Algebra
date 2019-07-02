public class Main {
    public static void main(String[] args) {
        double[][] arr = {{1,4,2},{3,-1,-1},{10,-5,3}};
        Matrix m = new Matrix(arr);
        System.out.println(m);
        System.out.println(Matrix.reduce(m, Matrix.Reduction.UTRIANGLE, false));
        System.out.println(Matrix.det(m,false));
    }

}
