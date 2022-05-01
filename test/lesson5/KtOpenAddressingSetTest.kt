package lesson5

import org.junit.jupiter.api.RepeatedTest
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

class KtOpenAddressingSetTest : AbstractOpenAddressingSetTest() {

    override fun <T : Any> create(bits: Int): MutableSet<T> {
        return KtOpenAddressingSet(bits)
    }

    @Test
    @Tag("Example")
    fun addTest() {
        doAddTest()
    }

    @Test
    @Tag("7")
    fun removeTest() {
        doRemoveTest()
    }

    @Test
    fun `removeTest in empty set`() {
        `do removeTest in empty set`()
    }

    @RepeatedTest(100)
    fun `removeTest with full set`() {
        `do removeTest with full set`()
    }

    @Test
    @Tag("5")
    fun iteratorTest() {
        doIteratorTest()
    }

    @Test
    fun `iteratorTest with empty set`() {
        `do iteratorTest with empty set`()
    }

    @RepeatedTest(100)
    fun `iteratorRemoveTest with full set`() {
        `do iteratorRemoveTest with full set`()
    }

    @Test
    @Tag("8")
    fun iteratorRemoveTest() {
        doIteratorRemoveTest()
    }

    @Test
    fun `iteratorRemoveTest from empty set`() {
        `do iteratorRemoveTest from empty set`()
    }
}