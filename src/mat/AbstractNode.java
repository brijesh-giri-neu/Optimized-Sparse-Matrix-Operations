package mat;

/**
 * This abstract class represents the common aspects of a single node of a 2D matrix.
 */
abstract class AbstractNode<T> {

  // each type of node has a row and column associated with it.
  protected int rowIndex;
  protected int colIndex;
  //each type of node has a reference to the left, right, top, and bottom nodes in the matrix.
  protected AbstractNode<T> left;
  protected AbstractNode<T> right;
  protected AbstractNode<T> top;
  protected AbstractNode<T> bottom;

  AbstractNode() {
    rowIndex = -1;
    colIndex = -1;
    this.left = null;
    this.right = null;
    this.top = null;
    this.bottom = null;
  }

  AbstractNode(int rowIndex, int colIndex, AbstractNode<T> left, AbstractNode<T> right,
      AbstractNode<T> top, AbstractNode<T> bottom) {
    this.rowIndex = rowIndex;
    this.colIndex = colIndex;
    this.left = left;
    this.right = right;
    this.top = top;
    this.bottom = bottom;

    //set the references
    this.left.right = this;
    this.right.left = this;
    this.top.bottom = this;
    this.bottom.top = this;
  }

  /**
   * Return the data stored in this node.
   *
   * @return by default, this method returns null. This implementation is good for the Sentinel
   */
  T getDataAtNode() {
    return null;
  }

  /**
   * Add a new node in the matrix at the given position with given links.
   *
   * @param object   the object to be added to this matrix
   * @param rowIndex row index of the node
   * @param colIndex col index of the node
   * @param left     left link of the node to be added
   * @param right    right link of the node to be added
   * @param top      top link of the node to be added
   * @param bottom   bottom link of the node to be added
   */
  void addNode(T object, int rowIndex, int colIndex, AbstractNode<T> left, AbstractNode<T> right,
      AbstractNode<T> top, AbstractNode<T> bottom) {
    AbstractNode<T> newNode = new DataNode<T>(object, rowIndex, colIndex, left, right, top, bottom);
  }

  /**
   * Remove this node from the list.
   */
  void remove() {
    //perform a nodec-tomy!
    this.left.right = this.right;
    this.right.left = this.left;
    this.top.bottom = this.bottom;
    this.bottom.top = this.top;
  }
}

