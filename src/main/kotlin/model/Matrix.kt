package model

import com.sun.org.apache.xpath.internal.operations.Minus
import java.lang.Exception

class Matrix(private val data: List<List<Double>>) {

    private var matrixDirection: MatrixDirection = MatrixDirection.NORMAL

    val columnSize = data.firstOrNull()?.size ?: 0
    val rowSize = data.size

    init {
        val firstRowLength = data.firstOrNull()?.size ?: 0
        require(data.all { it.size == firstRowLength }) { "Not all rows share the same size(first row was size $firstRowLength" }
    }

    fun dotOperation(otherMatrix: Matrix, operation: MatrixOperation): Matrix {
        val thisSize = this.size()
        require(otherMatrix.size() == this.size())
        val resultHolder = Array(thisSize.first) { Array<Number?>(thisSize.second) { null } }

        val result = resultHolder.mapIndexed { rowIndex, arrayOfNumbers ->
            arrayOfNumbers.mapIndexed { columnIndex, _ ->
                val thisValue = this.get(rowIndex, columnIndex)
                val otherValue = otherMatrix.get(rowIndex, columnIndex)
                when(operation) {
                    is MatrixOperation.PLUS -> thisValue + otherValue
                    is MatrixOperation.SUBTRACT -> thisValue - otherValue
                    is MatrixOperation.MULTIPLY -> thisValue * otherValue
                    is MatrixOperation.DIVIDE -> thisValue / otherValue
                }
            }
        }
        return Matrix(result)
    }

    sealed class MatrixOperation {
        object PLUS : MatrixOperation()
        object SUBTRACT : MatrixOperation()
        object MULTIPLY : MatrixOperation()
        object DIVIDE : MatrixOperation()
    }

    fun get(rowIndex: Int, columnIndex: Int): Double {
        return when (matrixDirection) {
            is MatrixDirection.NORMAL -> data.getOrNull(rowIndex)?.getOrNull(columnIndex)
            is MatrixDirection.TRANSPOSED -> data.getOrNull(columnIndex)?.getOrNull(rowIndex)
        } ?: error("Specified element ($rowIndex,$columnIndex) does not exist in Matrix.")
    }

    fun size(): Pair<Int, Int> = when (matrixDirection) {
        is MatrixDirection.NORMAL -> rowSize to columnSize
        is MatrixDirection.TRANSPOSED -> columnSize to rowSize
    }

    fun multiply(otherMatrix: Matrix): Matrix {
        val thisSize = this.size()
        val otherSize = otherMatrix.size()
        require(thisSize.second == otherSize.first) { "Mismatched sizes: this: $thisSize that: $otherSize" }

        val resultHolder = Array(thisSize.first) { Array<Number?>(otherSize.second) { null } }

        val result = resultHolder.mapIndexed { rowIndex, arrayOfNumbers ->
            arrayOfNumbers.mapIndexed { columnIndex, _ ->
                val row = this.getRow(rowIndex)
                val column = otherMatrix.getColumn(columnIndex)
                row.zip(column).sumByDouble { it.first * it.second }
            }
        }
        return Matrix(result)
    }

    fun getColumn(columnIndex: Int): List<Double> = when (matrixDirection) {
        is MatrixDirection.NORMAL -> data.map {
            it.getOrNull(columnIndex)
                    ?: error("Column index out of range. requested index: $columnIndex, matrix columns: ${this.size().second}")
        }
        is MatrixDirection.TRANSPOSED -> this.getRow(columnIndex)
    }

    fun getRow(rowIndex: Int): List<Double> = when (matrixDirection) {
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

    fun equals(otherMatrix: Matrix): Boolean =
            try {
                data.allIndexed { rowIndex, row ->
                    row.allIndexed { columnIndex, valueEntry ->
                        when (matrixDirection) {
                            MatrixDirection.NORMAL -> valueEntry == otherMatrix.get(rowIndex, columnIndex)
                            MatrixDirection.TRANSPOSED -> valueEntry == otherMatrix.get(columnIndex, rowIndex)
                        }
                    }
                }
            } catch (exception: Exception) {
                false
            }


    inline fun <T> Iterable<T>.allIndexed(predicate: (Int, T) -> Boolean): Boolean {
        if (this is Collection && isEmpty()) return true
        this.forEachIndexed { index, t -> if (!predicate(index, t)) return false }
        return true
    }

    sealed class MatrixDirection {
        object NORMAL : MatrixDirection()
        object TRANSPOSED : MatrixDirection()
    }
}