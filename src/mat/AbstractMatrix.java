package mat;

/**
 * This abstract class represents the common aspects between different implementations of a 2D
 * square matrix.
 */
abstract class AbstractMatrix implements SquareMatrix {

  // Abstract methods for mixed case. SparseMatrix being provided an ArrayMatrix

  /**
   * This implementation is good for adding a SparseMatrix with a SquareMatrix.
   *
   * @param other a sparse matrix
   * @return a square matrix
   */
  abstract protected SquareMatrix addSparseMatrix(SparseMatrix other);

  /**
   * This implementation is good for post-multiplying a SparseMatrix with an SquareMatrix i.e.
   * SquareMatrix * SparseMatrix.
   *
   * @param other a sparse matrix
   * @return a square matrix
   */
  abstract protected SquareMatrix postmulSparseMatrix(SparseMatrix other);

  /**
   * This implementation is good for pre-multiplying a SparseMatrix with an SquareMatrix i.e.
   * SparseMatrix * SquareMatrix.
   *
   * @param other a sparse matrix
   * @return a square matrix
   */
  abstract protected SquareMatrix premulSparseMatrix(SparseMatrix other);

  /**
   * Default implementation. This implementation is good for adding an ArrayMatrix with a
   * SquareMatrix.
   *
   * @param other an array matrix
   * @return a square matrix
   */
  abstract protected SquareMatrix addArrayMatrix(ArrayMatrix other);

  /**
   * Default implementation. This implementation is good for post-multiplying an ArrayMatrix with a
   * SquareMatrix.
   *
   * @param other an array matrix
   * @return a square matrix
   */
  abstract protected SquareMatrix postmulArrayMatrix(ArrayMatrix other);

  /**
   * This implementation is good for pre-multiplying an ArrayMatrix with a SquareMatrix.
   *
   * @param other an array matrix
   * @return a square matrix
   */
  abstract protected SquareMatrix premulArrayMatrix(ArrayMatrix other);
}
