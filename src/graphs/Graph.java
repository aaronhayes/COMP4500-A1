package graphs;

import java.util.Iterator;

/** Directed or undirected graph - the main difference is in 
 * the treatment of edges.
 * @author Ian Hayes
 */
public interface Graph<V extends Vertex, E> extends Iterable<V>{
    
    /** Class used for returning edges in iterator */
    public class AdjacentEdge<V,E> {
        public V target;
        public E edgeInfo;
        
        public AdjacentEdge( V target, E edgeInfo ) {
            super();
            this.target = target;
            this.edgeInfo = edgeInfo;
        }
    }

    /** @return the size of the graph */
    public int size();
    
    /** Adds a vertex to the graph
     * @param v vertex to be added
     * @requires v is not already in a graph 
     */
    public void addVertex( V v );

    /** Add an edge to the graph
     * @param u source vertex
     * @param v target vertex
     * @param e edge information
     * @requires both u and v are already vertices of the graph
     */
    public void addEdge( V u, V v, E e );
    
    /** Check is an edge exits from u to v
     * @param u source vertex
     * @param v target vertex 
     * @requires both u and v are vertices in the graph
     * @return true if and only if graph has edge from u to v */
    public boolean hasEdge( V u, V v );
    
    /** @return an iterator over the vertices of the graph */
    public Iterator<V> iterator();
    
    /** @return iterator over the list of vertices adjacent to vertex v */
    public Iterable<AdjacentEdge<V,E>> adjacent( V v );
    
}
