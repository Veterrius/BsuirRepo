import matrixLib.Matrix;
import matrixLib.MatrixType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MatrixTypeAssigningTest {

    private static Matrix unitMatrix;
    private static Matrix diagonalMatrix;
    private static Matrix untypedMatrix;
    private static Matrix symmetricMatrix;
    private static Matrix upperTriangularMatrix;
    private static Matrix lowerTriangularMatrix;

    @BeforeAll
    static void setUp() {
        unitMatrix = new Matrix(new double[][]{{1,0,0},{0,1,0},{0,0,1}},3,3);
        diagonalMatrix = new Matrix(new double[][]{{2,0,0},{0,1,0},{0,0,6}},3,3);
        untypedMatrix = new Matrix(new double[][]{{2,0},{1,0},{0,1}},3,2);
        symmetricMatrix = new Matrix(new double[][]{{2,3,6},{3,4,5},{6,5,9}},3,3);
        upperTriangularMatrix = new Matrix(new double[][]{{2,2,2},{0,1,1},{0,0,1}},3,3);
        lowerTriangularMatrix = new Matrix(new double[][]{{2,0,0},{2,1,0},{2,3,1}},3,3);
    }

    @Test
    public void assignTypeTest_ZERO() {
        Matrix tm = new Matrix();
        tm.assignType();
        MatrixType expectedType = MatrixType.ZERO;
        MatrixType actualType = tm.getType();
        assertEquals(expectedType, actualType);
    }

    @Test
    public void assignTypeTest_SQUARE() {
        Matrix tm = new Matrix(new double[][]{{1,2},{3,4}},2,2);
        tm.assignType();
        MatrixType expectedType = MatrixType.SQUARE;
        MatrixType actualType = tm.getType();
        assertEquals(expectedType, actualType);
    }

    @Test
    public void assignTypeTest_SYMMETRIC() {
        symmetricMatrix.assignType();
        MatrixType expectedType = MatrixType.SYMMETRIC;
        MatrixType actualType = symmetricMatrix.getType();
        assertEquals(expectedType, actualType);
    }

    @Test
    public void assignTypeTest_UNTYPED() {
        untypedMatrix.assignType();
        MatrixType expectedType = MatrixType.UNTYPED;
        MatrixType actualType = untypedMatrix.getType();
        assertEquals(expectedType, actualType);
    }

    @Test
    public void assignTypeTest_LOWER_TRIANGULAR() {
        lowerTriangularMatrix.assignType();
        MatrixType expectedType = MatrixType.LOWER_TRIANGULAR;
        MatrixType actualType = lowerTriangularMatrix.getType();
        assertEquals(expectedType, actualType);
    }

    @Test
    public void assignTypeTest_UPPER_TRIANGULAR() {
        upperTriangularMatrix.assignType();
        MatrixType expectedType = MatrixType.UPPER_TRIANGULAR;
        MatrixType actualType = upperTriangularMatrix.getType();
        assertEquals(expectedType, actualType);
    }

    @Test
    public void assignTypeTest_DIAGONAL() {
        diagonalMatrix.assignType();
        MatrixType expectedType = MatrixType.DIAGONAL;
        MatrixType actualType = diagonalMatrix.getType();
        assertEquals(expectedType, actualType);
    }

    @Test
    public void assignTypeTest_UNIT() {
        unitMatrix.assignType();
        MatrixType expectedType = MatrixType.UNIT;
        MatrixType actualType = unitMatrix.getType();
        assertEquals(expectedType, actualType);
    }
}
