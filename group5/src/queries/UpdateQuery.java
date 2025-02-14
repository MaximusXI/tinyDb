package queries;

import java.util.List;

/**
 * This class represents a query to update records in a table in a database.
 */
public class UpdateQuery {
    private String tableName;
    private List<String> setColumn;
    private List<String> setValue;
    private String conditionColumn;
    private String conditionOperator;
    private String conditionValue;

    /**
     * Constructs an UpdateQuery with the specified table name, columns and values to be set, and condition.
     *
     * @param tableName         the name of the table in which records will be updated
     * @param setColumn         the columns to be updated
     * @param setValue          the values to be set in the specified columns
     * @param conditionColumn   the column to be used for the update condition
     * @param conditionOperator the operator to be used for the update condition
     * @param conditionValue    the value to be used for the update condition
     */
    public UpdateQuery(String tableName, List<String> setColumn, List<String> setValue, String conditionColumn, String conditionOperator, String conditionValue) {
        this.tableName = tableName;
        this.setColumn = setColumn;
        this.setValue = setValue;
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
     * Gets the columns to be updated.
     *
     * @return the list of columns
     */
    public List<String> getSetColumn() {
        return setColumn;
    }

    /**
     * Gets the values to be set in the specified columns.
     *
     * @return the list of values
     */
    public List<String> getSetValue() {
        return setValue;
    }

    /**
     * Gets the column to be used for the update condition.
     *
     * @return the condition column
     */
    public String getConditionColumn() {
        return conditionColumn;
    }

    /**
     * Gets the operator to be used for the update condition.
     *
     * @return the condition operator
     */
    public String getConditionOperator() {
        return conditionOperator;
    }

    /**
     * Gets the value to be used for the update condition.
     *
     * @return the condition value
     */
    public String getConditionValue() {
        return conditionValue;
    }

    @Override
    public String toString() {
        return "UpdateQuery{" +
                "tableName='" + tableName + '\'' +
                ", setColumn='" + setColumn + '\'' +
                ", setValue='" + setValue + '\'' +
                ", conditionColumn='" + conditionColumn + '\'' +
                ", conditionOperator='" + conditionOperator + '\'' +
                ", conditionValue='" + conditionValue + '\'' +
                '}';
    }
}
