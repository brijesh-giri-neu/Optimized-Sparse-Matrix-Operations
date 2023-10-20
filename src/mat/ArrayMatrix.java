package mat;

/**
 * This class implements a 2D matrix of numbers using a 2D array. This implementation is efficient
 * if most of the entries of the matrix are not zero. But this wastes a lot of space and computing
 * time if most of its entries are zero.
 */
public class ArrayMatrix extends AbstractMatrix {

  private float[][] mat;

  /**
   * Constructs a new matrix of the given dimensions. All entries of this matrix are by default, 0
   *
   * @param size the number of rows and columns in this matrix
   * @throws IllegalArgumentException if the size is a non-positive number
   */
  public ArrayMatrix(int size) throws IllegalArgumentException {
    if (size < 0) {
      throw new IllegalArgumentException("The size of an array matrix cannot be non-positive");
    }

    mat = new float[size][size];
    for (int i = 0; i < mat.length; i += 1) {
      for (int j = 0; j < mat[i].length; j += 1) {
        mat[i][j] = 0.0f;
      }
    }
  }

  private ArrayMatrix(float[][] mat) throws IllegalArgumentException {
    int numRows = mat.length;
    int numCols = mat[0].length;
    //ensuring all rows have the same number of columns
    for (int i = 0; i < numRows; i += 1) {
      if (mat[i].length != numCols) {
        throw new IllegalArgumentException("Unequal number of columns");
      }
    }
    this.mat = new float[numRows][numCols];
    for (int i = 0; i < numRows; i += 1) {
      for (int j = 0; j < numCols; j += 1) {
        this.mat[i][j] = mat[i][j];
      }
    }
  }


  @Override
  public void setIdentity() {
    for (int i = 0; i < mat.length; i += 1) {
      for (int j = 0; j < mat.length; j += 1) {
        if (i == j) {
          mat[i][j] = 1;
        } else {
          mat[i][j] = 0;
        }
      }
    }
  }

  @Override
  public void set(int i, int j, float value) throws IllegalArgumentException {
    if ((i < 0) || (i >= mat.length)) {
      throw new IllegalArgumentException(
          "Row number in set cannot be beyond the bounds of the matrix");
    }

    if ((j < 0) || (j >= mat[i].length)) {
      throw new IllegalArgumentException(
          "Column number in set cannot be beyond the bounds of the matrix");
    }
    mat[i][j] = value;
  }

  @Override
  public float get(int i, int j) throws IllegalArgumentException {
    if ((i < 0) || (i >= mat.length)) {
      throw new IllegalArgumentException(
          "Row number in get cannot be beyond the bounds of the matrix");
    }

    if ((j < 0) || (j >= mat[i].length)) {
      throw new IllegalArgumentException(
          "Column number in get cannot be beyond the bounds of the matrix");
    }
    return mat[i][j];
  }

  @Override
  public SquareMatrix add(SquareMatrix other) throws IllegalArgumentException {
    if (this.size() != other.size()) {
      throw new IllegalArgumentException("The dimensions of the two matrices do not match "
          + "and therefore cannot be added together");
    }

    // assumption - all implementations extend AbstractMatrix
    return ((AbstractMatrix) other).addArrayMatrix(this);
  }

  @Override
  public SquareMatrix postmul(SquareMatrix other) throws IllegalArgumentException {
    if (this.size() != other.size()) {
      throw new IllegalArgumentException(
          "The size of this matrix is not the same as the size of the other matrix,"
              + " hence they cannot be multiplied together");
    }

    // assumption - all implementations extend AbstractMatrix
    // need to achieve this * other
    return ((AbstractMatrix) other).premulArrayMatrix(this);


  }

  @Override
  public SquareMatrix premul(SquareMatrix other) throws IllegalArgumentException {
    if (this.size() != other.size()) {
      throw new IllegalArgumentException(
          "The size of this matrix is not the same as the size of the other matrix,"
              + " hence they cannot be multiplied together");
    }

    // assumption - all implementations extend AbstractMatrix
    // need to achieve other * this
    return ((AbstractMatrix) other).postmulArrayMatrix(this);
  }

  @Override
  public int size() {
    return mat.length;
  }

  @Override
  protected SquareMatrix addSparseMatrix(SparseMatrix other) {
    return other.addArrayMatrix(this);
  }

  @Override
  protected SquareMatrix postmulSparseMatrix(SparseMatrix other) {
    return other.premulArrayMatrix(this);
  }

  @Override
  protected SquareMatrix premulSparseMatrix(SparseMatrix other) {
    return other.postmulArrayMatrix(this);
  }

  @Override
  protected SquareMatrix addArrayMatrix(ArrayMatrix other) {
    float[][] result = new float[this.size()][this.size()];
    for (int i = 0; i < this.size(); i += 1) {
      for (int j = 0; j < size(); j += 1) {
        result[i][j] = this.mat[i][j] + other.get(i, j);
      }
    }
    return new ArrayMatrix(result);
  }

  @Override
  protected SquareMatrix postmulArrayMatrix(ArrayMatrix other) {
    float[][] result = new float[this.size()][other.size()];
    for (int i = 0; i < this.size(); i += 1) {
      for (int j = 0; j < other.size(); j += 1) {
        result[i][j] = 0;
        for (int k = 0; k < this.size(); k += 1) {
          result[i][j] += this.mat[i][k] * other.get(k, j);
        }
      }
    }
    return new ArrayMatrix(result);
  }

  @Override
  protected SquareMatrix premulArrayMatrix(ArrayMatrix other) {
    float[][] result = new float[this.size()][other.size()];
    for (int i = 0; i < other.size(); i += 1) {
      for (int j = 0; j < this.size(); j += 1) {
        result[i][j] = 0;
        for (int k = 0; k < this.size(); k += 1) {
          result[i][j] += other.get(i, k) * this.mat[k][j];
        }
      }
    }
    return new ArrayMatrix(result);
  }
}
