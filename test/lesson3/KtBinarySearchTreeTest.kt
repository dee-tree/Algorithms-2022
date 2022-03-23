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
    fun s() {
        val dataset = listOf(29, 62, 34, 58, 76, 1, 38, 37, 81, 43, 77, 36, 39, 89, 65, 58, 13, 9, 50, 28)

        val tree = KtBinarySearchTree<Int>()

        tree.addAll(dataset)

        println("Before count: ${tree.size}")
        tree.print()

        var maxIterator = tree.iterator()

        maxIterator.hasNext()

        var cnt = 0

        while (maxIterator.hasNext()) {
            val next = maxIterator.next()
            cnt++
            println(next)

            if (next == 38)
                break
        }

        maxIterator.remove()

        while(maxIterator.hasNext()) {
            println("Next: ${maxIterator.next()}")
            cnt++
        }

        println("Count via iter: ${cnt}")


        println("After count: ${tree.size}")
        tree.print()

        val iter = tree.iterator()
        while (iter.hasNext())
            println("next: ${iter.next()}")

//        tree.clear()


        5 + 3


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