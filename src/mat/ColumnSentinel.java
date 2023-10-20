package mat;

import java.util.Arrays;
import java.util.List;

/**
 * This node represents a column sentinel of a 2D matrix. The column of a matrix begins and ends
 * with the same sentinel. This sentinel contains some unique functions that are not part of other
 * types of nodes.
 */
class ColumnSentinel<T> extends AbstractNode<T> {

  ColumnSentinel() {
    super();
    this.left = this;
    this.right = this;
    this.top = this;
    this.bottom = this;
  }

  /**
   * Get the top and bottom links for the node to be added by traversing a column of the matrix.
   *
   * @param rowIndex row index of the node
   * @param colIndex col index of the node
   * @return list of links where index0 -> topLink and index1 -> bottomLink
   */
  List<AbstractNode<T>> getVerticalLinks(int rowIndex, int colIndex) {
    // assumption - this is the correct column sentinel
    // compromise - traversing ONLY bottom to get links.

    // logic to find top, bottom of the DataNode to be added
    AbstractNode<T> topOfDataNode = null;
    AbstractNode<T> bottomOfDataNode = null;

    // find correct col
    AbstractNode<T> currCol;
    //Empty list
    if (this.top == this && this.bottom == this) {
      topOfDataNode = this;
      bottomOfDataNode = this;
      //exit;
    } else {
      currCol = this.bottom;
      while (currCol != this) {
        if (currCol.colIndex == colIndex && currCol.rowIndex < rowIndex) {
          currCol = currCol.bottom;
          continue;
        }

        if (currCol.colIndex == colIndex && currCol.rowIndex == rowIndex) {
          topOfDataNode = currCol.top;
          bottomOfDataNode = currCol.bottom;
          break;
          // exit
        }

        //first element with rowIndex greater than our node
        //add to the top of node
        if (currCol.colIndex == colIndex && currCol.rowIndex > rowIndex) {
          topOfDataNode = currCol.top;
          bottomOfDataNode = currCol;
          break;
        }
      }
      // continue-case: curr node rowIndex is smaller than our node, and we reached sentinel
      // add to the top of sentinel
      if (currCol == this) {
        topOfDataNode = currCol.top;
        bottomOfDataNode = currCol;
      }
    }

    return Arrays.asList(topOfDataNode, bottomOfDataNode);
  }

  /**
   * Returns the item at the given row and column, in this matrix using a Col Sentinel to traverse.
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
    if (this.top == this && this.bottom == this) {
      return data;
    }

    if (rowIndex <= size / 2) { //start from beginning and move forwards
      curr = this.bottom;

      //loop through each DataNode and try to find the relevant node
      while (curr != this) {
        if (curr.rowIndex == rowIndex && curr.colIndex == colIndex) {
          return curr.getDataAtNode();
        }
        curr = curr.bottom;
      }
    } else { //start from the end and move backwards
      curr = this.top;

      //loop through each DataNode and try to find the relevant node
      while (curr != this) {
        if (curr.rowIndex == rowIndex && curr.colIndex == colIndex) {
          return curr.getDataAtNode();
        }
        curr = curr.top;
      }
    }

    // such a node does not exist in the list,return null
    return data;
  }
}
