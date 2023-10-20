package mat;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents a 2D matrix of numbers using a 2D doubly linked list. It uses the Sentinel
 * representation i.e. a circular implementation of a linked list.
 */
public class SparseMatrix extends AbstractMatrix {

  private List<RowSentinel<Float>> rows;
  private List<ColumnSentinel<Float>> cols;

  /**
   * Constructs a new matrix of the given dimensions. All entries of this matrix are by default, 0.
   *
   * @param size the number of rows and columns in this matrix
   * @throws IllegalArgumentException if the size is a non-positive number
   */
  public SparseMatrix(int size) throws IllegalArgumentException {
    if (size < 0) {
      throw new IllegalArgumentException("The size of a matrix cannot be non-positive");
    }

    this.rows = new ArrayList<RowSentinel<Float>>(size);
    this.cols = new ArrayList<ColumnSentinel<Float>>(size);
    initializeSentinels(size);
  }

  // initialize the sentinels of a new matrix.
  private void initializeSentinels(int size) {
    for (int i = 0; i < size; i++) {
      this.rows.add(new RowSentinel<Float>());
      this.cols.add(new ColumnSentinel<Float>());
    }
  }

  @Override
  public void setIdentity() {
    // rest the sentinels to effectively create a new matrix
    int size = this.rows.size();
    this.rows = new ArrayList<RowSentinel<Float>>(size);
    this.cols = new ArrayList<ColumnSentinel<Float>>(size);

    initializeSentinels(size);

    for (int i = 0; i < this.rows.size(); i++) {
      // In Identity matrix i==j
      set(i, i, 1f);
    }
  }

  @Override
  public void set(int i, int j, float value) throws IllegalArgumentException {
    if ((i < 0) || (i >= this.rows.size())) {
      throw new IllegalArgumentException(
          "Row number in get cannot be beyond the bounds of the matrix");
    }

    if ((j < 0) || (j >= this.cols.size())) {
      throw new IllegalArgumentException(
          "Column number in get cannot be beyond the bounds of the matrix");
    }

    RowSentinel<Float> headRow = this.rows.get(i);
    ColumnSentinel<Float> headCol = this.cols.get(j);

    if (value == 0f) {
      headRow.removeNodeIfExists(i, j);
    } else {
      // add a node by getting the links
      List<AbstractNode<Float>> leftRightLinks = headRow.getHorizontalLinks(i, j);
      List<AbstractNode<Float>> topBottomLinks = headCol.getVerticalLinks(i, j);

      // can call addNode from any sentinel. since it has all the links passed.
      headRow.addNode(value, i, j, leftRightLinks.get(0), leftRightLinks.get(1),
          topBottomLinks.get(0), topBottomLinks.get(1));
    }
  }

  @Override
  public float get(int i, int j) throws IllegalArgumentException {
    if ((i < 0) || (i >= this.rows.size())) {
      throw new IllegalArgumentException(
          "Row number in get cannot be beyond the bounds of the matrix");
    }

    if ((j < 0) || (j >= this.cols.size())) {
      throw new IllegalArgumentException(
          "Column number in get cannot be beyond the bounds of the matrix");
    }

    // compromise - I always pick the row sentinel to access a node
    // Instead of  maintaining the count of nodes in each sentinel
    // and deciding between row/column sentinel based on that.
    RowSentinel<Float> head = this.rows.get(i);

    Float nullableFloat = head.get(i, j, this.rows.size());
    return nullableFloat == null ? 0f : nullableFloat;
  }

  // This implementation assumes the elements of a row in a SparseMatrix are in sorted order
  @Override
  public SquareMatrix add(SquareMatrix other) throws IllegalArgumentException {
    if (this.size() != other.size()) {
      throw new IllegalArgumentException("The dimensions of the two matrices do not match "
          + "and therefore cannot be added together");
    }

    // assumption - all implementations extend AbstractMatrix
    return ((AbstractMatrix) other).addSparseMatrix(this);
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
    return ((AbstractMatrix) other).postmulSparseMatrix(this);
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
    return ((AbstractMatrix) other).premulSparseMatrix(this);
  }

  @Override
  public int size() {
    return this.rows.size();
  }

