package model

class Matrix<T: Number>(private val data: Array<Array<T>>) {

    private var matrixDirection: MatrixDirection = MatrixDirection.NORMAL

    init {
        val firstRowLength = data.firstOrNull()?.size ?: 0
        require(data.all { it.size == firstRowLength}) {"Not all rows share the same size(first row was size $firstRowLength"}
    }

    fun get(rowIndex: Int, columnIndex: Int): Number {
        return when(matrixDirection) {
            is MatrixDirection.NORMAL -> data.getOrNull(rowIndex)?.getOrNull(columnIndex)
            is MatrixDirection.TRANSPOSED -> data.getOrNull(columnIndex)?.getOrNull(rowIndex)
        } ?: error("Specified element ($rowIndex,$columnIndex) does not exist in Matrix.")
    }

    fun transpose() {
        matrixDirection = when(matrixDirection) {
            is MatrixDirection.NORMAL -> MatrixDirection.TRANSPOSED
            is MatrixDirection.TRANSPOSED -> MatrixDirection.NORMAL
        }
    }

    sealed class MatrixDirection {
        object NORMAL : MatrixDirection()
        object TRANSPOSED : MatrixDirection()
    }
}