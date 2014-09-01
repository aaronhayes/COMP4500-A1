package dependencies;

import java.util.SortedSet;
import java.util.TreeSet;

/** DependSet is essentially a set of variable names */
public class DependSet {
    
    /** A SortedSet is used so that the variables print in order */
    private SortedSet<String> variables;
    
    public DependSet( SortedSet<String> variables ) {
        super();
        this.variables = variables;
    }
    /** Constructs an empty set of variables */
    public DependSet() {
        this( new TreeSet<String>() );
    }
    /** Constructs a sey with a single variable in it */
    public DependSet( String var ) {
        this();
        variables.add( var );
    }
    /** @return true if and only if the sets of variables are equal */
    public boolean equals( DependSet other ) {
        return variables.equals( other );
    }
    /** @return true if and only if the set of variables is empty */
    public boolean isEmpty() {
        return variables.isEmpty();
    }
    /** @return a copy of this set of variables */
    public DependSet copy() {
        DependSet copy =  new DependSet();
        copy.addAll( this );
        return copy;
    }
    /** Add the variables in the other set to this set
     * @param other set of variables
     * @return true if and only if this set is changed
     */
    public boolean merge( DependSet other ) {
        if( variables.containsAll( other.variables ) ) {
            return false;
        }
        variables.addAll( other.variables );
        return true;
    }
    /** @return the set of variables */
    public SortedSet<String> getDependencies() {
        return variables;
    }
    /** Add a variable tot he set */
    public void add(String name) {
        variables.add( name );
    }
    /** Add all the variables from another set */
    public void addAll( DependSet vars ) {
        variables.addAll( vars.variables );
    }
    /** @return a printable version of the set of variables */
    public String toString() {
        return variables.toString();
    }
}
