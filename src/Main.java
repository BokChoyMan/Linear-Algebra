public class Main {
    public static void main(String[] args) {
        double[][] temp = {{2,-3,10,-2},{1,-2,3,-2},{-1,3,1,4}};
        Matrix m = new Matrix(temp);
        System.out.println(m);
        System.out.println(Matrix.rref(m,true));
    }

}
