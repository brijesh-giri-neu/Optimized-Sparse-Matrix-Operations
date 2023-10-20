import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.Random;
import mat.ArrayMatrix;
import mat.SparseMatrix;
import mat.SquareMatrix;
import org.junit.Before;
import org.junit.Test;

/**
 * This class represents a Junit test class for the SparseMatrix class.
 */
public class SparseMatrixTest {

  private SparseMatrix sparseMatrix;
  private float delta = 0f;

  @Before
  public void setUp() {
    try {
      sparseMatrix = new SparseMatrix(100);
    } catch (Exception e) {
      fail();
    }
  }

  @Test
  public void initializeMatrix() {
    try {
      new SparseMatrix(1000000);
    } catch (Exception e) {
      fail();
    }
  }

  @Test(expected = IllegalArgumentException.class)
  public void initializeMatrixNegativeSize() {
    new SparseMatrix(-1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void initializeMatrixSizeZeroThenGet() {
    sparseMatrix = new SparseMatrix(0);
    sparseMatrix.get(0, 0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void initializeMatrixSizeZeroThenSet() {
    sparseMatrix = new SparseMatrix(0);
    sparseMatrix.set(0, 0, 100f);
  }

  @Test(expected = IllegalArgumentException.class)
  public void getNegativeIndex() {
    sparseMatrix = new SparseMatrix(100);
    sparseMatrix.get(-1, -1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void setNegativeIndex() {
    sparseMatrix = new SparseMatrix(100);
    sparseMatrix.set(-1, -1, 100f);
  }

  @Test(expected = IllegalArgumentException.class)
  public void getSizeEqualsIndex() {
    sparseMatrix = new SparseMatrix(100);
    sparseMatrix.get(100, 10);
  }

  @Test(expected = IllegalArgumentException.class)
  public void setSizeEqualsIndex() {
    sparseMatrix = new SparseMatrix(100);
    sparseMatrix.set(100, 10, 10f);
  }

  @Test(expected = IllegalArgumentException.class)
  public void getIndexBeyondSize() {
    sparseMatrix = new SparseMatrix(100);
    sparseMatrix.get(105, 10);
  }

  @Test(expected = IllegalArgumentException.class)
  public void setIndexBeyondSize() {
    sparseMatrix = new SparseMatrix(100);
    sparseMatrix.set(105, 10, 110f);
  }

  @Test(timeout = 10000)
  public void setIdentity() {
    int size = 10000;
    sparseMatrix = new SparseMatrix(size);
    sparseMatrix.setIdentity();

    for (int i = 0; i < size; i++) {
      for (int j = 0; j < size; j++) {
        if (i == j) {
          assertEquals(1f, sparseMatrix.get(i, j), delta);
        } else {
          assertEquals(0f, sparseMatrix.get(i, j), delta);
        }
      }
    }
  }

  @Test(timeout = 10000)
  public void matrixInitializationWorstCase() {
    int size = 100;
    sparseMatrix = new SparseMatrix(size);

    float[][] expectedOutput = new float[size][size];
    for (int i = 0; i < size; i++) {
      for (int j = 0; j < size; j++) {
        float randomVal = new Random().nextFloat();
        expectedOutput[i][j] = randomVal;
        sparseMatrix.set(i, j, randomVal);
      }
    }

    for (int i = 0; i < size; i++) {
      for (int j = 0; j < size; j++) {
        assertEquals(expectedOutput[i][j], sparseMatrix.get(i, j), delta);
      }
    }
  }

  @Test
  public void setNonZeroValue() {
    float value = 100.23f;
    int i = 5;
    int j = 3;
    sparseMatrix.set(i, j, value);

    assertEquals(value, sparseMatrix.get(i, j), delta);
  }

  @Test
  public void setZeroValue() {
    float value = 0f;
    int i = 1;
    int j = 3;
    sparseMatrix.set(5, 2, 100.32f);
    sparseMatrix.set(i, j, value);

    assertEquals(value, sparseMatrix.get(i, j), delta);
  }

  @Test(timeout = 10000)
  public void setFarEnds() {
    float value = 100.32f;
    sparseMatrix = new SparseMatrix(1000000);
    sparseMatrix.set(999999, 99999, value);
    assertEquals(value, sparseMatrix.get(999999, 99999), delta);

    // same column, multiple rows
    sparseMatrix.set(100, 99999, value);
    assertEquals(value, sparseMatrix.get(100, 99999), delta);
    sparseMatrix.set(50, 99999, value);
    assertEquals(value, sparseMatrix.get(50, 99999), delta);
    sparseMatrix.set(200, 99999, value);
    assertEquals(value, sparseMatrix.get(200, 99999), delta);
    sparseMatrix.set(200, 999999, value);
    assertEquals(value, sparseMatrix.get(200, 999999), delta);

    // same row, multiple columns
    sparseMatrix.set(500, 50000, value);
    assertEquals(value, sparseMatrix.get(500, 50000), delta);
    sparseMatrix.set(500, 99999, value);
    assertEquals(value, sparseMatrix.get(500, 99999), delta);
    sparseMatrix.set(500, 30000, value);
    assertEquals(value, sparseMatrix.get(500, 30000), delta);
    sparseMatrix.set(500, 50005, value);
    assertEquals(value, sparseMatrix.get(500, 50005), delta);
    sparseMatrix.set(500, 999999, value);
    assertEquals(value, sparseMatrix.get(500, 999999), delta);

    // same row, same column
    sparseMatrix.set(500, 500, value);
    assertEquals(value, sparseMatrix.get(500, 500), delta);

    sparseMatrix.set(535333, 999999, value);
    assertEquals(value, sparseMatrix.get(535333, 999999), delta);
  }

  @Test
  public void getZeroValueNode() {
    assertEquals(0.0f, sparseMatrix.get(10, 30), delta);
  }

  @Test
  public void setExistingValueToZero() {
    sparseMatrix = new SparseMatrix(100);

    sparseMatrix.set(10, 10, 100f);
    sparseMatrix.set(10, 10, 0f);

    assertEquals(0f, sparseMatrix.get(10, 10), delta);
  }

  @Test
  public void updateExistingValue() {
    sparseMatrix = new SparseMatrix(100);

    sparseMatrix.set(10, 10, 100f);
    sparseMatrix.set(10, 10, 22f);

    assertEquals(22f, sparseMatrix.get(10, 10), delta);
  }

  @Test
  public void size() {
    sparseMatrix = new SparseMatrix(100);
    assertEquals(100, sparseMatrix.size());
  }

  @Test(timeout = 10000)
  public void setIdentityOnlyCheckDiagonals() {
    int size = 100000;
    sparseMatrix = new SparseMatrix(size);
    sparseMatrix.setIdentity();

    for (int i = 0; i < size; i++) {
      assertEquals(1f, sparseMatrix.get(i, i), delta);
    }
  }

  @Test(timeout = 10000)
  public void addSparseWorstCase() {
    int size = 100;
    SparseMatrix a = new SparseMatrix(size);
    SparseMatrix b = new SparseMatrix(size);
    float[][] expectedA = new float[size][size];
    float[][] expectedB = new float[size][size];

    for (int i = 0; i < size; i++) {
      for (int j = 0; j < size; j++) {
        float randomValA = new Random().nextFloat();
        expectedA[i][j] = randomValA;
        a.set(i, j, randomValA);

        float randomValB = new Random().nextFloat();
        expectedB[i][j] = randomValB;
        b.set(i, j, randomValB);
      }
    }

    SparseMatrix c = (SparseMatrix) a.add(b);

    for (int i = 0; i < size; i++) {
      for (int j = 0; j < size; j++) {
        float expectedSum = expectedA[i][j] + expectedB[i][j];
        float result = c.get(i, j);
        assertEquals(expectedSum, result, delta);
      }
    }
  }

  @Test(timeout = 10000)
  public void addSparse() {
    int size = 100;
    SparseMatrix a = new SparseMatrix(size);
    SparseMatrix b = new SparseMatrix(size);
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

    SparseMatrix c = (SparseMatrix) a.add(b);

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
    SparseMatrix a = new SparseMatrix(size);
    ArrayMatrix b = new ArrayMatrix(size);
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

    // Fill SparseMatrix
    for (int i = 0; i < 0.4 * size; i++) {
      // For Matrix A
      int rowIndexA = new Random().nextInt(size);
      int colIndexA = new Random().nextInt(size);
      float randomValA = new Random().nextFloat();

      expectedA[rowIndexA][colIndexA] = randomValA;
      a.set(rowIndexA, colIndexA, randomValA);
    }

    SparseMatrix c = (SparseMatrix) a.add(b);

    for (int i = 0; i < size; i++) {
      for (int j = 0; j < size; j++) {
        float expectedSum = expectedA[i][j] + expectedB[i][j];
        float result = c.get(i, j);
        assertEquals(expectedSum, result, delta);
      }
    }
  }

  @Test(timeout = 10000)
  public void premulSparse() {
    int size = 100;
    SparseMatrix a = new SparseMatrix(size);
    SparseMatrix b = new SparseMatrix(size);
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

    SparseMatrix c = (SparseMatrix) b.premul(a);

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
  public void premulMixed() {
    int size = 100;
    SparseMatrix a = new SparseMatrix(size);
    ArrayMatrix b = new ArrayMatrix(size);
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

    SquareMatrix c = a.premul(b);

    for (int i = 0; i < size; i++) {
      for (int j = 0; j < size; j++) {
        float expectedValue = 0f;
        for (int k = 0; k < size; k++) {
          expectedValue += expectedB[i][k] * expectedA[k][j];
        }
        float result = c.get(i, j);
        assertEquals(expectedValue, result, delta);
      }
    }
  }

  @Test(timeout = 10000)
  public void postmulSparse() {
    int size = 100;
    SparseMatrix a = new SparseMatrix(size);
    SparseMatrix b = new SparseMatrix(size);
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

    SparseMatrix c = (SparseMatrix) a.postmul(b);

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
    SparseMatrix a = new SparseMatrix(size);
    ArrayMatrix b = new ArrayMatrix(size);
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
    sparseMatrix = new SparseMatrix(size);
    // Fill the matrix with 40 percent density
    for (int i = 0; i < 0.4 * size; i++) {
      int rowIndex = new Random().nextInt(size);
      int colIndex = new Random().nextInt(size);
      float randomVal = new Random().nextFloat();

      sparseMatrix.set(rowIndex, colIndex, randomVal);
    }

    sparseMatrix.setIdentity();

    for (int i = 0; i < size; i++) {
      assertEquals(1f, sparseMatrix.get(i, i), delta);
    }
  }

  @Test(timeout = 10000)
  public void resetIdentityAndFillAgain() {
    resetIdentity();

    int size = 4000;
    sparseMatrix = new SparseMatrix(size);
    float[][] expectedMatrix = new float[size][size];
    // Fill the matrix with 40 percent density
    for (int i = 0; i < 0.4 * size; i++) {
      int rowIndex = new Random().nextInt(size);
      int colIndex = new Random().nextInt(size);
      float randomVal = new Random().nextFloat();

      expectedMatrix[rowIndex][colIndex] = randomVal;
      sparseMatrix.set(rowIndex, colIndex, randomVal);
    }

    for (int i = 0; i < size; i++) {
      for (int j = 0; j < size; j++) {
        assertEquals(expectedMatrix[i][j], sparseMatrix.get(i, j), delta);
      }
    }
  }
}