package dependencies;

import graphs.Vertex;

public class ControlFlowNode extends Vertex {
    /** A node has an attribute that is represents
     * the variable dependencies at that point.
     */
    private Dependencies depends;
    
    /** The dependencies for each node starts empty */
    public ControlFlowNode() {
        super();
        depends = new Dependencies();
    }
    public Dependencies getDepends() {
        return depends;
    }
    public void setDepends( Dependencies depends ) {
        this.depends = depends;
    }
    public String toString() {
        return super.toString() + depends;
    }

}
