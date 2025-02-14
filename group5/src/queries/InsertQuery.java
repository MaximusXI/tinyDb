package queries;

import java.util.List;

/**
 * This class represents a query to insert records into a table in a database.
 */
public class InsertQuery {
    private String tableName;
    private List<String> columnNames;
    private List<String> values;

    /**
     * Constructs an InsertQuery with the specified table name, column names, and values.
     *
     * @param tableName   the name of the table into which records will be inserted
     * @param columnNames the names of the columns into which values will be inserted
     * @param values      the values to be inserted into the columns
     */
    public InsertQuery(String tableName, List<String> columnNames, List<String> values) {
        this.tableName = tableName;
        this.columnNames = columnNames;
        this.values = values;
    }

    /**
     * Gets the name of the table.
     *
     * @return the table name
     */
    public String getTableName() {
        return tableName;
    }

    /**
     * Gets the names of the columns into which values will be inserted.
     *
     * @return the list of column names
     */
    public List<String> getColumnNames() {
        return columnNames;
    }

    /**
     * Gets the values to be inserted into the columns.
     *
     * @return the list of values
     */
    public List<String> getValues() {
        return values;
    }

    @Override
    public String toString() {
        return "InsertQuery{" +
                "tableName='" + tableName + '\'' +
                ", columnNames=" + columnNames +
                ", values=" + values +
                '}';
    }
}