# Implementation of Kosaraju's algorithm for
# finding the strongly connected components
# of directed graphs in O(m+n) time

import sys

processed = 0
source = 0

def kosaraju(graph):
    finishTimes = {}
    starting_times = {}
    leaders = {}

    vertices = len(graph)
    print 'Vertices in graph: ', vertices
    order = [x for x in range(1, vertices+1)]
    #order.reverse()


    print 'Running DFS on forward graph.'
    kosarajuLoop(graph, order, starting_times, finishTimes, leaders)


    print '------------------\n\n-----------------'

    # The magic order to visit the nodes in the
    # next pass of DFS is the decreasing order
    # of finishing times.
    swappedTimes = dict(zip(finishTimes.values(), finishTimes.keys()))
    magicOrder = [swappedTimes[x] for x in swappedTimes]
    magicOrder.reverse()


    print 'Reversing graph edges.'
    reversedGraph = reverseDirectedGraph(graph)
    print 'Running DFS on reversed graph.'
    kosarajuLoop(reversedGraph, magicOrder, starting_times, finishTimes, leaders)

    # Get sizes of strongly connected components
    SCCs = {}
    for node in leaders:
        leader = leaders[node]
        if leader not in SCCs:
            SCCs[leader] = 1
        else:
            SCCs[leader] += 1

    reverseSizes = sorted(SCCs.values(), reverse=True)

    print 'Number of SCCs: ', len(reverseSizes)
    print 'SCCs in order of decreasing size:'

    i = 0
    for size in reverseSizes:
        print size
        i += 1

        if i == 10:
            break


def kosarajuLoop(graph, order, starting_times, finishTimes, leaders):
    global processed
    global source
    processed = 0
    source = 0

    explored = {}
    for node in graph:
        explored[node] = False

    for node in order:
        if explored[node] == False:
            source = node
            kosarajuSearch(graph, node, explored, starting_times, finishTimes, leaders)

def kosarajuSearch(graph, i, explored, starting_times, finishTimes, leaders):
    global processed
    global source

    processed += 1
    starting_times[i] = processed
    print "Starting Time[", i, "] = ", processed

    explored[i] = True
    leaders[i] = source
    print "Explored:", explored

    edges = graph[i]
    for edge in edges:
        if explored[edge] == False:
            kosarajuSearch(graph, edge, explored, starting_times, finishTimes, leaders)

    processed += 1
    finishTimes[i] = processed
    print "Finishing Time[", i, "] = ", processed

def reverseDirectedGraph(graph):
    reversedGraph = {}

    for node in graph:
        reversedGraph[node] = []

    for node in graph:
        edges = graph[node]
        for edge in edges:
            reversedGraph[edge].append(node)

    return reversedGraph

def depthFirstSearchLoop(explored, graph):
    for node in graph:
        if node not in explored:
            depthFirstSearch(explored, graph, node)


def depthFirstSearch(explored, graph, start):
    explored.append(start)
    for node in graph[start]:
        if node not in explored:
            depthFirstSearch(explored, graph, node)


def main():

    sys.setrecursionlimit(2**20)

    print "***Kosaraju's Algorithm***"
    print 'Computing SCCs...'

    graphSample = {1: [],
                   2: [],
                   3: [4],
                   4: [7],
                   5: [3, 7],
                   6: [3],
                   7: [9],
                   8: [],
                   9: [5, 6],
                   10: [9]}

    explored = []
    depthFirstSearchLoop(explored, graphSample)
    print explored
    print graphSample
    print reverseDirectedGraph(graphSample)

    kosaraju(graphSample)

if __name__ == '__main__':
   main()