  // Overrides of AbstractMatrix for the Sparse OP Sparse case.

  /**
   * This implementation adds a SparseMatrix with a SparseMatrix.
   *
   * @param other a sparse matrix
   */
  @Override
  protected SquareMatrix addSparseMatrix(SparseMatrix other) {
    SparseMatrix result = new SparseMatrix(this.rows.size());
    // pick row from both matrices
    // go right
    // check the 4 cases
    RowSentinel<Float> headRowA;
    RowSentinel<Float> headRowB;
    AbstractNode<Float> rowA;
    AbstractNode<Float> rowB;

    for (int i = 0; i < this.rows.size(); i++) {
      headRowA = this.rows.get(i);
      rowA = headRowA.right;

      headRowB = other.rows.get(i);
      rowB = headRowB.right;

      // if both rows have elements
      while (rowA != headRowA && rowB != headRowB) {
        // check if indices match
        if (rowA.colIndex == rowB.colIndex) {
          // add them and store at the index of any 1
          float resultAdd = rowA.getDataAtNode() + rowB.getDataAtNode();
          result.set(rowA.rowIndex, rowA.colIndex, resultAdd);
          rowA = rowA.right;
          rowB = rowB.right;
        } else if (rowA.colIndex < rowB.colIndex) {
          // rowA element is before rowB
          result.set(rowA.rowIndex, rowA.colIndex, rowA.getDataAtNode());
          rowA = rowA.right;
        } else {
          // rowB element is before rowA
          result.set(rowB.rowIndex, rowB.colIndex, rowB.getDataAtNode());
          rowB = rowB.right;
        }
      }

      // Add remaining elements from rowA
      while (rowA != headRowA) {
        result.set(rowA.rowIndex, rowA.colIndex, rowA.getDataAtNode());
        rowA = rowA.right;
      }

      // Add remaining elements from rowB
      while (rowB != headRowB) {
        result.set(rowB.rowIndex, rowB.colIndex, rowB.getDataAtNode());
        rowB = rowB.right;
      }
    }

    return result;
  }

  /**
   * This implementation post-multiplies a SparseMatrix with a SparseMatrix. i.e. SparseMatrix-this
   * * SparseMatrix-other
   *
   * @param other a sparse matrix
   */
  @Override
  protected SquareMatrix postmulSparseMatrix(SparseMatrix other) {
    SparseMatrix result = new SparseMatrix(this.rows.size());
    // vars to traverse rows of this matrix and cols of other matrix
    RowSentinel<Float> headRowA;
    ColumnSentinel<Float> headColB;
    AbstractNode<Float> rowA;
    AbstractNode<Float> colB;

    for (int i = 0; i < this.rows.size(); i++) {
      headRowA = this.rows.get(i);
      rowA = headRowA.right;

      // if row has no elements skip row iteration
      if (rowA == headRowA) {
        continue;
      }

      for (int j = 0; j < other.cols.size(); j++) {
        // Reset row head on every iteration
        headRowA = this.rows.get(i);
        rowA = headRowA.right;
        headColB = other.cols.get(j);
        colB = headColB.bottom;

        // if col has no elements skip col iteration
        if (colB == headColB) {
          continue;
        }

        // multiplication value at (i,j)
        float sum = 0f;

        // While both row and column have elements
        while (rowA != headRowA && colB != headColB) {
          // indices match multiply and add to the sum
          if (rowA.colIndex == colB.rowIndex) {
            sum += rowA.getDataAtNode() * colB.getDataAtNode();
            rowA = rowA.right;
            colB = colB.bottom;
          } else if (rowA.colIndex < colB.rowIndex) {
            // skip row elements
            rowA = rowA.right;
          } else {
            // skip col elements
            colB = colB.bottom;
          }
        }

        if (sum != 0f) {
          result.set(i, j, sum);
        }
      }
    }

    return result;
  }

