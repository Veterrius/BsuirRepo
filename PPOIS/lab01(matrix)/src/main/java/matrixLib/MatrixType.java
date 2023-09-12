package matrixLib;

/**
 * This enum contains types for a {@link Matrix} class when type assignment is used.
 * Matrix is considered UNTYPED when any other type in this Enum can't be assigned
 */
public enum MatrixType {
    SQUARE, DIAGONAL, ZERO, UNIT, SYMMETRIC, LOWER_TRIANGULAR, UPPER_TRIANGULAR, UNTYPED
}
