package lesson3

import org.junit.jupiter.api.RepeatedTest
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

class KtBinarySearchTreeTest : AbstractBinarySearchTreeTest() {

    override fun create(): CheckableSortedSet<Int> =
        KtBinarySearchTree()

    @Test
    @Tag("Example")
    fun initTest() {
        doInitTest()
    }

    @Test
    @Tag("Example")
    fun addTest() {
        doAddTest()
    }

    @Test
    @Tag("Example")
    fun firstAndLastTest() {
        doFirstAndLastTest()
    }

    @Test
    @Tag("5")
    fun removeTest() {
        doRemoveTest()
    }

    @Test
    fun `removeTest in empty set`() {
        `do removeTest in empty set`()
    }

    @RepeatedTest(1_000)
    fun `removeTest in growing only set`() {
        `do removeTest in growing only set`()
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

    @RepeatedTest(1_000)
    fun `iteratorTest with growing only set`() {
        `do iteratorTest with growing only set`()
    }

    @RepeatedTest(1_000)
    fun `iteratorTest with decreasing only set`() {
        `do iteratorTest with decreasing only set`()
    }

    @Test
    @Tag("8")
    fun iteratorRemoveTest() {
        doIteratorRemoveTest()
    }

    @RepeatedTest(100)
//    @Tag("Just iteratorRemoveTest, but with more iterations")
    fun iteratorRemoveRepeatedTest() {
        doIteratorRemoveTest()
    }

    @Test
    fun `iteratorRemoveTest from empty set`() {
        `do iteratorRemoveTest from empty set`()
    }

    @Test
    @Tag("5")
    fun subSetTest() {
        doSubSetTest()
    }

    @Test
    @Tag("8")
    fun subSetRelationTest() {
        doSubSetRelationTest()
    }

    @RepeatedTest(1_000)
    fun `subSet borders and ranges Test`() {
        `do subSet borders and ranges Test`()
    }

    @RepeatedTest(1_000)
    fun `subSet operations Test`() {
        `subSet iterator Test`()
    }

    @Test
    @Tag("7")
    fun subSetFirstAndLastTest() {
        doSubSetFirstAndLastTest()
    }


    @Test
    @Tag("4")
    fun headSetTest() {
        doHeadSetTest()
    }

    @Test
    @Tag("7")
    fun headSetRelationTest() {
        doHeadSetRelationTest()
    }

    @Test
    @Tag("4")
    fun tailSetTest() {
        doTailSetTest()
    }

    @Test
    @Tag("7")
    fun tailSetRelationTest() {
        doTailSetRelationTest()
    }

}
