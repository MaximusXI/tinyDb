package entities;

/**
 * Represents a column in a database table with a name, data type, and size.
 */
public class Column implements Cloneable {
    String columnName;

    String columnDataType;

    int dataTypeSize;

    /**
     * Constructs a Column with the specified name, data type, and size.
     *
     * @param columnName the name of the column
     * @param columnDataType the data type of the column
     * @param dataTypeSize the size of the data type
     */
    Column(String columnName, String columnDataType, int dataTypeSize) {
        this.columnName = columnName;
        this.columnDataType = columnDataType;
        this.dataTypeSize = dataTypeSize;
    }

    /**
     * Gets the name of the column.
     *
     * @return the name of the column
     */
    public String getColumnName() {
        return columnName;
    }

    /**
     * Sets the name of the column.
     *
     * @param columnName the new name of the column
     */
    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    /**
     * Gets the data type of the column.
     *
     * @return the data type of the column
     */
    public String getColumnDataType() {
        return columnDataType;
    }

    /**
     * Gets the size of the data type of the column.
     *
     * @return the size of the data type
     */
    public int getDataTypeSize() {
        return dataTypeSize;
    }

    /**
     * Sets the size of the data type of the column.
     *
     * @param dataTypeSize the new size of the data type
     */
    public void setDataTypeSize(int dataTypeSize) {
        this.dataTypeSize = dataTypeSize;
    }

    /**
     * Serializes the column to a string with the specified indentation.
     *
     * @param indent the indentation to use
     * @return the serialized string representation of the column
     */
    public String serialize(String indent) {
        return indent + "- Column: " + columnName + "\n" +
                indent + "  DataType: " + columnDataType + "\n" +
                indent + "  Size: " + dataTypeSize + "\n";
    }

    /**
     * Deserializes the column from a string.
     *
     * @param str the string to deserialize from
     */
    public void deserialize(String str) {
        String[] parts = str.split(": ");
        if (parts.length > 1) {
            if (parts[0].trim().equals("- Column")) {
                columnName = parts[1].trim();
            } else if (parts[0].trim().equals("DataType")) {
                columnDataType = parts[1].trim();
            } else if (parts[0].trim().equals("Size")) {
                dataTypeSize = Integer.parseInt(parts[1].trim());
            }
        }
    }

    /**
     * Creates and returns a copy of this column.
     *
     * @return a clone of this column
     * @throws CloneNotSupportedException if the column cannot be cloned
     */
    @Override
    public Object clone() throws CloneNotSupportedException {
        return (Column) super.clone();
    }
}
