public class Main {
    public static void main(String[] args) {
        double[][] temp = {{1,1,1,1,0,0},{3,5,4,0,1,0},{3,6,5,0,0,1}};
        Matrix m = new Matrix(temp);
        System.out.println(m);
        System.out.println(Matrix.rref(m,true));
    }

}
