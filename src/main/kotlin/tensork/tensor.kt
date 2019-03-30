package tensork

import koma.extensions.map
import koma.matrix.Matrix

typealias Tensor = List<Matrix<Double>>

fun Tensor.sum(): Double {
    var res = 0.0
    this.forEach { res += it.elementSum() }
    return res
}

fun Tensor.shape() = IntArray(3) {
    when (it) {
        0 -> this.size
        1 -> this.first().numRows()
        2 -> this.first().numCols()
        else -> throw IllegalStateException()
    }
}

fun Tensor.sumMatrices(): Tensor = listOf(this.reduce { acc, matrix -> acc + matrix })

fun ones(vararg lengths: Int): Tensor = (0 until lengths.first()).map { koma.ones(lengths[1], lengths[2]) }
fun rand(vararg lengths: Int): Tensor = (0 until lengths.first()).map { koma.randn(lengths[1], lengths[2]) }
fun init(vararg lengths: Int, block: (Int) -> Matrix<Double>): Tensor {
    return (0 until lengths.first()).map {
        val mat = block(it)
        assert(mat.numRows() == lengths[1]) { "All result matrices must be of shape (${lengths[1]}, ${lengths[2]})" }
        return@map mat
    }
}

fun init(vararg arr: Array<Array<Double>>): Tensor {
    val dim0 = arr.size
    val dim1 = arr.first().size
    val dim2 = arr.first().first().size

    return init(
        dim0,
        dim1,
        dim2
    ) { Matrix.doubleFactory.create(arr[it].map { mat -> mat.toDoubleArray() }.toTypedArray()) }
}


operator fun Tensor.times(other: Tensor): Tensor {
    assert(size == other.size) { "You can only multiply tensors of same size." }

    return mapIndexed { idx, mat -> mat * other[idx] }
}

operator fun Tensor.times(other: Matrix<Double>): Tensor {
    return map { mat -> mat * other }
}

operator fun Tensor.times(other: Double): Tensor {
    return map { it * other }
}

fun Tensor.elementTimes(other: Tensor): Tensor {
    return mapIndexed { idx, mat -> mat.elementTimes(other[idx]) }
}

operator fun Tensor.plus(other: Tensor): Tensor {
    // poor man's broadcasting
    if (other.size == 1) {
        return map { mat -> mat + other[0] }
    }

    if (size == 1) {
        return other + this
    }

    assert(size == other.size) { "You can only add tensors of same size." }

    return mapIndexed { idx, mat -> mat + other[idx] }
}

operator fun Tensor.plus(other: Matrix<Double>): Tensor {
    return map { mat -> mat + other }
}

operator fun Tensor.unaryMinus(): Tensor {
    return map { -it }
}

operator fun Tensor.minus(other: Tensor): Tensor = this + -other

fun Tensor.pow(exponent: Double): Tensor {
    return this.map { it.epow(exponent) }
}

val Tensor.T: Tensor
    get() = map { it.T }

fun Tensor.elementWise(block: (Double) -> Double): Tensor {
    return map { it.map(block) }
}