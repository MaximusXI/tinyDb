package queries;

import entities.ForeignKey;
import java.util.List;

/**
 * This class represents a query to create a table in a database.
 */
public class CreateTableQuery {
    private String tableName;
    private List<String> columnNames;
    private List<String> columnDataTypes;
    private List<Integer> columnSizes;
    private String primaryKeyColumn;
    private ForeignKey foreignKey;

    /**
     * Constructs a CreateTableQuery with the specified table name, column details, primary key, and foreign key.
     *
     * @param tableName          the name of the table to be created
     * @param columnNames        the names of the columns in the table
     * @param columnDataTypes    the data types of the columns in the table
     * @param columnSizes        the sizes of the columns in the table
     * @param primaryKeyColumn   the name of the primary key column
     * @param foreignKey         the foreign key constraint for the table
     */
    public CreateTableQuery(String tableName, List<String> columnNames, List<String> columnDataTypes, List<Integer> columnSizes,
                            String primaryKeyColumn, ForeignKey foreignKey) {
        this.tableName = tableName;
        this.columnNames = columnNames;
        this.columnDataTypes = columnDataTypes;
        this.columnSizes = columnSizes;
        this.primaryKeyColumn = primaryKeyColumn;
        this.foreignKey = foreignKey;
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
     * Gets the names of the columns in the table.
     *
     * @return the list of column names
     */
    public List<String> getColumnNames() {
        return columnNames;
    }

    /**
     * Gets the data types of the columns in the table.
     *
     * @return the list of column data types
     */
    public List<String> getColumnDataTypes() {
        return columnDataTypes;
    }

    /**
     * Gets the sizes of the columns in the table.
     *
     * @return the list of column sizes
     */
    public List<Integer> getColumnSizes() {
        return columnSizes;
    }

    /**
     * Gets the name of the primary key column.
     *
     * @return the primary key column name
     */
    public String getPrimaryKeyColumn() {
        return primaryKeyColumn;
    }

    /**
     * Gets the foreign key constraint for the table.
     *
     * @return the foreign key
     */
    public ForeignKey getForeignKey() {
        return foreignKey;
    }
}