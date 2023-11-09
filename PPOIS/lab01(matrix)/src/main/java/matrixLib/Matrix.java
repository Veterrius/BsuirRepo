package matrixLib;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class Matrix {

    private double[][] contents;
    private int rows;
    private int columns;
    private MatrixType type;

    public Matrix() {
        this.contents = new double[1][1];
        this.rows = 1;
        this.columns = 1;
        this.type = MatrixType.ZERO;
    }

    public Matrix(double[][] contents, int rows, int columns) {
        this.contents = contents;
        this.rows = rows;
        this.columns = columns;
        this.type = MatrixType.UNTYPED;
    }

    public Matrix(int rows, int columns) {
        this.rows = rows;
        this.columns = columns;
        this.contents = new double[rows][columns];
        this.type = MatrixType.UNTYPED;
    }

    /**
     * This method creates a matrix from a text file. The format of that file should be:
     *
     * <br>  M N
     * <br> X1 X2 X3 X4 ... Xi <br>
     * Where M,N are the numbers of rows and columns, and X1, .., Xi are the double
     * numbers which are the matrix's contents. (i = M*N)
     *
     * @param filePath the path to file which is used for Matrix creation
     * @return new Matrix
     * @throws MatrixException if the path/format of the file is not correct
     */
    public static Matrix createFromFile(String filePath) throws MatrixException {
        Path mtxFilePath = Paths.get(filePath);
        List<String> mtxFileLines;
        try {
            mtxFileLines = Files.readAllLines(mtxFilePath);
        } catch (IOException e) {
            throw new MatrixException("Wrong file path");
        }
        Matrix newMtx = MatrixStringParser.parse(mtxFileLines);
        newMtx.assignType();
        return newMtx;
    }

    /**
     * This method changes the number of rows and columns in Matrix,
     * resetting the contents
     *
     * @param newRows is the new number of Rows in Matrix
     * @param newColumns is the new numer of Columns in Matrix
     * @throws MatrixException when the new numbers of rows and columns are {@literal <}=0
     */
    public void changeRowsAndColumns(int newRows, int newColumns) throws MatrixException {
        if (newRows <= 0 || newColumns <= 0) throw new MatrixException("Rows' and Columns' numbers cannot be < 0");
        this.rows = newRows;
        this.columns = newColumns;
        this.contents = new double[rows][columns];
        this.assignType();
    }

    /**
     * This method extracts a submatrix from the Matrix.
     * Extraction is done from the top-left angle of the matrix.
     *
     * @param subRows is the number of Rows in submatrix
     * @param subColumns is the number of Columns in submatrix
     * @return a submatrix which is a new Matrix object
     * @throws MatrixException when the numbers of submatrix's rows and columns are {@literal <}=0
     */
    public Matrix extractSubmatrix(int subRows, int subColumns) throws MatrixException {
        if (subRows > this.rows || subColumns > this.columns
        || subRows <= 0 || subColumns <= 0) {
            throw new MatrixException("Wrong size of the submatrix");
        }
        double[][] subContents = new double[subRows][subColumns];
        for (int i = 0; i < subRows; i++) {

            for (int j = 0; j < subColumns; j++) {
                subContents[i][j] = this.contents[i][j];
            }
        }
        return new Matrix(subContents, subRows, subColumns);
    }

    /**
     * This method transposes a Matrix, changing it's contents
     */
    public void transpose() {
        double[][] oldContents = this.contents;
        int temp = this.columns;
        this.columns = this.rows;
        this.rows = temp;

        this.contents = new double[this.rows][this.columns];
        for (int i = 0; i < this.rows; i++) {
            double[] buf = new double[this.columns];
            for (int j = 0; j < this.columns; j++) {
                buf[j] = oldContents[j][i];
            }
            this.contents[i] = buf;
        }
    }

    /**
     * This method assigns a type to a matrix.
     * @see MatrixType for a list of matrix types available.
     */
    public void assignType() {
        MatrixTypeAssigner.assignType(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Matrix matrix = (Matrix) o;
        return rows == matrix.rows &&
                columns == matrix.columns && Arrays.deepEquals(contents, matrix.contents);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Matrix{type=").append(this.getType()).
                append(", rows=").append(rows).
                append(", columns=").append(columns).
                append(", contents:\n");
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                sb.append(contents[i][j]).append("  ");
            }
            sb.append('\n');
        }
        sb.append('}');
        return sb.toString();
    }


    public double[][] getContents() {
        return contents;
    }

    public void setContents(double[][] contents) {
        this.contents = contents;
    }

    public int getRows() {
        return rows;
    }

    public int getColumns() {
        return columns;
    }

    public void setType(MatrixType type) {
        this.type = type;
    }

    public MatrixType getType() {
        return type;
    }
}
