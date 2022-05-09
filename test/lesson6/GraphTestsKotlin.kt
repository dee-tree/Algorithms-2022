package lesson6

import org.junit.jupiter.api.RepeatedTest
import org.junit.jupiter.api.Tag
import kotlin.test.Test

class GraphTestsKotlin : AbstractGraphTests() {

    @Test
    @Tag("6")
    fun testFindEulerLoop() {
        findEulerLoop { findEulerLoop() }
    }

    @RepeatedTest(100)
    fun `findEulerLoop disconnected graph test`() {
        `do findEulerLoop disconnected graph test` { findEulerLoop() }
    }

    @Test
    @Tag("7")
    fun testMinimumSpanningTree() {
        minimumSpanningTree { minimumSpanningTree() }
    }

    @RepeatedTest(100)
    fun `testMinimumSpanningTree disconnected graph test`() {
        `do testMinimumSpanningTree disconnected graph test` { minimumSpanningTree() }
    }

    @Test
    @Tag("10")
    fun testLargestIndependentVertexSet() {
        largestIndependentVertexSet { largestIndependentVertexSet() }
    }

    @Test
    @Tag("8")
    fun testLongestSimplePath() {
        longestSimplePath { longestSimplePath() }
    }

    @Test
    @Tag("6")
    fun testBaldaSearcher() {
        baldaSearcher { inputName, words -> baldaSearcher(inputName, words) }
    }
}