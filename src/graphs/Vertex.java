package graphs;

public class Vertex {
    /** Used as a unique identifier for vertex to allow
     * array-based implementation.
     * Set and get index are only allowed within package graphs
     */
    private int index;
    
    public Vertex( ) {
        super();
        index = -1;
    }
    
    int getIndex() {
        return index;
    }
    
    void setIndex( int i ) {
        this.index = i;
    }
    
    public String toString() {
        return "vertex(" + index + ")";
    }
    
}
