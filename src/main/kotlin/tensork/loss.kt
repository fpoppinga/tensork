package tensork

interface Loss {
    fun loss(expected: Tensor, predicted: Tensor): Double
    fun grad(expected: Tensor, predicted: Tensor): Tensor
}

class TotalSquaredError: Loss {
    override fun loss(expected: Tensor, predicted: Tensor): Double {
        return (predicted - expected).pow(2.0).sum()
    }

    override fun grad(expected: Tensor, predicted: Tensor): Tensor {
        return (predicted - expected) * 2.0
    }
}