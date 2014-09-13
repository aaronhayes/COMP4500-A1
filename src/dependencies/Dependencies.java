package dependencies;

//import java.io.PrintStream;
import java.util.SortedSet;
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

    /**
     * Get a DependSet of a variable
     * 
     * @return DependSet of Variable
     */
    public DependSet get(String var) {
        return dependencies.get(var);
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
        for (SortedMap.Entry<String, DependSet> 
                entry : dependencies.entrySet()) {
            newCopy.dependencies.put(entry.getKey(), entry.getValue().copy());
        }
        return newCopy;
    }

    public String toString() {
        String result = "{";
        String sep = " ";
        for (SortedMap.Entry<String, DependSet> 
                entry : dependencies.entrySet()) {
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
     * Add a dependency between a variable and a set of variables Ensure
     * dependencies are linked with previous dependencies
     * 
     * @param variable
     *            to be added with dependencies
     * @param expression
     *            DependSet of variable
     * @return Dependencies with added dependency
     */
    public Dependencies add(String variable, DependSet expression) {
        SortedSet<String> vars = expression.getDependencies();
        DependSet newSet = new DependSet();

        for (String var : vars) {
            DependSet set = dependencies.get(var);
            if (set != null) {
                newSet.merge(set);
            }
        }

        dependencies.put(variable, newSet);
        return this;
    }

    /**
     * Merge two Dependencies If a key appears in either Dependencies, it must
     * appear in the merged Dependencies with a combined DependSet.
     * 
     * @param deps
     *            Dependencies to be merged
     * @return Merged Dependencies
     */
    public Dependencies merge(Dependencies deps) {
        // Check for keys in Dependencies 1
        for (String key : dependencies.keySet()) {
            if (deps.dependencies.containsKey(key)) {
                dependencies.get(key).merge(deps.get(key));
            }
        }

        // Check for keys in Dependencies 2
        for (String key : deps.dependencies.keySet()) {
            // Already checked Dependencies 1, so nothing to merge - just add
            if (!dependencies.containsKey(key)) {
                dependencies.put(key, deps.get(key));
            }
        }

        return this;
    }
}
