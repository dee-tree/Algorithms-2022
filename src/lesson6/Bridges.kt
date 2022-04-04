package lesson6

import lesson6.Graph.Edge
import lesson6.Graph.Vertex

fun Graph.findBridges(): Set<Graph.Edge> =
    BridgeTraverser(this).findBridges()

private class BridgeTraverser(val graph: Graph) {
    val visitedVertices = mutableSetOf<Graph.Vertex>()

    var traverseIndex = 0

    class Info(var inIndex: Int, var bridgeIndex: Int) {
        constructor(index: Int) : this(index, index)
    }

    val verticesInfo = mutableMapOf<Graph.Vertex, Info>()

    val bridges = mutableSetOf<Graph.Edge>()

    fun findBridges(): Set<Graph.Edge> {
        for (vertex in graph.vertices) {
            if (vertex in visitedVertices) continue
            traverse(vertex, null)
        }
        return bridges
    }

    fun traverse(current: Graph.Vertex, previous: Graph.Vertex?) {
        visitedVertices += current
        verticesInfo[current] = Info(traverseIndex)
        traverseIndex++
        for ((next, edge) in graph.getConnections(current)) {
            if (next == previous) continue
            val currentInfo = verticesInfo[current]!!
            if (next in visitedVertices) {
                val nextInfo = verticesInfo[next]!!
                currentInfo.bridgeIndex = minOf(currentInfo.bridgeIndex, nextInfo.inIndex)
            } else {
                traverse(next, current)
                val nextInfo = verticesInfo[next]!!
                currentInfo.bridgeIndex = minOf(currentInfo.bridgeIndex, nextInfo.bridgeIndex)
                if (nextInfo.bridgeIndex > currentInfo.inIndex) {
                    bridges += edge
                }
            }
        }
    }
}

fun Graph.isBridge(edge: Edge, excluded: Set<Edge>? = null): Boolean {
    val connectionsInitially = dfsCount(edge.begin, excluded)
    val connectionsAfterRemove = dfsCount(edge.begin, excluded?.plus(edge) ?: hashSetOf(edge))

    return connectionsInitially != connectionsAfterRemove
}

fun Graph.dfsCount(vertex: Vertex, excluded: Set<Edge>? = null): Int {
    fun dfsCount(vertex: Vertex, visited: MutableSet<Vertex>): Int {
        visited += vertex
        var count = 1

        getNeighbors(vertex).forEach { neighbour ->
            if (neighbour !in visited && (excluded == null || getConnection(vertex, neighbour) !in excluded)) {
                count += dfsCount(neighbour, visited)
            }
        }
        return count
    }


    return dfsCount(vertex, hashSetOf())
}
