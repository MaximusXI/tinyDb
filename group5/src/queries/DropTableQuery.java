package queries;

/**
 * This class represents a query to drop a table from a database.
 */
public class DropTableQuery {
    private String tableName;

    /**
     * Constructs a DropTableQuery with the specified table name.
     *
     * @param tableName the name of the table to be dropped
     */
    public DropTableQuery(String tableName) {
        this.tableName = tableName;
    }

    /**
     * Gets the name of the table.
     *
     * @return the table name
     */
    public String getTableName() {
        return tableName;
    }

    @Override
    public String toString() {
        return "DropTableQuery{" +
                "tableName='" + tableName + '\'' +
                '}';
    }
}