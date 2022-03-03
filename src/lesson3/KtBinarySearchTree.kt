package lesson3

import java.util.*
import kotlin.NoSuchElementException
import kotlin.math.max

// attention: Comparable is supported but Comparator is not
open class KtBinarySearchTree<T : Comparable<T>> : AbstractMutableSet<T>(), CheckableSortedSet<T> {

    protected class Node<T>(
        val value: T
    ) {
        var left: Node<T>? = null
        var right: Node<T>? = null
    }

    protected var root: Node<T>? = null

    private var _size = 0

    override val size
        get() = _size


    private fun find(value: T): Node<T>? =
        root?.let { find(it, value) }

    private fun find(start: Node<T>, value: T): Node<T> {
        val comparison = value.compareTo(start.value)
        return when {
            comparison == 0 -> start
            comparison < 0 -> start.left?.let { find(it, value) } ?: start
            else -> start.right?.let { find(it, value) } ?: start
        }
    }

    override operator fun contains(element: T): Boolean {
        val closest = find(element)
        return closest != null && element.compareTo(closest.value) == 0
    }

    /**
     * Добавление элемента в дерево
     *
     * Если элемента нет в множестве, функция добавляет его в дерево и возвращает true.
     * В ином случае функция оставляет множество нетронутым и возвращает false.
     *
     * Спецификация: [java.util.Set.add] (Ctrl+Click по add)
     *
     * Пример
     */
    override fun add(element: T): Boolean {
        val closest = find(element)
        val comparison = if (closest == null) -1 else element.compareTo(closest.value)
        if (comparison == 0) {
            return false
        }
        val newNode = Node(element)
        when {
            closest == null -> root = newNode
            comparison < 0 -> {
                assert(closest.left == null)
                closest.left = newNode
            }
            else -> {
                assert(closest.right == null)
                closest.right = newNode
            }
        }
        _size++
        return true
    }

    /**
     * Удаление элемента из дерева
     *
     * Если элемент есть в множестве, функция удаляет его из дерева и возвращает true.
     * В ином случае функция оставляет множество нетронутым и возвращает false.
     * Высота дерева не должна увеличиться в результате удаления.
     *
     * Спецификация: [java.util.Set.remove] (Ctrl+Click по remove)
     * (в Котлине тип параметера изменён с Object на тип хранимых в дереве данных)
     *
     * Средняя
     */
    override fun remove(element: T): Boolean =
        /*
        time complexity: average: O(log(n)), worst: O(n) for growing/decreasing only set
        space complexity: O(n)
     */
        root?.removeNode(element)?.also { root = it.first; if (it.second) _size-- }?.second ?: false


    /**
     * finds and removes element [value] from the tree beginning with this node
     * @return node to be replaced instead of removable and true if element was removed
     */
    private fun <T : Comparable<T>> Node<T>.removeNode(value: T): Pair<Node<T>?, Boolean> {
        if (value < this.value) {
            return this to (this.left?.removeNode(value)?.also { this.left = it.first }?.second ?: false)
        }
        if (value > this.value) {
            return this to (this.right?.removeNode(value)?.also { this.right = it.first }?.second ?: false)
        }

        // node to be removed was found

        // case where node hasn't children
        if (this.left == null && this.right == null) {
            return null to true
        }

        // case where node has only right child
        if (this.left == null) {
            return this.right to true
        }
        // case where node has only left child
        if (this.right == null) {
            return this.left to true
        }

        // case where node has both children
        val minNode = this.right!!.minNode()

        return Node(minNode.value).also {
            it.left = this.left
            it.right = this.right!!.removeNode(minNode.value).first
        } to true
    }

    /**
     * @return node with the lowest value beginning this node
     */
    protected tailrec fun <T> Node<T>.minNode(): Node<T> {
        return if (this.left == null)
            this
        else
            this.left!!.minNode()
    }


    override fun comparator(): Comparator<in T>? =
        null

    override fun iterator(): MutableIterator<T> =
        BinarySearchTreeIterator()

