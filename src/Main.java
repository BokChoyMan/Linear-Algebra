public class Main {
    public static void main(String[] args) {
        double[][] arr = {{1,4,2},{3,-1,-1},{10,-5,3}};
        Matrix m = new Matrix(arr);
        Matrix inv = Matrix.inverse(m, true);
        System.out.println(inv);
    }

}
