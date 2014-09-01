package dependencies;

import source.Position;

public abstract class Primitive extends Statement {
    
    protected Primitive( Position pos ){
        super( pos );
    }
    /** Default dependency calculation for null statement */
    public Dependencies calculateDependencies( Dependencies deps ) {
        return deps.copy();
    }
    /** Building a graph is the same for all primitives
     * @param entry vertex for graph for primitive
     * @param exit vertex for graph for primitive
     * @param flowGraph to be built */
    public void buildGraph( ControlFlowNode entry, ControlFlowNode exit, 
            FlowGraph flowGraph ) {
        flowGraph.buildPrimitive( entry, exit, this );
    }
    /** For handling erroneous input programs */
    public static class ErrorStatement extends Primitive {
        
        public ErrorStatement( Position pos ) {
            super( pos );
        }
        
        public String toString() {
            return "error;"; 
        }
    }
    /** Null statement does nothing */
    public static class NullStatement extends Primitive {
        
        public NullStatement( Position pos ) {
            super( pos );
        }
                
        public String toString() {
            return "null;";
        }
    }
    /** A statement that acquires a lock */
    public static class Assignment extends Primitive {
        String variable;
        DependSet expression;
        
        public Assignment( Position pos, String variable,
                DependSet expression ) {
            super( pos );
            this.variable = variable;
            this.expression = expression;
        }
        
        @Override
        public Dependencies calculateDependencies( Dependencies in ) {
            Dependencies out = null;
            //TODO compute out
            return out;
        }
        
        public String toString() {
            return variable + " := " + expression + ";";
        }
    }
}