    inner class BinarySearchTreeIterator internal constructor() : MutableIterator<T> {

        private val iterationStack = Stack<Node<T>>()

        init {
            root?.fillIterationStack()
        }

        private fun Node<T>.fillIterationStack() {
            // push all leftmost nodes
            var current: Node<T>? = this
            while (current != null) {
                iterationStack.push(current)
                current = current.left
            }
        }

        private var current: T? = null

        /**
         * Проверка наличия следующего элемента
         *
         * Функция возвращает true, если итерация по множеству ещё не окончена (то есть, если вызов next() вернёт
         * следующий элемент множества, а не бросит исключение); иначе возвращает false.
         *
         * Спецификация: [java.util.Iterator.hasNext] (Ctrl+Click по hasNext)
         *
         * Средняя
         */
        override fun hasNext(): Boolean = iterationStack.isNotEmpty()

        /**
         * Получение следующего элемента
         *
         * Функция возвращает следующий элемент множества.
         * Так как BinarySearchTree реализует интерфейс SortedSet, последовательные
         * вызовы next() должны возвращать элементы в порядке возрастания.
         *
         * Бросает NoSuchElementException, если все элементы уже были возвращены.
         *
         * Спецификация: [java.util.Iterator.next] (Ctrl+Click по next)
         *
         * Средняя
         */
        override fun next(): T {
            val current: Node<T>
            try {
                current = iterationStack.pop()
            } catch (e: EmptyStackException) {
                throw NoSuchElementException("All elements of the set was already returned!")
            }

            this.current = current.value

            current.right?.fillIterationStack()
            return current.value
        }

        /**
         * Удаление предыдущего элемента
         *
         * Функция удаляет из множества элемент, возвращённый крайним вызовом функции next().
         *
         * Бросает IllegalStateException, если функция была вызвана до первого вызова next() или же была вызвана
         * более одного раза после любого вызова next().
         *
         * Спецификация: [java.util.Iterator.remove] (Ctrl+Click по remove)
         *
         * Сложная
         */
        override fun remove() = current?.let {
            remove(it)
            current = null
        } ?: throw IllegalStateException("Current element does not exist!")

    }

    /**
     * Подмножество всех элементов в диапазоне [fromElement, toElement)
     *
     * Функция возвращает множество, содержащее в себе все элементы дерева, которые
     * больше или равны fromElement и строго меньше toElement.
     * При равенстве fromElement и toElement возвращается пустое множество.
     * Изменения в дереве должны отображаться в полученном подмножестве, и наоборот.
     *
     * При попытке добавить в подмножество элемент за пределами указанного диапазона
     * должен быть брошен IllegalArgumentException.
     *
     * Спецификация: [java.util.SortedSet.subSet] (Ctrl+Click по subSet)
     * (настоятельно рекомендуется прочитать и понять спецификацию перед выполнением задачи)
     *
     * Очень сложная (в том случае, если спецификация реализуется в полном объёме)
     */
    override fun subSet(fromElement: T, toElement: T): SortedSet<T> {
        return KtBinarySearchSubTree(this, fromElement, toElement)
    }

    /**
     * Подмножество всех элементов строго меньше заданного
     *
     * Функция возвращает множество, содержащее в себе все элементы дерева строго меньше toElement.
     * Изменения в дереве должны отображаться в полученном подмножестве, и наоборот.
     *
     * При попытке добавить в подмножество элемент за пределами указанного диапазона
     * должен быть брошен IllegalArgumentException.
     *
     * Спецификация: [java.util.SortedSet.headSet] (Ctrl+Click по headSet)
     * (настоятельно рекомендуется прочитать и понять спецификацию перед выполнением задачи)
     *
     * Сложная
     */
    override fun headSet(toElement: T): SortedSet<T> {
        TODO()
    }

    /**
     * Подмножество всех элементов нестрого больше заданного
     *
     * Функция возвращает множество, содержащее в себе все элементы дерева нестрого больше toElement.
     * Изменения в дереве должны отображаться в полученном подмножестве, и наоборот.
     *
     * При попытке добавить в подмножество элемент за пределами указанного диапазона
     * должен быть брошен IllegalArgumentException.
     *
     * Спецификация: [java.util.SortedSet.tailSet] (Ctrl+Click по tailSet)
     * (настоятельно рекомендуется прочитать и понять спецификацию перед выполнением задачи)
     *
     * Сложная
     */
    override fun tailSet(fromElement: T): SortedSet<T> {
        TODO()
    }

    override fun first(): T {
        var current: Node<T> = root ?: throw NoSuchElementException()
        while (current.left != null) {
            current = current.left!!
        }
        return current.value
    }

    override fun last(): T {
        var current: Node<T> = root ?: throw NoSuchElementException()
        while (current.right != null) {
            current = current.right!!
        }
        return current.value
    }

    override fun height(): Int =
        height(root)

    private fun height(node: Node<T>?): Int {
        if (node == null) return 0
        return 1 + max(height(node.left), height(node.right))
    }

    override fun checkInvariant(): Boolean =
        root?.let { checkInvariant(it) } ?: true

    private fun checkInvariant(node: Node<T>): Boolean {
        val left = node.left
        if (left != null && (left.value >= node.value || !checkInvariant(left))) return false
        val right = node.right
        return right == null || right.value > node.value && checkInvariant(right)
    }


