package dependencies;

import java.util.ArrayList;
import java.util.Iterator;
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
    DGraph<ControlFlowNode, Primitive> graph;
    /** Unique entry and exit nodes for the control flow graph */
    ControlFlowNode entry, exit;

    /** Construct a new control flow graph for a function */
    public FlowGraph(Statement body) {
        super();
        graph = new DGraphAdj<ControlFlowNode, Primitive>();
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

    /**
     * To construct the control flow graph for a primitive, one only needs to
     * add an edge labelled with the primitive statement
     * 
     * @param entry
     *            vertex already in graph
     * @param exit
     *            vertex already in graph
     * @param statement
     *            labelling the edge
     */
    public void buildPrimitive(ControlFlowNode entry, ControlFlowNode exit,
            Primitive statement) {
        graph.addEdge(entry, exit, statement);
    }

    /** Calculate the dependencies for this graph */
    public Dependencies calculateDependencies(Dependencies entryDependencies) {
        Dependencies exitDependencies = entryDependencies.copy();
        entry.setDepends(exitDependencies);
        depthFirstSearch();
        return exit.getDepends();
    }
    
    /**
     * Perform a Depth First Search On the FlowGraph
     */
    private void depthFirstSearch() {
        for (ControlFlowNode v : graph) {
            visit(v);
        }
    }
    
    /**
     * Visit a ControlFlowNode (vertex) for a DFS traversal
     *  - treat vertex as unvisited if calculating dependencies of edge 
     *      would change the dependencies of the node.
     *      
     * @param u 
     *          ControlFlowNode vertex in graph 
     */
    private void visit(ControlFlowNode u) {
        
        int links = 0;  // Count the number of edges from each Vertex
        
        for (AdjacentEdge<ControlFlowNode, Primitive> e : graph.adjacent(u)) {
            if (!(e.edgeInfo instanceof Primitive.NullStatement)) links++;
            
            ControlFlowNode v = e.target;           // Target Vertex
            Dependencies depsIn = v.getDepends();   // Dependencies of Origin
            Dependencies depsTarget;                // Dependencies of Target
            

            if (links > 1) {
                /* If more than 1 non Null statement extends from a Vertex
                    dependencies at target dependencies need to be merged
                        (support for select statements)                 */
                depsTarget = e.edgeInfo.
                        calculateDependencies(v.getDepends());
                depsTarget = depsIn.merge(depsTarget);
            } else {
                depsTarget = e.edgeInfo.
                        calculateDependencies(u.getDepends());
            }
            
            v.setDepends(depsTarget);           // Update Target Dependencies
            
            // Visit Target if dependencies differ
            if (!depsTarget.equals(depsIn)) {
                visit(e.target);
            }
            
        }
    }
  
    public String toString() {
        String result = "Entry = " + entry + " Exit = " + exit + "\n";
        // As the graph is connected we just print the edges
        for (ControlFlowNode n : graph) {
            for (AdjacentEdge<ControlFlowNode, Primitive> e : 
                graph.adjacent(n)) {
                result += "  " + n + " -> " + e.target + " " + 
                        e.edgeInfo + " " + "\n";
            }
        }
        return result;
    }

    /**
     * To construct the control flow graph for a compound statement
     * 
     * @param entry
     *            vertex already in graph
     * @param exit
     *            vertex already in graph
     * @param compound
     *            statement
     */
    public void buildCompound(ControlFlowNode entry, ControlFlowNode exit,
            Compound compound) {

        int count = 0;                  // Number of edges added to graph
        int max = compound.getStatements().size();  // 
        ControlFlowNode last = entry;   // Previous Vertex In ControlFlowGraph  
        ControlFlowNode next;           // Next Vertex In ControlFlowGraph

        for (Statement statement : compound.getStatements()) {
            count++;
            if (count < max && max > 1) {
                // Create new Vertex for additional edges    
                next = newVertex();
                statement.buildGraph(last, next, this);
                last = next;
            } else {
                // Last statement in compound statement
                // No need to create any additional vertices
                statement.buildGraph(last, exit, this);
            }
        }
    }

    /**
     * Construct the control flow graph for a repeat statement
     * 
     * @param entry
     *            vertex already in graph
     * @param exit
     *            vertex already in graph
     * @param repeat
     *            statement
     */
    public void buildRepeat(ControlFlowNode entry, ControlFlowNode exit,
            Repeat repeat) {
        // Add NullStatement entry --> exit edge
        buildPrimitive(entry, exit, new Primitive.NullStatement(repeat.pos));

        // Add NullStatement entry --> enter_body edge
        ControlFlowNode enterBody = newVertex(); // entry vertex of repeat loop
        buildPrimitive(entry, enterBody, 
                new Primitive.NullStatement(repeat.pos));
        
        // Add NullStatement exit_body --> exit edge
        ControlFlowNode exit_body = newVertex(); // exit vertex of repeat loop
        buildPrimitive(exit_body, exit, 
                new Primitive.NullStatement(repeat.pos));

        // Add NullStatement exit_body --> enter_body edge
        buildPrimitive(exit_body, enterBody,
                new Primitive.NullStatement(repeat.pos));
        
        // Add edges for statements inside the repeat statement
        repeat.getStatement().buildGraph(enterBody, exit_body, this);
    }

    /**
     * Construct the control flow graph for a select statement
     * 
     * @param entry
     *            vertex already in graph
     * @param exit
     *            vertex already in graph
     * @param select
     *            statement
     */
    public void buildSelect(ControlFlowNode entry, ControlFlowNode exit,
            Select select) {
        for (Statement statement : select.getStatements()) {
            statement.buildGraph(entry, exit, this);
        }
    }
}
