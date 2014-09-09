package dependencies;

//import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.SortedMap;

public class Dependencies {

    /**
     * Maps each variable name to the names of variables it depends on. A
     * SortedMap is used so that dependencies print in order.
     */
    private SortedMap<String, DependSet> dependencies;

    /** Construct empty dependencies */
    public Dependencies() {
        super();
        dependencies = new TreeMap<String, DependSet>();
    }

    /** Add a dependency between a variable and a set of variables */
    public Dependencies put(String var, DependSet vars) {
        dependencies.put(var, vars);
        return this;
    }

    /** Get a dependency set between a variable and a set of variables */
    public DependSet get(String var) {
        return dependencies.get(var);
    }
    
    public void replace(String var, DependSet vars) {
        dependencies.remove(var);
        dependencies.put(var, vars);
    }
    
    /**
     * 
     */
    public void clean() {
        for (SortedMap.Entry<String, DependSet> entry : 
            dependencies.entrySet()) {
                entry.getValue().getDependencies().remove("911");
        }
    }
    

    /**
     * 
     * @param variable
     */
    public void remove(String variable) {
        dependencies.remove(variable);
    }
    

    /**
     * Dependency equality requires that the dependencies are identical for all
     * variables
     */
    public boolean equals(Dependencies other) {
        return dependencies.equals(other.dependencies);
    }

    /** Construct dependencies that are a copy of this */
    public Dependencies copy() {
        Dependencies newCopy = new Dependencies();
        for (SortedMap.Entry<String, DependSet> entry : 
            dependencies.entrySet()) {
            newCopy.dependencies.put(entry.getKey(), entry.getValue().copy());
        }
        return newCopy;
    }

    public String toString() {
        String result = "{";
        String sep = " ";
        for (SortedMap.Entry<String, DependSet> entry : 
            dependencies.entrySet()) {
            // Omit any empty dependencies when printing
            if (!entry.getValue().isEmpty()) {
                result += sep + "(" + entry.getKey() + ","
                        + entry.getValue().getDependencies() + ")";
                sep = ", ";
            }
        }
        return result + " }";
    }

    /**
     * 
     * @param variable
     * @return
     */
    public ArrayList<String> getKeys(String variable) {
        ArrayList<String> keys = new ArrayList<String>();
        
        for (Entry<String, DependSet> entry : dependencies.entrySet()) {
            if (entry.getValue().getDependencies().contains(variable)) {
                keys.add(entry.getKey());
            }
                
        }
        
        return keys;
        
    }
}
