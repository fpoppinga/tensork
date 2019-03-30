import koma.argMax
import koma.matrix.Matrix
import tensork.*

fun fizzBuzzEncode(i: Int): Array<DoubleArray> {
    if (i % 15 == 0) {
        return arrayOf(arrayOf(0.0, 1.0, 0.0, 0.0).toDoubleArray())
    }

    if (i % 3 == 0) {
        return arrayOf(arrayOf(0.0, 0.0, 0.0, 1.0).toDoubleArray())
    }

    if (i % 5 == 0) {
        return arrayOf(arrayOf(0.0, 0.0, 1.0, 0.0).toDoubleArray())
    }


    return arrayOf(arrayOf(1.0, 0.0, 0.0, 0.0).toDoubleArray())
}

fun binaryEncode(x: Int) = arrayOf((0 until 10).map { (x.shr(it) and 1) * 1.0 }.toDoubleArray())

val len = 1024 - 101
private val inputs = init(len, 1, 10) { Matrix.doubleFactory.create(binaryEncode(it + 100)) }
private val targets = init(len, 1, 4) { Matrix.doubleFactory.create(fizzBuzzEncode(it + 100)) }

private val nn = Network(listOf(
    Linear(10, 50),
    Tanh(),
    Linear(50, 4)
))

fun main() {
    train(inputs to targets, nn, optimizer = StochasticGradientDescent(0.0002))

    for (x in 1..100) {
        val predicted = nn.forward(init(1, 1, 10) { Matrix.doubleFactory.create(binaryEncode(x)) })
        val predictedClass = argMax(predicted[0])
        val actualClass = fizzBuzzEncode(x)[0].indexOf(1.0)

        val labels = listOf(x, "fizzbuzz", "buzz", "fizz")

        println("$x -> ${labels[predictedClass]} (${labels[actualClass]})")
    }
}