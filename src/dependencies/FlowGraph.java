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
    
    private void depthFirstSearch() {
        for (ControlFlowNode v : graph) {
            if (true) {
                visit(v);
            }
        }
    }
    
    /**
     * 
     * @param u
     */
    private void visit(ControlFlowNode u) {
        
        int links = 0;
        
        for (AdjacentEdge<ControlFlowNode, Primitive> e : graph.adjacent(u)) {
            if (!(e.edgeInfo instanceof Primitive.NullStatement)) links++;
            
            ControlFlowNode v = e.target;
            Dependencies vDepsIn = v.getDepends();
            Dependencies depsOut;
            

            if (links > 1) {
                depsOut = e.edgeInfo.
                        calculateDependencies(v.getDepends());
                depsOut = vDepsIn.merge(depsOut);
                
            } else {
                depsOut = e.edgeInfo.
                        calculateDependencies(u.getDepends());
            }
            
            v.setDepends(depsOut);
            
            if (!depsOut.equals(vDepsIn)) {
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

        int count = 0;
        int max = compound.getStatements().size();
        ControlFlowNode last = entry;
        ControlFlowNode next;

        for (Statement s : compound.getStatements()) {
            count++;
            if (count == 1 && max > 1) {
                next = newVertex();
                buildStatement(entry, next, s);
                last = next;
            } else if (count == 1 && max == 1) {
                buildStatement(entry, exit, s);
            } else if (count > 1 && count < max) {
                next = newVertex();
                buildStatement(last, next, s);
                last = next;
            } else {
                buildStatement(last, exit, s);
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
     * @param compound
     *            statement
     */
    public void buildRepeat(ControlFlowNode entry, ControlFlowNode exit,
            Repeat repeat) {
        // Add null entry --> exit edge
        buildPrimitive(entry, exit, new Primitive.NullStatement(null));

        // Add null entry --> enter_body edge
        ControlFlowNode enter_body = newVertex();
        buildPrimitive(entry, enter_body, new Primitive.NullStatement(null));
        
        // Add null exit_body --> exit edge
        ControlFlowNode exit_body = newVertex();
        buildPrimitive(exit_body, exit, new Primitive.NullStatement(null));

        // Add null exit_body --> enter_body edge
        buildPrimitive(exit_body, enter_body,
                new Primitive.NullStatement(null));
        
        // Add edges for statements inside the repeat statement
        buildStatement(enter_body, exit_body, repeat.getStatement());
    }

    /**
     * Construct the control flow graph for a select statement
     * 
     * @param entry
     *            vertex already in graph
     * @param exit
     *            vertex already in graph
     * @param compound
     *            statement
     */
    public void buildSelect(ControlFlowNode entry, ControlFlowNode exit,
            Select select) {
        for (Statement s : select.getStatements()) {
            buildStatement(entry, exit, s);
        }
    }

    /**
     * Construct the control flow graph for any statement
     * 
     * @param entry
     *            vertex already in graph
     * @param exit
     *            vertex already in graph
     * @param s
     *            the statement
     */
    private void buildStatement(ControlFlowNode entry, ControlFlowNode exit,
            Statement s) {
        if (s instanceof Primitive) {
            buildPrimitive(entry, exit, (Primitive) s);
        } else if (s instanceof Select) {
            buildSelect(entry, exit, (Select) s);
        } else if (s instanceof Repeat) {
            buildRepeat(entry, exit, (Repeat) s);
        } else if (s instanceof Compound) {
            buildCompound(entry, exit, (Compound) s);
        }
    }
}
