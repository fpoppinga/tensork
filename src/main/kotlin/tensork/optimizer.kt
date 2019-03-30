package tensork

interface Optimizer {
    fun step(net: Network)
}

class StochasticGradientDescent(private val learningRate: Double = 0.01): Optimizer {
    override fun step(net: Network) {
        for ((param, grad, update) in net.paramsAndGrads()) {
            update(param - grad.sumMatrices() * learningRate)
        }
    }
}