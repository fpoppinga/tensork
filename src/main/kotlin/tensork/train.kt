package tensork



fun train(
    trainingData: TrainingData,
    nn: Network,
    optimizer: Optimizer = StochasticGradientDescent(),
    loss: Loss = TotalSquaredError(),
    epochs: Long = 5000
) {
    (1..epochs).forEach { epoch ->
        var epochLoss = 0.0
        trainingData.batched(50).forEach{
            val predicted = nn.forward(it.first)
            epochLoss += loss.loss(expected = it.second, predicted = predicted)
            val grad = loss.grad(expected = it.second, predicted = predicted)

            nn.backward(it.first, grad)

            optimizer.step(nn)
        }

        if (epoch % 100 == 0L) {
            println("Epoch $epoch - loss = $epochLoss")
        }
    }
}