package graphs;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Iterator;

/** This class implements the common parts of directed and undirected 
 * graphs using an adjacency list representation. 
 * Only adding edges is different between the two.
 * @author Ian Hayes
 *
 * @param <V extends Vertex> type of information stored with each vertex
 * @param <E> type of information stored with each edge
 */
abstract class GraphAdj<V extends Vertex,E> 
        implements Graph<V,E> {

    /** A graph is represented by a list of Ventry elements,
     * each of which contains a vertex and a list of adjacent edges.
     */
    protected List<VEntry> graph;
    
    protected class VEntry {
        V source;
        List<AdjacentEdge<V,E>> edges;
        
        VEntry( V v ) {
            super();
            this.source = v;
            this.edges = new LinkedList<AdjacentEdge<V,E>>();
        }
    }
    /** Constructor for an empty graph */
    public GraphAdj() {
        super();
        // Array implementation to allow efficient lookup of vertices
        graph = new ArrayList<VEntry>();
    }
    /** @return the number of vertices in the graph */
    public int size() {
        return graph.size();
    }
    /** Add a vertex to the graph
     * @param v vertex to be added
     * @requires v is not already in a graph
     */
    public void addVertex( V v ) {
        /** Check if vertex has been added to a graph */
        assert v.getIndex() == -1;
        v.setIndex( graph.size() );
        graph.add( new VEntry( v ) );
    }
    /** Internal check that a vertex is actually in the graph
     * @return true if and only if v is in this graph  */
    protected boolean hasVertex( V v ) {
        int i = v.getIndex();
        return 0 <= i && i < graph.size() && graph.get(i).source == v;
    }
    /** Adding an edge is different for directed and undirected graphs
     * and hence the method is abstract in this class.
     * @param u source vertex
     * @param v target vertex
     * @param e edge information
     * @requires both u and v are already vertices of the graph
     */
    public abstract void addEdge( V u, V v, E e );
    
    /** Check is graph has an edge from u to v
     * @param u source vertex
     * @param v target vertex
     * @requires u and v are vertices of the graph
     * @return true if and only if the graph has an edge from u to v
     */
    public boolean hasEdge( V u, V v ) {
        assert hasVertex(u) && hasVertex(u);
        // Search the edges adjacent to u for vertex v
        for( AdjacentEdge<V,E> e : adjacent( u )  ) {
            if( e.target == v ) {
                return true;
            }
        }
        return false;
    }
    /** Allow iteration over the vertices of a graph
     * @return an iterator over the vertices
     */
    public Iterator<V> iterator() {
        return new Vertices();
    }
    /** Allow iteration over the edges adjacent to a vertex 
     * @param u source vertex
     * @return edges adjacent to u as an Iterable
     */
    public Iterable<AdjacentEdge<V,E>> adjacent( V u ) {
        return graph.get(u.getIndex()).edges;
    }
    /** The iterator over vertices uses the list iterator over the entries
     *  in the graph and selects the vertex from the entry.
     */
    private class Vertices implements Iterator<V> {
        
        private Iterator<VEntry> entryIterator;
        
        public Vertices() {
            super();
            entryIterator = graph.iterator();
        }
        public boolean hasNext() {
            return entryIterator.hasNext();
        }
        public V next() {
            return entryIterator.next().source;
        }
        /** Removal of vertices is not supported */
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }
}
