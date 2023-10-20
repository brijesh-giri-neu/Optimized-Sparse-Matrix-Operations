import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.Random;
import mat.ArrayMatrix;
import mat.SparseMatrix;
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

  @Test(timeout = 10000)
  public void matrixInitializationWorstCase() {
    int size = 100;
    arrayMatrix = new ArrayMatrix(size);

    float[][] expectedOutput = new float[size][size];
    for (int i = 0; i < size; i++) {
      for (int j = 0; j < size; j++) {
        float randomVal = new Random().nextFloat();
        expectedOutput[i][j] = randomVal;
        arrayMatrix.set(i, j, randomVal);
      }
    }

    for (int i = 0; i < size; i++) {
      for (int j = 0; j < size; j++) {
        assertEquals(expectedOutput[i][j], arrayMatrix.get(i, j), delta);
      }
    }
  }

  @Test
  public void setExistingValueToZero() {
    arrayMatrix = new ArrayMatrix(100);

    arrayMatrix.set(10, 10, 100f);
    arrayMatrix.set(10, 10, 0f);

    assertEquals(0f, arrayMatrix.get(10, 10), delta);
  }

  @Test
  public void updateExistingValue() {
    arrayMatrix = new ArrayMatrix(100);

    arrayMatrix.set(10, 10, 100f);
    arrayMatrix.set(10, 10, 22f);

    assertEquals(22f, arrayMatrix.get(10, 10), delta);
  }

  @Test
  public void size() {
    arrayMatrix = new ArrayMatrix(100);
    assertEquals(100, arrayMatrix.size());
  }

  @Test(timeout = 10000)
  public void setIdentityOnlyCheckDiagonals() {
    int size = 100;
    arrayMatrix = new ArrayMatrix(size);
    arrayMatrix.setIdentity();

    for (int i = 0; i < size; i++) {
      assertEquals(1f, arrayMatrix.get(i, i), delta);
    }
  }

  @Test(timeout = 10000)
  public void addArray() {
    int size = 100;
    SquareMatrix a = new ArrayMatrix(size);
    SquareMatrix b = new ArrayMatrix(size);
    float[][] expectedA = new float[size][size];
    float[][] expectedB = new float[size][size];

    // 40 percent density
    for (int i = 0; i < 0.4 * size; i++) {
      // For Matrix A
      int rowIndexA = new Random().nextInt(size);
      int colIndexA = new Random().nextInt(size);
      float randomValA = new Random().nextFloat();

      expectedA[rowIndexA][colIndexA] = randomValA;
      a.set(rowIndexA, colIndexA, randomValA);

      // For Matrix B
      int rowIndexB = new Random().nextInt(size);
      int colIndexB = new Random().nextInt(size);
      float randomValB = new Random().nextFloat();

      expectedB[rowIndexB][colIndexB] = randomValB;
      b.set(rowIndexB, colIndexB, randomValB);
    }

    SquareMatrix c = a.add(b);

    for (int i = 0; i < size; i++) {
      for (int j = 0; j < size; j++) {
        float expectedSum = expectedA[i][j] + expectedB[i][j];
        float result = c.get(i, j);
        assertEquals(expectedSum, result, delta);
      }
    }
  }


  @Test(timeout = 10000)
  public void addMixed() {
    int size = 100;
    ArrayMatrix a = new ArrayMatrix(size);
    SparseMatrix b = new SparseMatrix(size);
    float[][] expectedA = new float[size][size];
    float[][] expectedB = new float[size][size];

    // Fill ArrayMatrix
    for (int i = 0; i < 0.85 * size; i++) {
      // For Matrix B
      int rowIndexB = new Random().nextInt(size);
      int colIndexB = new Random().nextInt(size);
      float randomValB = new Random().nextFloat();

      expectedB[rowIndexB][colIndexB] = randomValB;
      b.set(rowIndexB, colIndexB, randomValB);
    }

    // Fill arrayMatrix
    for (int i = 0; i < 0.4 * size; i++) {
      // For Matrix A
      int rowIndexA = new Random().nextInt(size);
      int colIndexA = new Random().nextInt(size);
      float randomValA = new Random().nextFloat();

      expectedA[rowIndexA][colIndexA] = randomValA;
      a.set(rowIndexA, colIndexA, randomValA);
    }

    SquareMatrix c = a.add(b);

    for (int i = 0; i < size; i++) {
      for (int j = 0; j < size; j++) {
        float expectedSum = expectedA[i][j] + expectedB[i][j];
        float result = c.get(i, j);
        assertEquals(expectedSum, result, delta);
      }
    }
  }

  @Test(timeout = 10000)
  public void premulArray() {
    int size = 100;
    ArrayMatrix a = new ArrayMatrix(size);
    ArrayMatrix b = new ArrayMatrix(size);
    float[][] expectedA = new float[size][size];
    float[][] expectedB = new float[size][size];

    // 40 percent density
    for (int i = 0; i < 0.4 * size; i++) {
      // For Matrix A
      int rowIndexA = new Random().nextInt(size);
      int colIndexA = new Random().nextInt(size);
      float randomValA = new Random().nextFloat();

      expectedA[rowIndexA][colIndexA] = randomValA;
      a.set(rowIndexA, colIndexA, randomValA);

      // For Matrix B
      int rowIndexB = new Random().nextInt(size);
      int colIndexB = new Random().nextInt(size);
      float randomValB = new Random().nextFloat();

      expectedB[rowIndexB][colIndexB] = randomValB;
      b.set(rowIndexB, colIndexB, randomValB);
    }

    SquareMatrix c = b.premul(a);

    for (int i = 0; i < size; i++) {
      for (int j = 0; j < size; j++) {
        float expectedValue = 0f;
        for (int k = 0; k < size; k++) {
          expectedValue += expectedA[i][k] * expectedB[k][j];
        }
        float result = c.get(i, j);
        assertEquals(expectedValue, result, delta);
      }
    }
  }

  //    @Test(timeout = 10000)
  //    public void premulMixed() {
  //      int size = 100;
  //      ArrayMatrix a = new ArrayMatrix(size);
  //      SparseMatrix b = new SparseMatrix(size);
  //      float[][] expectedA = new float[size][size];
  //      float[][] expectedB = new float[size][size];
  //
  //      // Fill ArrayMatrix
  //      for (int i = 0; i < 0.85 * size; i++) {
  //        // For Matrix B
  //        int rowIndexB = new Random().nextInt(size);
  //        int colIndexB = new Random().nextInt(size);
  //        float randomValB = new Random().nextFloat();
  //
  //        expectedB[rowIndexB][colIndexB] = randomValB;
  //        b.set(rowIndexB, colIndexB, randomValB);
  //      }
  //
  //      // 40 percent density
  //      for (int i = 0; i < 0.4 * size; i++) {
  //        // For Matrix B
  //        int rowIndexB = new Random().nextInt(size);
  //        int colIndexB = new Random().nextInt(size);
  //        float randomValB = new Random().nextFloat();
  //
  //        expectedB[rowIndexB][colIndexB] = randomValB;
  //        b.set(rowIndexB, colIndexB, randomValB);
  //      }
  //
  //      SquareMatrix c = a.premul(b);
  //
  //      for (int i = 0; i < size; i++) {
  //        for (int j = 0; j < size; j++) {
  //          float expectedValue = 0f;
  //          for (int k = 0; k < size; k++) {
  //            expectedValue += expectedB[i][k] * expectedA[k][j];
  //          }
  //          float result = c.get(i, j);
  //          assertEquals(expectedValue, result, delta);
  //        }
  //      }
  //    }

  @Test(timeout = 10000)
  public void postmulArray() {
    int size = 100;
    ArrayMatrix a = new ArrayMatrix(size);
    ArrayMatrix b = new ArrayMatrix(size);
    float[][] expectedA = new float[size][size];
    float[][] expectedB = new float[size][size];

    // 40 percent density
    for (int i = 0; i < 0.4 * size; i++) {
      // For Matrix A
      int rowIndexA = new Random().nextInt(size);
      int colIndexA = new Random().nextInt(size);
      float randomValA = new Random().nextFloat();

      expectedA[rowIndexA][colIndexA] = randomValA;
      a.set(rowIndexA, colIndexA, randomValA);

      // For Matrix B
      int rowIndexB = new Random().nextInt(size);
      int colIndexB = new Random().nextInt(size);
      float randomValB = new Random().nextFloat();

      expectedB[rowIndexB][colIndexB] = randomValB;
      b.set(rowIndexB, colIndexB, randomValB);
    }

    SquareMatrix c = a.postmul(b);

    for (int i = 0; i < size; i++) {
      for (int j = 0; j < size; j++) {
        float expectedValue = 0f;
        for (int k = 0; k < size; k++) {
          expectedValue += expectedA[i][k] * expectedB[k][j];
        }
        float result = c.get(i, j);
        assertEquals(expectedValue, result, delta);
      }
    }
  }

  @Test(timeout = 10000)
  public void postmulMixed() {
    int size = 100;
    ArrayMatrix a = new ArrayMatrix(size);
    SparseMatrix b = new SparseMatrix(size);
    float[][] expectedA = new float[size][size];
    float[][] expectedB = new float[size][size];

    // Fill ArrayMatrix
    for (int i = 0; i < 0.85 * size; i++) {
      // For Matrix B
      int rowIndexB = new Random().nextInt(size);
      int colIndexB = new Random().nextInt(size);
      float randomValB = new Random().nextFloat();

      expectedB[rowIndexB][colIndexB] = randomValB;
      b.set(rowIndexB, colIndexB, randomValB);
    }

    // 40 percent density
    for (int i = 0; i < 0.4 * size; i++) {
      // For Matrix B
      int rowIndexB = new Random().nextInt(size);
      int colIndexB = new Random().nextInt(size);
      float randomValB = new Random().nextFloat();

      expectedB[rowIndexB][colIndexB] = randomValB;
      b.set(rowIndexB, colIndexB, randomValB);
    }

    SquareMatrix c = a.postmul(b);

    for (int i = 0; i < size; i++) {
      for (int j = 0; j < size; j++) {
        float expectedValue = 0f;
        for (int k = 0; k < size; k++) {
          expectedValue += expectedA[i][k] * expectedB[k][j];
        }
        float result = c.get(i, j);
        assertEquals(expectedValue, result, delta);
      }
    }
  }

  @Test(timeout = 10000)
  public void resetIdentity() {
    int size = 4000;
    arrayMatrix = new ArrayMatrix(size);
    // Fill the matrix with 40 percent density
    for (int i = 0; i < 0.4 * size; i++) {
      int rowIndex = new Random().nextInt(size);
      int colIndex = new Random().nextInt(size);
      float randomVal = new Random().nextFloat();

      arrayMatrix.set(rowIndex, colIndex, randomVal);
    }

    arrayMatrix.setIdentity();

    for (int i = 0; i < size; i++) {
      assertEquals(1f, arrayMatrix.get(i, i), delta);
    }
  }

  @Test(timeout = 10000)
  public void resetIdentityAndFillAgain() {
    resetIdentity();

    int size = 4000;
    arrayMatrix = new ArrayMatrix(size);
    float[][] expectedMatrix = new float[size][size];
    // Fill the matrix with 40 percent density
    for (int i = 0; i < 0.4 * size; i++) {
      int rowIndex = new Random().nextInt(size);
      int colIndex = new Random().nextInt(size);
      float randomVal = new Random().nextFloat();

      expectedMatrix[rowIndex][colIndex] = randomVal;
      arrayMatrix.set(rowIndex, colIndex, randomVal);
    }

    for (int i = 0; i < size; i++) {
      for (int j = 0; j < size; j++) {
        assertEquals(expectedMatrix[i][j], arrayMatrix.get(i, j), delta);
      }
    }
  }
}

