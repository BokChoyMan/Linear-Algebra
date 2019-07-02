import java.util.InputMismatchException;
import java.util.Scanner;

public class Matrix {
    private int row;
    private int col;
    private int posX;
    private int posY;
    private static final boolean debugging = false;
    private int[][] matrix;

    public Matrix(int row, int col){
        this.row = row;
        this.col = col;
        matrix = new int[row][col];
        posX = 0;
        posY = 0;
    }

    public void initialize(int entity){
        boolean filled = false;
        row = input("Rows");
        col = input("Columns");
        while(!filled){
            matrix[posX][posY]= input(posX, posY);
            posY ++;
            posX ++;
            if(posY == row || posX == row){

            }
        }
    }

    private static int input(int entityRow, int entityCol){
        Scanner in = new Scanner(System.in);
        try{
            System.out.print("Please Enter entity at " + "(" + entityRow + ", " + entityCol + ")" + " : ");
            return in.nextInt();
        } catch(InputMismatchException e){
            System.out.println("Invalid input");
            return input(entityRow, entityCol);
        }
    }

    private static int input(String msg){
        Scanner in = new Scanner(System.in);
        try{
            System.out.print("Please Enter Number of" + msg + " : ");
            return in.nextInt();
        } catch(InputMismatchException e){
            System.out.println("Invalid input");
            return input(msg);
        }
    }

    @Override
    public String toString(){
        String str = "";
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                str += matrix[row][col] + " ";
            }
            str += "\n";
        }
        return str;
    }

    private static void debug(String str){
        if(debugging)
            System.out.println(str);
    }



}
