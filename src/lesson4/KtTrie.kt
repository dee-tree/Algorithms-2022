package lesson4

import java.util.*
import kotlin.NoSuchElementException

/**
 * Префиксное дерево для строк
 */
class KtTrie : AbstractMutableSet<String>(), MutableSet<String> {

    private class Node {
        val children: SortedMap<Char, Node> = sortedMapOf()
    }

    private companion object {
        const val terminator = 0.toChar()
    }

    private val root = Node()

    override var size: Int = 0
        private set

    override fun clear() {
        root.children.clear()
        size = 0
    }

    private fun String.withZero() = this + terminator

    private fun findNode(element: String): Node? {
        var current = root
        for (char in element) {
            current = current.children[char] ?: return null
        }
        return current
    }

    override fun contains(element: String): Boolean =
        findNode(element.withZero()) != null

    override fun add(element: String): Boolean {
        var current = root
        var modified = false
        for (char in element.withZero()) {
            val child = current.children[char]
            if (child != null) {
                current = child
            } else {
                modified = true
                val newChild = Node()
                current.children[char] = newChild
                current = newChild
            }
        }
        if (modified) {
            size++
        }
        return modified
    }

    override fun remove(element: String): Boolean {
        val current = findNode(element) ?: return false
        if (current.children.remove(terminator) != null) {
            size--
            return true
        }
        return false
    }

    /**
     * Итератор для префиксного дерева
     *
     * Спецификация: [java.util.Iterator] (Ctrl+Click по Iterator)
     *
     * Сложная
     */
    override fun iterator(): MutableIterator<String> {
        return TrieIterator()
    }

    inner class TrieIterator internal constructor() : MutableIterator<String> {

        private val nodesStack = Stack<Pair<Node, Char>>()

        private var current: Node? = null

        init {
            nodesStack.push(root to terminator)
        }

        /**
         * time complexity: O(1), parameter represents strings in the set
         * space complexity: O(1)
         */
        override fun hasNext(): Boolean {
            nodesStack.reversed().forEach { curr ->
                if (curr.hasNext()) return true
            }
            return false
        }

        /**
         * time complexity: O(1), parameter represents strings in the set
         * space complexity: O(1)
         */
        override fun next(): String {
            var current = nodesStack.pop()
            this.current = current.first

            while (!current.hasNext()) {
                if (nodesStack.empty()) throw NoSuchElementException("Oops! There no element to be next")
                current = nodesStack.pop()
            }

            val next = current.next()
            nodesStack.push(next)
            fillStack()
            return restoreValueFromStack()
        }

        /**
         * time complexity: O(1), parameter represents strings in the set
         * space complexity: O(1)
         */
        override fun remove() {
            if (current == null) throw IllegalStateException("No element to be removed!")

            var last = nodesStack.pop()

            val hasAnotherAtThisBranch = last.first.children.size > 1

            if (hasAnotherAtThisBranch) {
                last.first.children.remove(last.second)
                nodesStack.push(last)
            } else {
                while (last.first.children.size == 1 && nodesStack.isNotEmpty()) {
                    last.first.children.remove(last.second)
                    last = nodesStack.pop()
                }

                nodesStack.push(last.previousOrFirst())
                last.first.children.remove(last.second)
            }
            size--
            current = null
        }


        private fun Pair<Node, Char>.hasNext(): Boolean {
            val nextIdx = first.children.keys.indexOf(second) + 1
            return first.children.keys.size > nextIdx
        }

        private fun Pair<Node, Char>.next(): Pair<Node, Char> {
            val nextIdx = first.children.keys.indexOf(second) + 1
            return first to first.children.keys.toList()[nextIdx]!!
        }

        private fun Pair<Node, Char>.previousOrFirst(): Pair<Node, Char> {
            val prevIdx = first.children.keys.indexOf(second).let { if (it < 1) 0 else it - 1 }
            return first to first.children.keys.toList()[prevIdx]!!
        }

        private fun fillStack() {
            var current = nodesStack.peek()
            while (current.second != terminator) {
                val nextNode = current.first.children[current.second]!!
                val nextKey = nextNode.children.firstKey()

                val next = nextNode to nextKey
                nodesStack.push(next)
                current = next
            }
        }

        private fun restoreValueFromStack(): String =
            nodesStack.subList(0, nodesStack.lastIndex).joinToString("") { it.second.toString() }
    }
}