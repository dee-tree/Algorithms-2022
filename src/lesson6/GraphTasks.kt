@file:Suppress("UNUSED_PARAMETER", "unused")

package lesson6

import lesson6.Graph.Edge
import lesson6.Graph.Vertex
import lesson6.impl.GraphBuilder

/**
 * Эйлеров цикл.
 * Средняя
 *
 * Дан граф (получатель). Найти по нему любой Эйлеров цикл.
 * Если в графе нет Эйлеровых циклов, вернуть пустой список.
 * Соседние дуги в списке-результате должны быть инцидентны друг другу,
 * а первая дуга в списке инцидентна последней.
 * Длина списка, если он не пуст, должна быть равна количеству дуг в графе.
 * Веса дуг никак не учитываются.
 *
 * Пример:
 *
 *      G -- H
 *      |    |
 * A -- B -- C -- D
 * |    |    |    |
 * E    F -- I    |
 * |              |
 * J ------------ K
 *
 * Вариант ответа: A, E, J, K, D, C, H, G, B, C, I, F, B, A
 *
 * Справка: Эйлеров цикл -- это цикл, проходящий через все рёбра
 * связного графа ровно по одному разу
 *
 * Complexity:
 *  * Time: O(n)
 *  * Space: O(n)
 *
 */
fun Graph.findEulerLoop(): List<Edge> {

    // check on Eulerian cycle existence
    this.vertices.forEach {
        val neighbours = getNeighbors(it).size
        if (neighbours == 0 || neighbours % 2 != 0)
            return emptyList()
    }

    val first = vertices.firstOrNull() ?: return emptyList()
    var current = first

    val eulerLoop = mutableListOf<Edge>()
    val handledEdges = hashSetOf<Edge>()

    do {
        val neighbours = getNeighbors(current, handledEdges)

        for (neighbour in neighbours) {
            val currentEdge = getConnection(current, neighbour)!!
            if (neighbours.size == 1 || !isBridge(currentEdge, handledEdges)) {
                eulerLoop += currentEdge
                handledEdges += currentEdge
                current = neighbour
                break
            }
        }

    } while (current != first)

    return eulerLoop
}

fun Graph.getNeighbors(vertex: Vertex, excluded: Set<Edge>): Set<Vertex> {
    return getNeighbors(vertex)
        .map { neighbour -> getConnection(vertex, neighbour)!! }
        .minus(excluded)
        .map { if (vertex == it.begin) it.end else it.begin }
        .toSet()
}

/**
 * Минимальное остовное дерево.
 * Средняя
 *
 * Дан связный граф (получатель). Найти по нему минимальное остовное дерево.
 * Если есть несколько минимальных остовных деревьев с одинаковым числом дуг,
 * вернуть любое из них. Веса дуг не учитывать.
 *
 * Пример:
 *
 *      G -- H
 *      |    |
 * A -- B -- C -- D
 * |    |    |    |
 * E    F -- I    |
 * |              |
 * J ------------ K
 *
 * Ответ:
 *
 *      G    H
 *      |    |
 * A -- B -- C -- D
 * |    |    |
 * E    F    I
 * |
 * J ------------ K
 *
 * Complexity:
 *  * Time: O((v + e) log v)
 *  * Space: O(v)
 *
 */
fun Graph.minimumSpanningTree(): Graph = GraphBuilder().also {
    vertices.forEach { if (this@minimumSpanningTree.getConnections(it).isEmpty()) return@also }
    val first = vertices.firstOrNull() ?: return@also
    vertices.forEach { vertex -> it.addVertex(vertex.name) }
    shortestPath(first)
        .mapNotNull { it.value.prev?.to(it.value.vertex) }
        .forEach { edge -> it.addConnection(edge.first, edge.second) }
}.build()

/**
 * Максимальное независимое множество вершин в графе без циклов.
 * Сложная
 *
 * Дан граф без циклов (получатель), например
 *
 *      G -- H -- J
 *      |
 * A -- B -- D
 * |         |
 * C -- F    I
 * |
 * E
 *
 * Найти в нём самое большое независимое множество вершин и вернуть его.
 * Никакая пара вершин в независимом множестве не должна быть связана ребром.
 *
 * Если самых больших множеств несколько, приоритет имеет то из них,
 * в котором вершины расположены раньше во множестве this.vertices (начиная с первых).
 *
 * В данном случае ответ (A, E, F, D, G, J)
 *
 * Если на входе граф с циклами, бросить IllegalArgumentException
 */
fun Graph.largestIndependentVertexSet(): Set<Graph.Vertex> {
    TODO()
}

/**
 * Наидлиннейший простой путь.
 * Сложная
 *
 * Дан граф (получатель). Найти в нём простой путь, включающий максимальное количество рёбер.
 * Простым считается путь, вершины в котором не повторяются.
 * Если таких путей несколько, вернуть любой из них.
 *
 * Пример:
 *
 *      G -- H
 *      |    |
 * A -- B -- C -- D
 * |    |    |    |
 * E    F -- I    |
 * |              |
 * J ------------ K
 *
 * Ответ: A, E, J, K, D, C, H, G, B, F, I
 */
fun Graph.longestSimplePath(): Path {
    TODO()
}

/**
 * Балда
 * Сложная
 *
 * Задача хоть и не использует граф напрямую, но решение базируется на тех же алгоритмах -
 * поэтому задача присутствует в этом разделе
 *
 * В файле с именем inputName задана матрица из букв в следующем формате
 * (отдельные буквы в ряду разделены пробелами):
 *
 * И Т Ы Н
 * К Р А Н
 * А К В А
 *
 * В аргументе words содержится множество слов для поиска, например,
 * ТРАВА, КРАН, АКВА, НАРТЫ, РАК.
 *
 * Попытаться найти каждое из слов в матрице букв, используя правила игры БАЛДА,
 * и вернуть множество найденных слов. В данном случае:
 * ТРАВА, КРАН, АКВА, НАРТЫ
 *
 * И т Ы Н     И т ы Н
 * К р а Н     К р а н
 * А К в а     А К В А
 *
 * Все слова и буквы -- русские или английские, прописные.
 * В файле буквы разделены пробелами, строки -- переносами строк.
 * Остальные символы ни в файле, ни в словах не допускаются.
 */
fun baldaSearcher(inputName: String, words: Set<String>): Set<String> {
    TODO()
}
