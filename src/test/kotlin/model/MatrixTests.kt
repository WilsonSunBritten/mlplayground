package model

import org.junit.Assert
import org.junit.Test

class MatrixTests {

    @Test
    fun transposeTest() {
        val testMatrix = Matrix(listOf(
                listOf(1.0,2.0,3.0),
                listOf(4.0,5.0,6.0),
                listOf(7.0,8.0,9.0),
                listOf(10.0,11.0,12.0)
        ))

        Assert.assertTrue(testMatrix.get(1,2) == 6.0)
        testMatrix.transpose()
        Assert.assertTrue(testMatrix.get(1,2) == 8.0)
    }

    @Test
    fun testMultiply() {
        val testMatrix = Matrix(listOf(
                listOf(1.0, 2.0),
                listOf(3.0, 4.0)
        ))

        val testOtherMatrix = Matrix(listOf(
                listOf(1.0, 1.0, 0.0),
                listOf(0.0, 0.0, 0.0)
        ))

        val expectedMatrix = Matrix(listOf(
                listOf(1.0, 1.0, 0.0),
                listOf(3.0, 3.0, 0.0)
        ))

        Assert.assertTrue(expectedMatrix.equals(testMatrix.multiply(testOtherMatrix)))
    }

    @Test
    fun testDot() {
        val testMatrix = Matrix(listOf(
                listOf(1.0, 2.0),
                listOf(3.0, 4.0)
        ))

        val testOtherMatrix = Matrix(listOf(
                listOf(1.0, 1.0),
                listOf(2.0, 2.0)
        ))
        val addResult = Matrix(listOf(
                listOf(2.0, 3.0),
                listOf(5.0, 6.0)
        ))
        val subtractResult = Matrix(listOf(
                listOf(0.0, 1.0),
                listOf(1.0, 2.0)
        ))
        val multiplyResult = Matrix(listOf(
                listOf(1.0, 2.0),
                listOf(6.0, 8.0)
        ))
        val divideResult = Matrix(listOf(
                listOf(1.0, 2.0),
                listOf(1.5, 2.0)
        ))
        Assert.assertTrue(testMatrix.dotOperation(testOtherMatrix, Matrix.MatrixOperation.PLUS).equals(addResult))
        Assert.assertTrue(testMatrix.dotOperation(testOtherMatrix, Matrix.MatrixOperation.SUBTRACT).equals(subtractResult))
        Assert.assertTrue(testMatrix.dotOperation(testOtherMatrix, Matrix.MatrixOperation.MULTIPLY).equals(multiplyResult))
        Assert.assertTrue(testMatrix.dotOperation(testOtherMatrix, Matrix.MatrixOperation.DIVIDE).equals(divideResult))

    }
}