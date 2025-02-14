package Utils;

import java.util.*;

/**
 * Utility class to store and manage primary key information for database tables.
 */
public class DatabaseInfo {
    private final Map<String, Set<String>> tablePrimaryKeys;

    /**
     * Constructs a new DatabaseInfo object.
     */
    public DatabaseInfo() {
        tablePrimaryKeys = new HashMap<>();
    }

    /**
     * Adds a primary key to a specified table.
     *
     * @param table      the name of the table
     * @param primaryKey the primary key column name
     */
    public void addPrimaryKey(String table, String primaryKey) {
        tablePrimaryKeys.computeIfAbsent(table, k -> new HashSet<>()).add(primaryKey.trim());
    }

    /**
     * Checks if a specified column is a primary key in the given table.
     *
     * @param table  the name of the table
     * @param column the column name to check
     * @return true if the column is a primary key in the table otherwise false
     */
    public boolean isPrimaryKey(String table, String column) {
        return tablePrimaryKeys.getOrDefault(table, Collections.emptySet()).contains(column.trim());
    }

}
