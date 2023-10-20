package mat;

/**
 * This node represents the only data-containing node of a 2D matrix.
 */
class DataNode<T> extends AbstractNode<T> {

  private T data;

  DataNode(T data, int rowIndex, int colIndex, AbstractNode<T> left, AbstractNode<T> right,
      AbstractNode<T> top, AbstractNode<T> bottom) {
    super(rowIndex, colIndex, left, right, top, bottom);
    this.data = data;
  }

  /**
   * Get the data stored in this node.
   *
   * @return the data item in this node
   */
  @Override
  T getDataAtNode() {
    return this.data;
  }
}

