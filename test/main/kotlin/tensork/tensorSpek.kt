package tensork

import koma.eye
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe
import kotlin.test.assertEquals

object TensorSpec: Spek({
    describe("Tensor") {
        it("can be summed element wise") {
            val t = ones(3, 3, 3)

            assertEquals(27.0, t.sum())
        }

        it("can be summed along the rows") {
            val t = ones(5, 2, 2)

            assertEquals(ones(1, 2, 2) * 5.0, t.sumMatrices())
        }

        it("does the matrix multiplication") {
            val t = init(5, 2, 2) { eye(2,2) * 2.0 }
            val other = ones(5, 2, 2)

            val result = t * other
            assertEquals(ones(5, 2, 2) * 2.0, result)
        }
    }
})