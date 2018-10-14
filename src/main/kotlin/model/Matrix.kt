package model

class Matrix<T : Number>(private val data: Array<Array<T>>) {

    private var matrixDirection: MatrixDirection = MatrixDirection.NORMAL

    val columnSize = data.firstOrNull()?.size ?: 0
    val rowSize = data.size

    init {
        val firstRowLength = data.firstOrNull()?.size ?: 0
        require(data.all { it.size == firstRowLength }) { "Not all rows share the same size(first row was size $firstRowLength" }
    }

    fun get(rowIndex: Int, columnIndex: Int): T {
        return when (matrixDirection) {
            is MatrixDirection.NORMAL -> data.getOrNull(rowIndex)?.getOrNull(columnIndex)
            is MatrixDirection.TRANSPOSED -> data.getOrNull(columnIndex)?.getOrNull(rowIndex)
        } ?: error("Specified element ($rowIndex,$columnIndex) does not exist in Matrix.")
    }

    fun size(): Pair<Int, Int> = when (matrixDirection) {
        is MatrixDirection.NORMAL -> rowSize to columnSize
        is MatrixDirection.TRANSPOSED -> columnSize to rowSize
    }

    fun multiply(otherMatrix: Matrix<T>) {
        val otherSize = otherMatrix.size()
        require(this.size().second == otherSize.first) { "Mismatched sizes: this: ${this.size()} that: $otherSize" }

        
    }

    fun getColumn(columnIndex: Int): List<T> = when (matrixDirection) {
        is MatrixDirection.NORMAL -> data.map {
            it.getOrNull(columnIndex)
                    ?: error("Column index out of range. requested index: $columnIndex, matrix columns: ${this.size().second}")
        }
        is MatrixDirection.TRANSPOSED -> this.getRow(columnIndex)
    }

    fun getRow(rowIndex: Int): List<T> = when (matrixDirection) {
        is MatrixDirection.NORMAL -> data.getOrNull(rowIndex)?.toList()
                ?: error("Row Index out of range. requested index: $rowIndex, matrix rows: ${this.size().first}")
        is MatrixDirection.TRANSPOSED -> getColumn(rowIndex)
    }

    fun transpose() {
        matrixDirection = when (matrixDirection) {
            is MatrixDirection.NORMAL -> MatrixDirection.TRANSPOSED
            is MatrixDirection.TRANSPOSED -> MatrixDirection.NORMAL
        }
    }

    sealed class MatrixDirection {
        object NORMAL : MatrixDirection()
        object TRANSPOSED : MatrixDirection()
    }
}