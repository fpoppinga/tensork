package tensork

interface Layer {
    val params: MutableMap<String, Tensor>
    val grads: MutableMap<String, Tensor>

    fun forward(inputs: Tensor): Tensor
    fun backward(inputs: Tensor, grad: Tensor): Tensor
}

class Linear(inputSize: Int, outputSize: Int) : Layer {
    override val params = mutableMapOf(
        "weights" to rand(1, inputSize, outputSize),
        "offsets" to rand(1, 1, outputSize)
    )

    override val grads = mutableMapOf<String, Tensor>()

    override fun forward(inputs: Tensor): Tensor {
        return inputs * params["weights"]!!.first() + params["offsets"]!!.first()
    }

    override fun backward(inputs: Tensor, grad: Tensor): Tensor {
        grads["offsets"] = grad.sumMatrices()
        grads["weights"] = inputs.T * grad
        return grad * params["weights"]!!.first().T
    }
}

abstract class Activation : Layer {
    abstract val f: (Tensor) -> Tensor
    abstract val df: (Tensor) -> Tensor
    override val params = mutableMapOf<String, Tensor>()
    override val grads = mutableMapOf<String, Tensor>()

    override fun forward(inputs: Tensor): Tensor = f(inputs)

    override fun backward(inputs: Tensor, grad: Tensor): Tensor {
        // Chain Rule
        return df(inputs).elementTimes(grad)
    }
}

val tanh = {t: Tensor -> t.elementWise { kotlin.math.tanh(it) }}
val dTanh = {t: Tensor ->
    ones(*t.shape()) - tanh(t).pow(2.0)
}

class Tanh: Activation() {
    override val f = tanh
    override val df = dTanh
}
