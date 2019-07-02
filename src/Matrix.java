import java.util.InputMismatchException;
import java.util.Scanner;

public class Matrix {
    private int SizeRow;
    private int SizeCol;

    private static final boolean debugging = false;
    private int[][] matrix;

    public Matrix(int SizeRow, int SizeCol){
        this.SizeRow = SizeRow;
        this.SizeCol = SizeCol;
        matrix = new int[SizeRow][SizeCol];
    }

    public static int prompt(String size){
        int s = input(size);
        return s;
    }

    private static int input(String msg){
        Scanner in = new Scanner(System.in);
        try{
            System.out.print("Please Enter Number of " + msg + " : ");
            return in.nextInt();
        } catch(InputMismatchException e){
            System.out.println("Invalid input");
            return input(msg);
        }
    }

    public void initialize(){
        for (int i = 0; i < SizeRow + 1; i++) {
            for (int j = 0; j < SizeCol + 1; j++)
                matrix[i][j] = input(i, j);
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

    @Override
    public String toString(){
        String str = "";
        for (int i = 0; i < SizeRow; i++) {
            for (int j = 0; j < SizeCol; j++) {
                if(i == 0 && j == 0)
                    str += "⎡ ";
                else if(i == SizeRow - 1 && j == 0)
                    str += "⎣ ";
                else if(j == 0 && i != SizeRow - 1)
                    str += "⎜ ";

                str += matrix[i][j] + " ";

                if (i == 0 && j == SizeCol - 1)
                    str += "⎤ \n";
                else if (i == SizeRow -1 && j == SizeCol-1)
                    str += "⎦";
            }
            if(i != 0 && i != SizeRow-1){
                str += "⎟\n";
            }
        }
        return str;
    }

    private static void debug(String str){
        if(debugging)
            System.out.println(str);
    }

}

//⎡⎤
//⎜⎟
//⎣⎦