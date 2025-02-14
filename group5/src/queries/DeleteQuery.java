package queries;

/**
 * This class represents a query to delete records from a table in a database.
 */
public class DeleteQuery {
    private String tableName;
    private String conditionColumn;
    private String conditionValue;
    private String conditionOperator;

    /**
     * Constructs a DeleteQuery with the specified table name, condition column, condition value, and condition operator.
     *
     * @param tableName         the name of the table from which records will be deleted
     * @param conditionColumn   the column to be used for the delete condition
     * @param conditionValue    the value to be used for the delete condition
     * @param conditionOperator the operator to be used for the delete condition
     */
    public DeleteQuery(String tableName, String conditionColumn, String conditionValue, String conditionOperator) {
        this.tableName = tableName;
        this.conditionColumn = conditionColumn;
        this.conditionValue = conditionValue;
        this.conditionOperator = conditionOperator;
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
     * Gets the column to be used for the delete condition.
     *
     * @return the condition column
     */
    public String getConditionColumn() {
        return conditionColumn;
    }

    /**
     * Gets the value to be used for the delete condition.
     *
     * @return the condition value
     */
    public String getConditionValue() {
        return conditionValue;
    }

    /**
     * Gets the operator to be used for the delete condition.
     *
     * @return the condition operator
     */
    public String getConditionOperator() {
        return conditionOperator;
    }

    @Override
    public String toString() {
        return "DeleteQuery{" +
                "tableName='" + tableName + '\'' +
                ", conditionColumn='" + conditionColumn + '\'' +
                ", conditionValue='" + conditionValue + '\'' +
                ", conditionOperator='" + conditionOperator + '\'' +
                '}';
    }
}


