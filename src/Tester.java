public class Tester {
    public static void main(String[] args) {
        int row = Matrix.prompt("Rows");
        int col = Matrix.prompt("Columns");
        Matrix m = new Matrix(row,col);
        System.out.println(m);
    }
}
