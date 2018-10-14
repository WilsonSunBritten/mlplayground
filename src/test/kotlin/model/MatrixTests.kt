package model

import org.junit.Assert
import org.junit.Test

class MatrixTests {

    @Test
    fun transposeTest() {
        val testMatrix = Matrix(arrayOf(
                arrayOf(1,2,3),
                arrayOf(4,5,6),
                arrayOf(7,8,9),
                arrayOf(10,11,12)
        ))

        Assert.assertTrue(testMatrix.get(1,2) == 6)
        testMatrix.transpose()
        Assert.assertTrue(testMatrix.get(1,2) == 8)
    }
}