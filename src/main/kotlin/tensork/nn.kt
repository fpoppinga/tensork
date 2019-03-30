package tensork

import java.util.*

class Network(private val layers: List<Layer>) {
    private val intermediateInput: Stack<Tensor> = Stack()

    fun forward(inputs: Tensor): Tensor {
        intermediateInput.clear()
        
        var output: Tensor = inputs
        for (layer in layers) {
            intermediateInput.add(output)
            output = layer.forward(output)
        }
        return output
    }

    fun backward(inputs: Tensor, grad: Tensor): Tensor {
        assert(intermediateInput.size == layers.size) { "Only use backward, after forward!" }

        var result = grad
        for (layer in layers.reversed()) {
            result = layer.backward(intermediateInput.pop(), result)
        }
        return result
    }

    fun paramsAndGrads(): Sequence<Triple<Tensor, Tensor, (Tensor) -> Unit>> = sequence {
        for (layer in layers) {
            for ((key, value) in layer.grads) {
                yield(Triple(layer.params[key]!!, value, { t: Tensor -> layer.params[key] = t }))
            }
        }
    }
}