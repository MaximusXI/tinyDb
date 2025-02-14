package queries;

/**
 * This class represents a query to switch the current database context.
 */
public class UseDatabaseQuery {
    private String databaseName;

    /**
     * Constructs a UseDatabaseQuery with the specified database name.
     *
     * @param databaseName the name of the database to be used
     */
    public UseDatabaseQuery(String databaseName) {
        this.databaseName = databaseName;
    }

    /**
     * Gets the name of the database.
     *
     * @return the database name
     */
    public String getDatabaseName() {
        return databaseName;
    }

    @Override
    public String toString() {
        return "UseDatabaseQuery{" +
                "databaseName='" + databaseName + '\'' +
                '}';
    }
}


