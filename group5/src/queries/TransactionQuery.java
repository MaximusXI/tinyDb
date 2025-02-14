package queries;

/**
 * Represents a transaction query with an associated SQL statement.
 * This class encapsulates an SQL statement that can be used to perform
 * transactions or operations on a database.
 *
 */
public class TransactionQuery {
    /**
     * The SQL statement associated with this transaction query.
     */
    private String statement;

    /**
     * Constructs a new {@code TransactionQuery} with the specified SQL statement.
     *
     * @param statement the SQL statement to be associated with this query
     */
    public TransactionQuery(String statement) {
        this.statement = statement;
    }

    /**
     * Returns the SQL statement associated with this transaction query.
     *
     * @return the SQL statement as a {@code String}
     */
    public String getStatement() {
        return statement;
    }
}