    private class KtBinarySearchSubTree<T : Comparable<T>>(
        private val original: KtBinarySearchTree<T>, val fromElement: T, val toElementExclusively: T
    ) : KtBinarySearchTree<T>() {

        init {
            if (fromElement > toElementExclusively)
                throw IllegalArgumentException("fromElement $fromElement can't be greater than toElement $toElementExclusively!")
        }

        override val size: Int
            get() {
                var elements = 0
                val iter = iterator()
                while (iter.hasNext()) {
                    elements++
                    iter.next()
                }
                return elements
            }

        override fun add(element: T): Boolean {
            if (element < fromElement || element >= toElementExclusively)
                throw IllegalArgumentException("You can't insert $element which is outside the range of the subset!")
            return original.add(element)
        }

        override fun remove(element: T): Boolean {
            if (element < fromElement || element >= toElementExclusively)
                return false

            return original.remove(element)
        }

        override fun contains(element: T): Boolean {
            if (element < fromElement || element >= toElementExclusively)
                return false

            return original.contains(element)
        }

        override fun iterator(): MutableIterator<T> =
            BinarySearchSubTreeIterator()

        inner class BinarySearchSubTreeIterator internal constructor() : MutableIterator<T> {

            private val iterationStack = Stack<Node<T>>()

            init {
                original.root?.fillIterationStack()
            }

            private fun Node<T>.fillIterationStack() {
                // push all leftmost nodes

                var current: Node<T>? = this
                while (current != null) {
                    if (fromElement <= current.value && current.value < toElementExclusively)
                        iterationStack.push(current)
                    if (current.value >= fromElement)
                        current = current.left
                    else if (current.value < toElementExclusively)
                        current = current.right
                }
            }

            private var current: T? = null

            /**
             * Проверка наличия следующего элемента
             *
             * Функция возвращает true, если итерация по множеству ещё не окончена (то есть, если вызов next() вернёт
             * следующий элемент множества, а не бросит исключение); иначе возвращает false.
             *
             * Спецификация: [java.util.Iterator.hasNext] (Ctrl+Click по hasNext)
             */
            override fun hasNext(): Boolean = iterationStack.isNotEmpty()

            /**
             * Получение следующего элемента
             *
             * Функция возвращает следующий элемент множества.
             * Так как BinarySearchTree реализует интерфейс SortedSet, последовательные
             * вызовы next() должны возвращать элементы в порядке возрастания.
             *
             * Бросает NoSuchElementException, если все элементы уже были возвращены.
             *
             * Спецификация: [java.util.Iterator.next] (Ctrl+Click по next)
             */
            override fun next(): T {
                val current: Node<T>
                try {
                    current = iterationStack.pop()
                } catch (e: EmptyStackException) {
                    throw NoSuchElementException("All elements of the set was already returned!")
                }

                this.current = current.value

                current.right?.fillIterationStack()
                return current.value
            }

            /**
             * Удаление предыдущего элемента
             *
             * Функция удаляет из множества элемент, возвращённый крайним вызовом функции next().
             *
             * Бросает IllegalStateException, если функция была вызвана до первого вызова next() или же была вызвана
             * более одного раза после любого вызова next().
             *
             * Спецификация: [java.util.Iterator.remove] (Ctrl+Click по remove)
             */
            override fun remove() = current?.let {
                remove(it)
                current = null
            } ?: throw IllegalStateException("Current element does not exist!")

        }


        override fun subSet(fromElement: T, toElement: T): SortedSet<T> {
            if (fromElement < this.fromElement || toElement > this.toElementExclusively)
                throw IllegalArgumentException("range of the subset can't be wider than the range of existed subset!")

            return KtBinarySearchSubTree(original, fromElement, toElement)
        }

        override fun first(): T {
            var current: Node<T> = original.root ?: throw NoSuchElementException()


            while (current.left != null && current.value >= fromElement || current.right != null && current.value < toElementExclusively) {

                if (current.value == fromElement) break

                current = if (current.value > fromElement)
                    current.left ?: break
                else if (current.value < toElementExclusively)
                    current.right ?: break
                else throw kotlin.NoSuchElementException()
            }

            if (fromElement <= current.value && current.value < toElementExclusively)
                return current.value

            throw kotlin.NoSuchElementException()

        }

        override fun last(): T {
            var current: Node<T> = original.root ?: throw NoSuchElementException()

            while (current.left != null && current.value >= toElementExclusively || current.right != null && current.value < toElementExclusively) {

                current = if (current.value < toElementExclusively) {
                    val minRight = current.right?.minNode()?.value
                    if (minRight != null && minRight >= toElementExclusively) break
                    current.right ?: break
                } else if (current.value > fromElement)
                    current.left ?: break
                else throw kotlin.NoSuchElementException()
            }

            if (fromElement <= current.value && current.value < toElementExclusively)
                return current.value

            throw kotlin.NoSuchElementException()

        }

        override fun height(): Int =
            height(original.root)

        private fun height(node: Node<T>?): Int {
            if (node == null ) return 0

            return when {
                node.value < fromElement -> height(node.right)
                node.value >= toElementExclusively -> height(node.left)
                else -> 1 + max(height(node.left), height(node.right))
            }
        }
    }

}