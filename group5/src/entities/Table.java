package entities;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Represents a table in a database with columns, rows, and a primary key.
 */
public class Table implements Cloneable {

    String tableName;

    List<Column> columns;

    List<Row> data;

    String primaryKeyColumnName;

    static int id = 1;

    ForeignKey foreignKey;

    /**
     * Constructs a new Table with the specified name.
     *
     * @param tableName the name of the table
     */
    public Table(String tableName) {
        this.tableName = tableName;
        this.columns = new ArrayList<>();
        this.data = new ArrayList<>();
        primaryKeyColumnName = "";
        Table.id = 1;
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
     * Sets the name of the table.
     *
     * @param tableName the new table name
     */
    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    /**
     * Gets the columns of the table.
     *
     * @return the list of columns
     */
    public List<Column> getColumns() {
        return columns;
    }

    /**
     * Sets the columns of the table.
     *
     * @param columns the new list of columns
     */
    public void setColumns(List<Column> columns) {
        this.columns = columns;
    }

    /**
     * Gets the data (rows) of the table.
     *
     * @return the list of rows
     */
    public List<Row> getData() {
        return data;
    }

    /**
     * Sets the data (rows) of the table.
     *
     * @param data the new list of rows
     */
    public void setData(List<Row> data) {
        this.data = data;
    }

    /**
     * Gets the current ID counter.
     *
     * @return the current ID
     */
    public static int getId() {
        return id;
    }

    /**
     * Sets the current ID counter.
     *
     * @param id the new ID
     */
    public static void setId(int id) {
        Table.id = id;
    }

    /**
     * Sets the primary key column name.
     *
     * @param primaryKeyColumnName the new primary key column name
     */
    public void setPrimaryKeyColumnName(String primaryKeyColumnName) {
        this.primaryKeyColumnName = primaryKeyColumnName;
    }

    /**
     * Gets the foreign key of the table.
     *
     * @return the foreign key
     */
    public ForeignKey getForeignKey() {
        return foreignKey;
    }

    /**
     * Sets the foreign key of the table.
     *
     * @param foreignKey the new foreign key
     */
    public void setForeignKey(ForeignKey foreignKey) {
        this.foreignKey = foreignKey;
    }

    /**
     * Gets the foreign key details of the table.
     *
     * @return the foreign key details or null if no foreign key exists
     */
    public ForeignKey getForeignKeyDetails() {
        if (foreignKey != null) {
            return foreignKey;
        }
        return null;
    }

    /**
     * Gets the names of the columns in the table.
     *
     * @return the list of column names
     */
    public List<String> getColumnNames() {
        List<String> columnNames = new ArrayList<>();
        for (Column column : columns) {
            columnNames.add(column.getColumnName());
        }
        return columnNames;
    }

    /**
     * Gets the data types of the columns in the table.
     *
     * @return the list of column data types
     */
    public List<String> getColumnDataTypes() {
        List<String> columnDataTypes = new ArrayList<>();
        for (Column column : columns) {
            columnDataTypes.add(column.getColumnDataType());
        }
        return columnDataTypes;
    }

    /**
     * Gets the sizes of the columns in the table.
     *
     * @return the list of column sizes
     */
    public List<Integer> getColumnSizes() {
        List<Integer> columnSizes = new ArrayList<>();
        for (Column column : columns) {
            columnSizes.add(column.getDataTypeSize());
        }
        return columnSizes;
    }

    /**
     * Gets the primary key column name.
     *
     * @return the primary key column name
     */
    public String getPrimaryKeyColumnName() {
        return primaryKeyColumnName;
    }

    /**
     * Adds a column to the table.
     *
     * @param columnName the column name
     * @param columnDataType the column data type
     * @param dataTypeSize the size of the data type
     * @param primaryKeyColumnName the primary key column name
     */
    public void addColumn(String columnName, String columnDataType, int dataTypeSize, String primaryKeyColumnName) {
        this.primaryKeyColumnName = primaryKeyColumnName;
        if (columnDataType.equalsIgnoreCase("int")) {
            dataTypeSize = Integer.MAX_VALUE;
        }
        if (columnDataType.equalsIgnoreCase("float")) {
            dataTypeSize = (int) Float.MAX_VALUE;
        }
        if (columnDataType.equalsIgnoreCase("double")) {
            dataTypeSize = (int) Double.MAX_VALUE;
        }
        Column column = new Column(columnName, columnDataType, dataTypeSize);
        columns.add(column);
    }

    /**
     * Inserts data into the table.
     *
     * @param columnNames the list of column names
     * @param values the list of values
     * @return the result of the insertion
     */
    public String insertData(List<String> columnNames, List<String> values) {
        boolean areAllEntriesValid = validateDataTypes(columns, columnNames, values);
        boolean isRecordPresent = isRecordPresent(columnNames,values);
        if (!areAllEntriesValid) {
            return "-1";
        }
        if(isRecordPresent){
            System.out.println("The record with the primary key already exists in the table");
            return "-1";
        }
        Row row = new Row();
        String result = row.addDataRow(columnNames, values, primaryKeyColumnName, id);
        data.add(row);
        id++;
        return result;
    }

    /**
     * Validates the data types of the values to be inserted.
     *
     * @param columnsInDatabase the columns in the database
     * @param columnNames the list of column names
     * @param values the list of values
     * @return true if all entries are valid, otherwise false
     */
    private boolean validateDataTypes(List<Column> columnsInDatabase, List<String> columnNames, List<String> values) {
        boolean allValid = true;
        for (int i = 0; i < columnNames.size(); i++) {
            String columnNameToBeInsertedWithData = columnNames.get(i);
            String valueToBeInserted = values.get(i);
            for (Column column : columnsInDatabase) {
                if (column.getColumnName().equalsIgnoreCase(columnNameToBeInsertedWithData)) {
                    if (column.getColumnDataType().equalsIgnoreCase("int")) {
                        if (!valueToBeInserted.matches("\\d+")) {
                            allValid = false;
                        }
                        break;
                    } else if (column.getColumnDataType().equalsIgnoreCase("float")) {
                        if (!valueToBeInserted.matches("\\d+")) {
                            allValid = false;
                        }
                        break;
                    } else if (column.getColumnDataType().equalsIgnoreCase("double")) {
                        if (!valueToBeInserted.matches("\\d+")) {
                            allValid = false;
                        }
                        break;
                    } else {
                        //For Others
                    }
                }
            }

        }
        return allValid;
    }

    /**
     * Retrieves data from the table based on specified conditions.
     *
     * @param columnNames the list of column names to retrieve
     * @param columnName the column name for the condition
     * @param value the value for the condition
     * @return the list of rows that match the condition
     */
    public List<Row> getDataFromTable(List<String> columnNames, String columnName, String value) {
        List<Row> result = new ArrayList<>();
        Map<String, String> rowData = new LinkedHashMap<>();
        if (Objects.isNull(columnName) && Objects.isNull(value)) {
            return filterData(columnNames, data);
        }
        for (Row row : data) {
            rowData = row.getDataValue();
            for (Map.Entry<String, String> entry : rowData.entrySet()) {
                if (entry.getKey().equalsIgnoreCase(columnName) && entry.getValue().equalsIgnoreCase(value)) {
                    result.add(row);
                }
            }
        }
        if (Objects.isNull(columnNames) || columnNames.isEmpty()) {
            return result;
        }
        return filterData(columnNames, result);
    }

    /**
     * Updates data in the table based on a condition.
     *
     * @param columnNames the list of column names to update
     * @param values the new values
     * @param whereColumn the column name to filter by
     * @param whereValue the value to filter by
     * @return a message indicating the result of the update operation
     */
    public String updateData(List<String> columnNames, List<String> values, String whereColumn, String whereValue) {
        List<Row> rowsToUpdate = getDataFromTable(null, whereColumn, whereValue);
        if (rowsToUpdate.isEmpty()) {
            return "The given criteria Data is not present in the database";
        }
        for (Row row : rowsToUpdate) {
            String updateResult = row.updateDataRow(columnNames, values);
        }
        return "Updated the data successfully";
    }

    /**
     * Deletes data from the table based on a condition.
     *
     * @param whereColumn the column name to filter by
     * @param whereValue the value to filter by
     * @return a message indicating the result of the delete operation
     */
    public String deleteData(String whereColumn, String whereValue) {
        List<Row> rowsToDelete = getDataFromTable(null, whereColumn, whereValue);
        if (rowsToDelete.isEmpty()) {
            return "The given criteria Data is not present in the database";
        }
        List<Row> updatedData = new ArrayList<>();
        for (Row row : data) {
            for (Row rowToDelete : rowsToDelete) {
                if (!row.equals(rowToDelete)) {
                    updatedData.add(row);
                }
            }
        }
        data.clear();
        data.addAll(updatedData);
        return "Successfully delete the required entries";
    }

    /**
     * Serializes the table to a string representation.
     *
     * @param indent the indentation for formatting the output
     * @return the serialized string representation of the table
     */
    public String serialize(String indent) {
        StringBuilder sb = new StringBuilder();
        sb.append(indent).append("- Table: ").append(tableName).append("\n");
        sb.append(indent).append("  PrimaryKey: ").append(primaryKeyColumnName).append("\n");
        sb.append(indent).append("  Columns:\n");
        for (Column column : columns) {
            sb.append(column.serialize(indent + "    "));
        }
        sb.append(indent).append("  Data:\n");
        for (Row row : data) {
            sb.append(row.serialize(indent + "    "));
        }
        return sb.toString();
    }

    /**
     * Deserializes the table from a string representation.
     *
     * @param str the string representation of the table
     */
    public void deserialize(String str) {
        String[] parts = str.split(": ");
        if (parts.length > 1) {
            if (parts[0].trim().equals("- Table")) {
                tableName = parts[1].trim();
            } else if (parts[0].trim().equals("PrimaryKey")) {
                primaryKeyColumnName = parts[1].trim();
            } else if (parts[0].trim().equals("  Columns")) {
                columns.clear();
            } else if (parts[0].trim().equals("  Data")) {
                data.clear();
            } else if (!columns.isEmpty() || !data.isEmpty()) {
                if (!columns.isEmpty()) {
                    columns.get(columns.size() - 1).deserialize(str);
                } else {
                    Row row = new Row();
                    row.deserialize(str);
                    data.add(row);
                }
            } else {
                Column column = new Column("", "", 0);
                column.deserialize(str);
                columns.add(column);
            }
        }
    }

    /**
     * Filters the data rows based on the specified column names.
     *
     * @param columnNames the list of column names to include in the filtered data
     * @param rows the list of rows to be filtered
     * @return the filtered list of rows
     */
    private List<Row> filterData(List<String> columnNames, List<Row> rows) {
        if (Objects.isNull(columnNames)) {
            return rows;
        }
        List<Row> finalRows = new ArrayList<>();
        for (Row row : rows) {
            Map<String, String> data = row.getDataValue();
            Map<String, String> modifiedData = new LinkedHashMap<>();
            for (String column : columnNames) {
                if (data.containsKey(column)) {
                    modifiedData.put(column, data.get(column));
                }
            }
            Row row1 = new Row();
            row1.setDataValue(modifiedData);
            finalRows.add(row1);
        }
        return finalRows;
    }

    /**
     * Checks if a record with the specified primary key is already present in the table.
     *
     * @param columnNames the list of column names
     * @param values the list of values
     * @return true if the record is present, otherwise false
     */
    boolean isRecordPresent(List<String> columnNames, List<String> values){
        int indexOfPrimaryKeyValue= columnNames.indexOf(primaryKeyColumnName);
        String inputValue = values.get(indexOfPrimaryKeyValue);
        List<Row> data = getDataFromTable(columnNames,primaryKeyColumnName,inputValue);
        if(!data.isEmpty()){
            return true;
        }
        return false;
    }

    /**
     * Creates a deep copy of the Table object.
     *
     * @return a clone of the Table object
     * @throws CloneNotSupportedException if the cloning is not supported
     */
    @Override
    public Object clone() throws CloneNotSupportedException {
        Table cloned = (Table) super.clone();
        cloned.columns = new ArrayList<>(this.columns.size());
        for (Column column : this.columns) {
            cloned.columns.add((Column) column.clone()); // Assuming Column implements Cloneable
        }
        cloned.data = new ArrayList<>(this.data.size());
        for (Row row : this.data) {
            cloned.data.add((Row) row.clone()); // Assuming Row implements Cloneable
        }
        return cloned;
    }
}
