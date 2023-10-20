package mat;

import java.util.Arrays;
import java.util.List;

/**
 * This node represents a row sentinel of a 2D matrix. The row of a matrix begins and ends with the
 * same sentinel. This sentinel contains some unique functions that are not part of other types of
 * nodes.
 */
class RowSentinel<T> extends AbstractNode<T> {

  RowSentinel() {
    super();
    this.left = this;
    this.right = this;
    this.top = this;
    this.bottom = this;
  }

  /**
   * Returns the item at the given row and column, in this matrix using a Row Sentinel to traverse.
   *
   * @param rowIndex row index
   * @param colIndex column index
   * @param size     size of the matrix to decide traversal direction
   * @return the value of the node located at given index
   */
  T get(int rowIndex, int colIndex, int size) {
    // assumption - this is the correct row sentinel

    // have used multiple returns instead of assigning result to variable
    // as it improved performance during testing.
    AbstractNode<T> curr;
    //null is the default value for Java Generics, just like 0 is default for numeric types
    T data = null;

    // no nodes in the list, return null
    if (this.right == this && this.left == this) {
      return data;
    }

    if (colIndex <= size / 2) { //start from beginning and move forwards
      curr = this.right;

      //loop through each DataNode and try to find the relevant node
      while (curr != this) {
        if (curr.rowIndex == rowIndex && curr.colIndex == colIndex) {
          return curr.getDataAtNode();
        }
        curr = curr.right;
      }
    } else { //start from the end and move backwards
      curr = this.left;

      //loop through each DataNode and try to find the relevant node
      while (curr != this) {
        if (curr.rowIndex == rowIndex && curr.colIndex == colIndex) {
          return curr.getDataAtNode();
        }
        curr = curr.left;
      }
    }

    // such a node does not exist in the list,return null
    return data;
  }

  /**
   * Get the left and right links for the node by traversing a row of the matrix.
   *
   * @param rowIndex row index of the node
   * @param colIndex col index of the node
   * @return list of links where index0 -> leftLink and index1 -> rightLink
   */
  List<AbstractNode<T>> getHorizontalLinks(int rowIndex, int colIndex) {
    // assumption - this the correct row sentinel
    // compromise - traversing ONLY right to get links.

    // logic to find left, right of the DataNode to be added
    AbstractNode<T> leftOfDataNode = null;
    AbstractNode<T> rightOfDataNode = null;

    // find correct row
    AbstractNode<T> currRow;
    //Empty list
    if (this.right == this && this.left == this) {
      leftOfDataNode = this;
      rightOfDataNode = this;
      //exit;
    } else {
      currRow = this.right;
      while (currRow != this) {
        if (currRow.rowIndex == rowIndex && currRow.colIndex < colIndex) {
          currRow = currRow.right;
          continue;
        }

        if (currRow.rowIndex == rowIndex && currRow.colIndex == colIndex) {
          leftOfDataNode = currRow.left;
          rightOfDataNode = currRow.right;
          break;
          //exit
        }

        //first element with colIndex greater than our node
        //add to the left of node
        if (currRow.rowIndex == rowIndex && currRow.colIndex > colIndex) {
          leftOfDataNode = currRow.left;
          rightOfDataNode = currRow;
          break;
        }
      }
      // continue-case: curr node colIndex is smaller than our node, and we reached sentinel
      // add to the left of sentinel
      if (currRow == this) {
        leftOfDataNode = currRow.left;
        rightOfDataNode = currRow;
      }
    }
    return Arrays.asList(leftOfDataNode, rightOfDataNode);
  }

  /**
   * Remove a node if it exists.
   *
   * @param rowIndex row index
   * @param colIndex col index
   */
  void removeNodeIfExists(int rowIndex, int colIndex) {
    // assumption - this is the correct row sentinel
    AbstractNode<T> currRow;

    currRow = this.right;
    while (currRow != this) {
      if (currRow.rowIndex == rowIndex && currRow.colIndex == colIndex) {
        currRow.remove();
        break;
      }
      currRow = currRow.right;
    }
  }
}
