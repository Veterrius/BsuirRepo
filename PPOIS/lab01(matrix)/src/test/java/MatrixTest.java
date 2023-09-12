import matrixLib.Matrix;
import matrixLib.MatrixException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MatrixTest {

    private static Matrix testMatrix1;
    private static Matrix testMatrix2;
    private static String testFilePath;
    private static String wrongTestFiePath;

    @BeforeAll
    static void setUp() {
        double[][] testContents1 = new double[][]{{1, 1, 1}, {2, 2, 2}, {3, 3, 3}};
        double[][] testContents2 = new double[3][3];
        testFilePath = "D:\\Uni\\PPOIS\\lab01(matrix)\\src\\test\\java\\testRes\\test.txt";
        wrongTestFiePath = "D:\\Uni\\PPOIS\\lab01(matrix)\\src\\test\\java\\testRes\\fTest.txt";
        testMatrix1 = new Matrix(testContents1, 3,3);
        testMatrix2 = new Matrix(testContents2, 3,3);
    }

    @Test
    public void extractSubmatrixTest_Success() throws MatrixException {
        Matrix expectedSub = new Matrix(new double[][]{{1,1},{2,2}}, 2,2);
        Matrix actualSub = testMatrix1.extractSubmatrix(2,2);
        assertEquals(expectedSub, actualSub);
    }

    @Test
    public void extractSubmatrixTest_MatrixException() {
        Throwable thrown = assertThrows(MatrixException.class, ()->testMatrix2.extractSubmatrix(-9,10));
        assertEquals("Wrong size of the submatrix", thrown.getMessage());
    }

    @Test
    public void extractSubmatrixTest_NOT_NULL() throws MatrixException {
        Matrix subNotNull = testMatrix2.extractSubmatrix(0,0);
        assertNotNull(subNotNull);
    }


    @Test
    public void changeRowsAndColumnsTest_Success() throws MatrixException {
        Matrix expectedChanged = new Matrix(new double[1][2], 1,2);
        Matrix actualChanged = testMatrix2;
        actualChanged.changeRowsAndColumns(1,2);
        assertEquals(expectedChanged, actualChanged);
    }

    @Test
    public void changeRowsAndColumnsTest_MatrixException() {
        Throwable thrown = assertThrows(MatrixException.class, ()->testMatrix2.changeRowsAndColumns(-5,6));
        assertEquals("Rows' and Columns' numbers cannot be < 0", thrown.getMessage());
    }

    @Test
    public void transposeTest_Success() {
        Matrix expectedTransposed = new Matrix(new double[][]{{1,2,3},
                {1,2,3},{1,2,3}}, 3,3);
        Matrix actualTransposed = testMatrix1;
        actualTransposed.transpose();
        assertEquals(expectedTransposed, actualTransposed);
    }

    @Test
    public void createFromFileTest_Success() throws MatrixException {
        Matrix expectedCreated = testMatrix1;
        Matrix actualCreated = Matrix.createFromFile(testFilePath);
        assertEquals(expectedCreated, actualCreated);
    }

    @Test
    public void createFromFileTest_MatrixException_WrongPath() {
        Throwable thrown = assertThrows(MatrixException.class, ()->Matrix.createFromFile(testFilePath+"f"));
        assertEquals("Wrong file path", thrown.getMessage());
    }

    @Test
    public void createFromFileTest_MatrixException_WrongFormat() {
        Throwable thrown = assertThrows(MatrixException.class, ()->Matrix.createFromFile(wrongTestFiePath));
        assertEquals("Wrong matrix file format", thrown.getMessage());
    }
}
