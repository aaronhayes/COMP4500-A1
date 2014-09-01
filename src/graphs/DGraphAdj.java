package graphs;

/** Implementation of a directed graph using adjacency lists 
 * @author Ian Hayes
 */
public class DGraphAdj<V extends Vertex, E>
    extends GraphAdj<V, E>
    implements DGraph<V, E> 
{

    public DGraphAdj() {
        super();
    }
    /** Add an edge to the graph
     * As this is a directed graph it is added from u to v only
     * @param u source vertex
     * @param v target vertex
     * @param e edge information
     * @requires both u and v are already vertices of the graph
     */
    public void addEdge( V u, V v, E e ) {
        assert hasVertex(u) && hasVertex(v);
        graph.get(u.getIndex()).edges.add( 
                new Graph.AdjacentEdge<V,E>(v,e) );
    }
}
