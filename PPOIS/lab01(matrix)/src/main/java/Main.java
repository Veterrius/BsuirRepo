import matrixLib.Matrix;
import matrixLib.MatrixException;

public class Main {

    public static void main(String[] args) {
        Matrix matrix = new Matrix();
        try {
            matrix = Matrix.createFromFile("D:\\Uni\\PPOIS\\mt.txt");
        } catch (MatrixException e) {
            e.printStackTrace();
        }

        System.out.println(matrix);
    }
}
