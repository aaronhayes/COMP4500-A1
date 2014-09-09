package dependencies;

import java.util.List;
import java.util.Set;

import source.Position;

public abstract class Statement {

    public Position pos;

    protected Statement(Position pos) {
        super();
        this.pos = pos;
    }

    /**
     * build a control flow graph from the entry vertex to the exit vertex based
     * on the structure of this statement
     * 
     * @param entry
     *            vertex already in flowGraph
     * @param exit
     *            vertex already in flowGraph
     * @param flowGraph
     *            to be completed for this statement
     */
    public abstract void buildGraph(ControlFlowNode entry,
            ControlFlowNode exit, FlowGraph flowGraph);

    /**
     * A compound statement made up of a list of statements that are executed in
     * sequence.
     */
    public static class Compound extends Statement {
        private List<Statement> statements;

        public Compound(Position pos, List<Statement> statements) {
            super(pos);
            this.statements = statements;
        }

        public List<Statement> getStatements() {
            return statements;
        }

        public void buildGraph(ControlFlowNode entry, ControlFlowNode exit,
                FlowGraph flowGraph) {
            flowGraph.buildCompound(entry, exit, this);
        }

        public String toString() {
            String result = "{";
            for (Statement s : statements) {
                result += " " + s.toString();
            }
            return result + " }";
        }
    }

    /** A statement that selects and executes one statement of its statements */
    public static class Select extends Statement {
        private Set<Statement> statements;

        public Select(Position pos, Set<Statement> statements) {
            super(pos);
            this.statements = statements;
        }

        public Set<Statement> getStatements() {
            return statements;
        }

        public void buildGraph(ControlFlowNode entry, ControlFlowNode exit,
                FlowGraph flowGraph) {
            flowGraph.buildSelect(entry, exit, this);
        }

        public String toString() {
            String result = "select {";
            String sep = " ";
            for (Statement s : statements) {
                result += sep + s.toString();
                sep = " | ";
            }
            return result + " }";
        }
    }

    /** A statement that repeats its body zero or more times */
    public static class Repeat extends Statement {
        private Statement statement;

        public Repeat(Position pos, Statement statement) {
            super(pos);
            this.statement = statement;
        }

        public Statement getStatement() {
            return statement;
        }

        public void buildGraph(ControlFlowNode entry, ControlFlowNode exit,
                FlowGraph flowGraph) {
            flowGraph.buildRepeat(entry, exit, this);
        }

        public String toString() {
            return "repeat " + statement.toString();
        }
    }
}
