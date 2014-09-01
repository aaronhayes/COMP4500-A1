package dependencies;

import java.util.List;

import dependencies.Primitive.NullStatement;
import dependencies.Statement.Compound;
import dependencies.Statement.Repeat;
import dependencies.Statement.Select;
import graphs.DGraph;
import graphs.DGraphAdj;
import graphs.Graph.AdjacentEdge;

public class FlowGraph {
    /** Control flow graph for a function */
    DGraph<ControlFlowNode,Primitive> graph;
    /** Unique entry and exit nodes for the control flow graph */
    ControlFlowNode entry, exit;
    
    /** Construct a new control flow graph for a function */
    public FlowGraph( Statement body ) {
        super();
        graph = new DGraphAdj<ControlFlowNode,Primitive>();
        entry = newVertex();
        exit = newVertex();
        body.buildGraph(entry, exit, this);
    }
    /** Construct a new vertex and add to graph */
    private ControlFlowNode newVertex() {
        ControlFlowNode v = new ControlFlowNode();
        graph.addVertex(v);
        return v;
    }
    /** To construct the control flow graph for a primitive,
     * one only needs to add an edge labeled with the primitive statement
     * @param entry vertex already in graph
     * @param exit vertex already in graph
     * @param statement labeling the edge
     */
    public void buildPrimitive( ControlFlowNode entry, ControlFlowNode exit,
            Primitive statement ) {
        graph.addEdge( entry, exit, statement);
    }
    /** Calculate the dependencies for this graph */
    public Dependencies calculateDependencies( 
            Dependencies entryDependencies ) {
        Dependencies exitDependencies = null;
        //TODO complete calculation of dependencies and
        //TODO return the dependencies at the exit vertex
        return exitDependencies;
    }
    public String toString() {
        String result = "Entry = " + entry + " Exit = " + exit + "\n";
        // As the graph is connected we just print the edges
        for( ControlFlowNode n : graph ) {
            for( AdjacentEdge<ControlFlowNode,Primitive> 
                    e : graph.adjacent(n) ) {
                result += "  " + n + " -> " + e.target + " " +
                        e.edgeInfo + " " + "\n";
            }
        }
        return result;
    }
}
