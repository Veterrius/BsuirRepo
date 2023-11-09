package matrixLib;

class MatrixTypeAssigner {

    public static void assignType(Matrix matrix) {
        if (isZero(matrix)) {
            matrix.setType(MatrixType.ZERO);
            return;
        }
        if (matrix.getColumns() == matrix.getRows()) {
            matrix.setType(squareTypeCheck(matrix));
        }
    }

    private static MatrixType squareTypeCheck(Matrix matrix) {
        MatrixType squareType = MatrixType.SQUARE;
        if (isSymmetric(matrix) || isZero(matrix)) {
            squareType = MatrixType.SYMMETRIC;
        }
        if (isUpperTriangular(matrix)) squareType = MatrixType.UPPER_TRIANGULAR;
        if (isLowerTriangular(matrix)) squareType = MatrixType.LOWER_TRIANGULAR;
        if (isUpperTriangular(matrix) && isLowerTriangular(matrix)) squareType = unitOrDiagonalCheck(matrix);
        return squareType;
    }

    private static boolean isUpperTriangular(Matrix matrix) {
        for (int i = 1; i < matrix.getRows(); i++) {
            for (int j = 0; j < i; j++) {
                if (matrix.getContents()[i][j] != 0) return false;
            }
        }
        return true;
    }

    private static boolean isLowerTriangular(Matrix matrix) {
        for (int i = 0; i < matrix.getRows(); i++) {
            for (int j = matrix.getRows() -1-i; j > 0; j--) {
                if (matrix.getContents()[i][j] != 0 && i!=j) return false;
            }
        }
        return true;
    }

    private static MatrixType unitOrDiagonalCheck(Matrix matrix) {
        MatrixType type = MatrixType.SQUARE;
        for (int i = 0; i < matrix.getRows(); i++) {
            switch((int) matrix.getContents()[i][i]) {
                case 1 :
                    type = MatrixType.UNIT;
                    break;
                case 0 :
                    type = MatrixType.SQUARE;
                    return type;
                default:
                    type = MatrixType.DIAGONAL;
                    return type;
            }
        }
        return type;
    }


    private static boolean isZero(Matrix matrix) {
        for (int i = 0; i < matrix.getRows(); i++) {
            for (int j = 0; j < matrix.getColumns(); j++) {
                if (matrix.getContents()[i][j] != 0) return false;
            }
        }
        return true;
    }

    private static boolean isSymmetric(Matrix matrix) {
        Matrix transposedMatrix = new Matrix(matrix.getContents(), matrix.getRows(), matrix.getColumns());
        transposedMatrix.transpose();
        return matrix.equals(transposedMatrix);
    }
    
}
