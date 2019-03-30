import koma.matrix.Matrix
import tensork.*

fun main() {
    val inputs = init(
        arrayOf(
            arrayOf(0.0, 0.0)
        ),
        arrayOf(
            arrayOf(0.0, 1.0)
        ),
        arrayOf(
            arrayOf(1.0, 0.0)
        ),
        arrayOf(
            arrayOf(1.0, 1.0)
        )
    )

    val outputs = init(
        //         true, false
        arrayOf(
            arrayOf(0.0, 1.0)
        ),
        arrayOf(
            arrayOf(1.0, 0.0)
        ),
        arrayOf(
            arrayOf(1.0, 0.0)
        ),
        arrayOf(
            arrayOf(0.0, 1.0)
        )
    )

    val nn = Network(listOf(
        Linear(2, 2),
        Tanh(),
        Linear(2, 2)
    ))

    train(inputs to outputs, nn)

    inputs.forEach { input ->
        val predicted = nn.forward(listOf(input))

        println("$input -> ${predicted.first()}" )
    }
}