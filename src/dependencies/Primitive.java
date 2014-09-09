package dependencies;

import java.util.SortedSet;

import source.Position;

public abstract class Primitive extends Statement {

    protected Primitive(Position pos) {
        super(pos);
    }

    /** Default dependency calculation for null statement */
    public Dependencies calculateDependencies(Dependencies deps) {
        return deps.copy();
    }
    public Dependencies calculateDependencies(Dependencies deps, 
            boolean merge) {
        return deps.copy();
    }

    /**
     * Building a graph is the same for all primitives
     * 
     * @param entry
     *            vertex for graph for primitive
     * @param exit
     *            vertex for graph for primitive
     * @param flowGraph
     *            to be built
     */
    public void buildGraph(ControlFlowNode entry, ControlFlowNode exit,
            FlowGraph flowGraph) {
        flowGraph.buildPrimitive(entry, exit, this);
    }

    /** For handling erroneous input programs */
    public static class ErrorStatement extends Primitive {

        public ErrorStatement(Position pos) {
            super(pos);
        }

        public String toString() {
            return "error;";
        }
    }

    /** Null statement does nothing */
    public static class NullStatement extends Primitive {

        public NullStatement(Position pos) {
            super(pos);
        }

        public String toString() {
            return "null;";
        }
    }

    /** A statement that acquires a lock */
    public static class Assignment extends Primitive {
        String variable;
        DependSet expression;

        public Assignment(Position pos, String variable, 
                DependSet expression) {
            super(pos);
            this.variable = variable;
            this.expression = expression;
        }

        @Override
        public Dependencies calculateDependencies(Dependencies in) {
            return calculateDependencies(in, false);
        }
        @Override
        public Dependencies calculateDependencies(Dependencies in, boolean merge) {
            Dependencies out = in.copy();
            // TODO compute out
            
            if (expression.isEmpty()) {
                // Add filler - will be removed later
                expression.add("911");
            }
            /*System.out.println(in.toString());
            
            SortedSet<String> vars = expression.getDependencies();

            if (vars.size() == 0) {
                out.remove(variable);
            } else {
                for (String var : vars) {
                    DependSet set = out.get(var);
                    if (set != null) {
                        if (out.get(var) != null && merge) {
                            set.merge(out.get(variable));
                        }
                        System.out.println(variable + " asdg " + set.toString());
                        out.put(variable, set);
                    } /*else if (out.wasRemoved(var)) {
                        DependSet loopSet = new DependSet(var);
                        if (merge) {
                            loopSet.merge(out.get(variable));
                        }
                        //out.put(variable, loopSet);
                        System.out.println("ADDING " + variable + " to " + loopSet.toString());
                    }
                }
            } */
            
            
            SortedSet<String> vars = expression.getDependencies();
            //System.out.println(vars.toString());
            DependSet newSet = expression;
            for (String var : vars) {
                DependSet set = out.get(var);
                if (set != null) {
                    //System.out.println(var +  " got " + set.toString());
                    
                    
                    if (set.getDependencies().contains("911"))  break;
                    
                    if (merge) {
                        newSet.merge(set);
                    } else {
                        newSet = set;
                    }
                }
            }
            //System.out.println(variable + " " + newSet);
            
            //System.out.println(out.get(variable) + "ADSGasdg");
            

            
            if (merge) {
                expression.merge(out.get(variable));
            }
            
            
            if (out.get(variable) != null && out.get(variable).getDependencies().contains("911")) {
                // Replace previous values of variable with new dependencies
                for (String key : out.getKeys(variable)) {
                    out.replace(key, newSet);
                }
            }
            
            //System.out.println("ADDING " + variable + " WITH " + newSet);
            out.put(variable, newSet);
            //System.out.println("RESULT NOW " + out);
            return out;
        }

        public String toString() {
            return variable + " := " + expression + ";";
        }
    }
}
