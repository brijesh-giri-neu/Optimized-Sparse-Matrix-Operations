import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import mat.ArrayMatrix;
import mat.SquareMatrix;
import org.junit.Before;
import org.junit.Test;

/**
 * This class represents a Junit test class for the ArrayMatrix class.
 */
public class ArrayMatrixTest {

  private ArrayMatrix arrayMatrix;
  private float delta = 0f;

  @Before
  public void setUp() {
    try {
      arrayMatrix = new ArrayMatrix(100);
    } catch (Exception e) {
      fail();
    }
  }

  @Test
  public void initializeMatrix() {
    try {
      new ArrayMatrix(1000);
    } catch (Exception e) {
      fail();
    }
  }

  @Test(expected = IllegalArgumentException.class)
  public void initializeMatrixNegativeSize() {
    new ArrayMatrix(-1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void initializeMatrixSizeZeroThenGet() {
    arrayMatrix = new ArrayMatrix(0);
    arrayMatrix.get(0, 0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void initializeMatrixSizeZeroThenSet() {
    arrayMatrix = new ArrayMatrix(0);
    arrayMatrix.set(0, 0, 100f);
  }

  @Test(expected = IllegalArgumentException.class)
  public void getNegativeIndex() {
    arrayMatrix = new ArrayMatrix(100);
    arrayMatrix.get(-1, -1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void setNegativeIndex() {
    arrayMatrix = new ArrayMatrix(100);
    arrayMatrix.set(-1, -1, 100f);
  }

  @Test(expected = IllegalArgumentException.class)
  public void getSizeEqualsIndex() {
    arrayMatrix = new ArrayMatrix(100);
    arrayMatrix.get(100, 10);
  }

  @Test(expected = IllegalArgumentException.class)
  public void setSizeEqualsIndex() {
    arrayMatrix = new ArrayMatrix(100);
    arrayMatrix.set(100, 10, 10f);
  }

  @Test(expected = IllegalArgumentException.class)
  public void getIndexBeyondSize() {
    arrayMatrix = new ArrayMatrix(100);
    arrayMatrix.get(105, 10);
  }

  @Test(expected = IllegalArgumentException.class)
  public void setIndexBeyondSize() {
    arrayMatrix = new ArrayMatrix(100);
    arrayMatrix.set(105, 10, 110f);
  }

  @Test
  public void setNonZeroValue() {
    float value = 100.23f;
    int i = 5;
    int j = 3;
    arrayMatrix.set(i, j, value);

    assertEquals(value, arrayMatrix.get(i, j), delta);
  }

  @Test
  public void setZeroValue() {
    float value = 0f;
    int i = 1;
    int j = 3;
    arrayMatrix.set(5, 2, 100.32f);
    arrayMatrix.set(i, j, value);

    assertEquals(value, arrayMatrix.get(i, j), delta);
  }

  @Test
  public void getZeroValueNode() {
    assertEquals(0.0f, arrayMatrix.get(10, 30), delta);
  }

  @Test(timeout = 10000)
  public void setIdentity() {
    int size = 10000;
    arrayMatrix = new ArrayMatrix(size);
    arrayMatrix.setIdentity();

    for (int i = 0; i < size; i++) {
      for (int j = 0; j < size; j++) {
        if (i == j) {
          assertEquals(1f, arrayMatrix.get(i, j), delta);
        } else {
          assertEquals(0f, arrayMatrix.get(i, j), delta);
        }
      }
    }
  }

  @Test
  public void testAdd() {
    /*

    1 2 3 4
    1 5 2 1
    0 1 -1 2
    0 0 1 0

    +

    2 1 3 1
    2 1 5 4
    -1 2 1 1
    1 1 1 1

    =

    3 3 6 5
    3 6 7 5
    -1 3 0 3
    1 1 2 1

     */
    SquareMatrix one = new ArrayMatrix(4);
    SquareMatrix two = new ArrayMatrix(4);
    float[][] onevalues = {{1, 2, 3, 4}, {1, 5, 2, 1}, {0, 1, -1, 2}, {0, 0, 1, 0}};

    float[][] twovalues = {{2, 1, 3, 1}, {2, 1, 5, 4}, {-1, 2, 1, 1}, {1, 1, 1, 1}};

    float[][] answer = {{3, 3, 6, 5}, {3, 6, 7, 5}, {-1, 3, 0, 3}, {1, 1, 2, 1}};
    for (int i = 0; i < 4; i += 1) {
      for (int j = 0; j < 4; j += 1) {
        one.set(i, j, onevalues[i][j]);
        two.set(i, j, twovalues[i][j]);
      }
    }
    SquareMatrix result = one.add(two);
    for (int i = 0; i < 4; i += 1) {
      for (int j = 0; j < 4; j += 1) {
        assertEquals(answer[i][j], result.get(i, j), 0.001);
      }
    }
  }

  @Test
  public void testLargeIdentities() {
    int dim = 100;
    SquareMatrix one = new ArrayMatrix(dim);
    SquareMatrix two = new ArrayMatrix(dim);
    one.setIdentity();
    two.setIdentity();
    SquareMatrix add = one.add(two);
    SquareMatrix premul = one.premul(two);
    SquareMatrix postmul = one.postmul(two);

    for (int i = 0; i < dim; i += 1) {
      for (int j = 0; j < dim; j += 1) {
        if (i == j) {
          assertEquals(2, add.get(i, j), 0.001);
          assertEquals(1, premul.get(i, j), 0.001);
          assertEquals(1, postmul.get(i, j), 0.001);
        } else {
          assertEquals(0, add.get(i, j), 0.001);
          assertEquals(0, premul.get(i, j), 0.001);
          assertEquals(0, postmul.get(i, j), 0.001);
        }
      }
    }
  }
}

