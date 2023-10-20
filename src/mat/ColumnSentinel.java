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
}
