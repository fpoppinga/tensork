package tensork

typealias TrainingData = Pair<Tensor, Tensor>

fun TrainingData.batched(batchSize: Int): Sequence<TrainingData> = sequence {
    val (inputs, outputs) = this@batched
    assert(inputs.shape().contentEquals(outputs.shape())) { "Inputs and outputs must be of same shape!" }

    val batchedInputs = inputs.chunked(batchSize).shuffled()
    val batchedOutputs = outputs.chunked(batchSize).shuffled()

    for (i in 0 until batchedInputs.size) {
        yield( batchedInputs[i] to batchedOutputs[i] )
    }
}
