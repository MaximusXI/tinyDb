package queries;

/**
 * This class represents a query to create a database.
 */
public class CreateDatabaseQuery {
    private String databaseName;

    /**
     * Constructs a CreateDatabaseQuery with the specified database name.
     *
     * @param databaseName the name of the database to be created
     */
    public CreateDatabaseQuery(String databaseName) {
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
        return "CreateDatabaseQuery{" +
                "databaseName='" + databaseName + '\'' +
                '}';
    }
}
