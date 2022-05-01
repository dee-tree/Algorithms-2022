package lesson5

import java.util.NoSuchElementException

/**
 * Множество(таблица) с открытой адресацией на 2^bits элементов без возможности роста.
 */
class KtOpenAddressingSet<T : Any>(private val bits: Int) : AbstractMutableSet<T>() {
    init {
        require(bits in 2..31)
    }

    private enum class Labels {
        REMOVED
    }

    private val capacity = 1 shl bits

    private val storage = Array<Any?>(capacity) { null }

    override var size: Int = 0
        private set

    /**
     * Индекс в таблице, начиная с которого следует искать данный элемент
     */
    private fun T.startingIndex(): Int {
        return hashCode() and (0x7FFFFFFF shr (31 - bits))
    }

    /**
     * Проверка, входит ли данный элемент в таблицу
     */
    override fun contains(element: T): Boolean {
        val startingIndex = element.startingIndex()
        var index = startingIndex
        var current = storage[index]
        while (current != null) {
            if (current == element) {
                return true
            }
            index = (index + 1) % capacity
            if (index == startingIndex) return false
            current = storage[index]
        }
        return false
    }

    /**
     * Добавление элемента в таблицу.
     *
     * Не делает ничего и возвращает false, если такой же элемент уже есть в таблице.
     * В противном случае вставляет элемент в таблицу и возвращает true.
     *
     * Бросает исключение (IllegalStateException) в случае переполнения таблицы.
     * Обычно Set не предполагает ограничения на размер и подобных контрактов,
     * но в данном случае это было введено для упрощения кода.
     */
    override fun add(element: T): Boolean {
        val startingIndex = element.startingIndex()
        var index = startingIndex
        var current = storage[index]
        while (current != null && current != Labels.REMOVED) {
            if (current == element) {
                return false
            }
            index = (index + 1) % capacity
            check(index != startingIndex) { "Table is full" }
            current = storage[index]
        }
        storage[index] = element
        size++
        return true
    }

    /**
     * Удаление элемента из таблицы
     *
     * Если элемент есть в таблице, функция удаляет его из дерева и возвращает true.
     * В ином случае функция оставляет множество нетронутым и возвращает false.
     * Высота дерева не должна увеличиться в результате удаления.
     *
     * Спецификация: [java.util.Set.remove] (Ctrl+Click по remove)
     *
     * Средняя
     *
     * Complexity:
     * * Time: average is O(1) - without collisions, the worst is O(n) - with collisions
     * * Space: O(1)
     * parameter is the size of the set
     */
    override fun remove(element: T): Boolean {
        val startingIndex = element.startingIndex()
        var index = startingIndex
        var current = storage[index]
        while (current != null) {
            if (current == element) {
                storage[index] = Labels.REMOVED
                size--
                return true
            }
            index = (index + 1) % capacity
            if (index == startingIndex) return false
            current = storage[index]
        }
        return false
    }

    /**
     * Создание итератора для обхода таблицы
     *
     * Не забываем, что итератор должен поддерживать функции next(), hasNext(),
     * и опционально функцию remove()
     *
     * Спецификация: [java.util.Iterator] (Ctrl+Click по Iterator)
     *
     * Средняя (сложная, если поддержан и remove тоже)
     */
    override fun iterator(): MutableIterator<T> = OpenAddressingSetIterator()


    inner class OpenAddressingSetIterator internal constructor() : MutableIterator<T> {

        private var currentIdx = -1

        private fun nextIdxOrNull(): Int? {
            for (i in (currentIdx + 1)..storage.lastIndex) {
                // in other words: if storage[i] is T (there are only null, T and Labels.REMOVED can be found in storage)
                if (storage[i] != null && storage[i] != Labels.REMOVED) {
                    return i
                }
            }
            return null
        }

        /**
         * Complexity:
         * * Time: O(1/L), where L is load factor (size / storage.size), or the worst case is O(n) when table is free, and the best one is O(1) when all places are busy
         * * Space: O(1)
         * parameter is size of storage, i.e. capacity of the set
         */
        override fun hasNext(): Boolean = nextIdxOrNull() != null

        /**
         * Complexity:
         * * Time: O(1/L), where L is load factor (size / storage.size), or the worst case is O(n) when table is free, and the best one is O(1) when all places are busy
         * * Space: O(1)
         * parameter is size of storage, i.e. capacity of the set
         */
        override fun next(): T {
            return nextIdxOrNull()?.let {
                currentIdx = it
                storage[it] as T
            } ?: throw NoSuchElementException("Oops! There no element to be next")
        }

        /**
         * Complexity:
         * * Time: O(1)
         * * Space: O(1)
         * parameter can be both size of the set and size of the storage, i.e. capacity of the set
         */
        override fun remove() {
            if (currentIdx == -1)
                throw IllegalStateException("No element to be removed!")

            storage[currentIdx] = Labels.REMOVED
            size--

        }

    }
}
