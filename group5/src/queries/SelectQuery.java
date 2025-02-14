package queries;

import java.util.List;

/**
 * This class represents a query to select records from a table in a database.
 */
public class SelectQuery {
    private String tableName;
    private List<String> columns;
    private String conditionColumn;
    private String conditionOperator;
    private String conditionValue;

    /**
     * Constructs a SelectQuery with the specified table name, columns, and condition.
     *
     * @param tableName         the name of the table from which records will be selected
     * @param columns           the names of the columns to be selected
     * @param conditionColumn   the column to be used for the select condition
     * @param conditionOperator the operator to be used for the select condition
     * @param conditionValue    the value to be used for the select condition
     */
    public SelectQuery(String tableName, List<String> columns, String conditionColumn, String conditionOperator, String conditionValue) {
        this.tableName = tableName;
        this.columns = columns;
        this.conditionColumn = conditionColumn;
        this.conditionOperator = conditionOperator;
        this.conditionValue = conditionValue;
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
     * Gets the names of the columns to be selected.
     *
     * @return the list of column names
     */
    public List<String> getColumns() {
        return columns;
    }

    /**
     * Gets the column to be used for the select condition.
     *
     * @return the condition column
     */
    public String getConditionColumn() {
        return conditionColumn;
    }

    /**
     * Gets the operator to be used for the select condition.
     *
     * @return the condition operator
     */
    public String getConditionOperator() {
        return conditionOperator;
    }

    /**
     * Gets the value to be used for the select condition.
     *
     * @return the condition value
     */
    public String getConditionValue() {
        return conditionValue;
    }

    @Override
    public String toString() {
        return "SelectQuery{" +
                "tableName='" + tableName + '\'' +
                ", columns=" + columns +
                ", conditionColumn='" + conditionColumn + '\'' +
                ", conditionOperator='" + conditionOperator + '\'' +
                ", conditionValue='" + conditionValue + '\'' +
                '}';
    }
}
