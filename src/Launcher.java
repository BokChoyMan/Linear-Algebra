public class Launcher {
    public static void main(String[] args) {
        int row = Matrix.prompt("Rows");
        int col = Matrix.prompt("Columns");
        Matrix m = new Matrix(row,col);
        m.initialize();
        System.out.println(m);
    }
}