  /**
   * This implementation pre-multiplies a SparseMatrix with a SparseMatrix. i.e. SparseMatrix-other
   * * SparseMatrix-this
   *
   * @param other a sparse matrix
   */
  @Override
  protected SquareMatrix premulSparseMatrix(SparseMatrix other) {
    SparseMatrix result = new SparseMatrix(other.rows.size());
    // vars to traverse rows of other matrix and cols of this matrix
    RowSentinel<Float> headRowA;
    ColumnSentinel<Float> headColB;
    AbstractNode<Float> rowA;
    AbstractNode<Float> colB;

    for (int i = 0; i < this.rows.size(); i++) {
      headRowA = other.rows.get(i);
      rowA = headRowA.right;

      // if row has no elements skip row iteration
      if (rowA == headRowA) {
        continue;
      }

      for (int j = 0; j < other.cols.size(); j++) {
        // Reset row head on every iteration
        headRowA = other.rows.get(i);
        rowA = headRowA.right;
        headColB = this.cols.get(j);
        colB = headColB.bottom;

        // if col has no elements skip col iteration
        if (colB == headColB) {
          continue;
        }

        // multiplication value at (i,j)
        float sum = 0f;

        // While both row and column have elements
        while (rowA != headRowA && colB != headColB) {
          // indices match multiply and add to the sum
          if (rowA.colIndex == colB.rowIndex) {
            sum += rowA.getDataAtNode() * colB.getDataAtNode();
            rowA = rowA.right;
            colB = colB.bottom;
          } else if (rowA.colIndex < colB.rowIndex) {
            // skip row elements
            rowA = rowA.right;
          } else {
            // skip col elements
            colB = colB.bottom;
          }
        }

        if (sum != 0f) {
          result.set(i, j, sum);
        }
      }
    }

    return result;
  }

  @Override
  protected SquareMatrix addArrayMatrix(ArrayMatrix other) {
    SparseMatrix result = new SparseMatrix(this.rows.size());
    // pick row from both matrices
    // go right
    // check the 4 cases
    RowSentinel<Float> headRowA;
    AbstractNode<Float> rowA;

    for (int i = 0; i < this.rows.size(); i++) {
      headRowA = this.rows.get(i);
      rowA = headRowA.right;

      for (int j = 0; j < other.size(); j++) {
        // if indices match add elements and store sum at index of any 1
        if (rowA.colIndex == j) {
          float resultAdd = rowA.getDataAtNode() + other.get(i, j);
          result.set(i, j, resultAdd);
          rowA = rowA.right;
        } else { // add elements of B
          // if (rowA.colIndex > j) condition will always be true if indices don't match
          result.set(i, j, other.get(i, j));
        }
      }
    }
    return result;
  }

  @Override
  protected SquareMatrix postmulArrayMatrix(ArrayMatrix other) {
    SquareMatrix result = new ArrayMatrix(this.rows.size());
    // vars to traverse rows of this matrix and cols of other matrix
    RowSentinel<Float> headRowA;
    AbstractNode<Float> rowA;

    for (int i = 0; i < this.rows.size(); i++) {
      for (int j = 0; j < this.cols.size(); j++) {
        headRowA = this.rows.get(i);
        rowA = headRowA.right;

        // multiplication value at (i,j)
        float sum = 0f;

        while (rowA != headRowA) {
          sum += rowA.getDataAtNode() + other.get(rowA.colIndex, j);
          rowA = rowA.right;
        }

        if (sum != 0f) {
          result.set(i, j, sum);
        }
      }
    }

    return result;
  }

  @Override
  protected SquareMatrix premulArrayMatrix(ArrayMatrix other) {
    SquareMatrix result = new ArrayMatrix(this.rows.size());
    // vars to traverse rows of this matrix and cols of other matrix
    ColumnSentinel<Float> headColB;
    AbstractNode<Float> colB;

    // computing result (i,j)
    for (int i = 0; i < this.cols.size(); i++) {
      for (int j = 0; j < this.rows.size(); j++) {
        headColB = this.cols.get(j);
        colB = headColB.bottom;

        // multiplication value at (i,j)
        float sum = 0f;

        while (colB != headColB) {
          sum += colB.getDataAtNode() * other.get(i, colB.rowIndex);
          colB = colB.bottom;
        }

        if (sum != 0f) {
          result.set(i, j, sum);
        }
      }
    }

    return result;
  }
}
