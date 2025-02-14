package entities;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Represents a row in a table, storing column values as key-value pairs.
 */
public class Row implements Cloneable {
    Map<String, String> dataValue;

    /**
     * Constructs a new Row with an empty dataValue map.
     */
    public Row() {
        this.dataValue = new LinkedHashMap<>();
    }

    /**
     * Sets the data values for this row.
     *
     * @param dataValue the map of column names to values
     */
    public void setDataValue(Map<String, String> dataValue) {
        this.dataValue = dataValue;
    }


    /**
     * Gets the data values for this row.
     *
     * @return the map of column names to values
     */
    public Map<String, String> getDataValue() {
        return dataValue;
    }

    /**
     * Adds data to the row.
     *
     * @param columnNames the list of column names
     * @param values the list of values corresponding to the column names
     * @param primaryKey the primary key column name
     * @param currentIdCounter the current ID counter
     * @return the primary key data of the row
     */
    public String addDataRow(List<String> columnNames, List<String> values, String primaryKey, int currentIdCounter) {
        //Map<String,String> dataRow = new HashMap<>();
        int i = 0;
        int sizeOfValues = columnNames.size();
        String primaryKeyData = "";
        for (String columnName : columnNames) {
            if (i < sizeOfValues) {
                if (columnName.equalsIgnoreCase(primaryKey)) {
                    primaryKeyData = values.get(i);
                    if (Objects.isNull(primaryKeyData) || primaryKeyData.isEmpty()) {
                        String newId = String.valueOf(currentIdCounter);
                        primaryKeyData = newId;
                        dataValue.putIfAbsent(columnName, newId);
                    } else {
                        dataValue.putIfAbsent(columnName, primaryKeyData);
                    }
                } else {
                    dataValue.putIfAbsent(columnName, values.get(i));
                }
                i++;
            }
        }
        return primaryKeyData;
    }

    /**
     * Updates data in the row.
     *
     * @param columnNames the list of column names to update
     * @param values the list of new values
     * @return a message indicating the update status
     */
    public String updateDataRow(List<String> columnNames, List<String> values) {
        for (Map.Entry<String, String> data : dataValue.entrySet()) {
            String key = data.getKey();
            if (columnNames.contains(key)) {
                int indexOfColumn = columnNames.indexOf(data.getKey());
                String newValue = values.get(indexOfColumn);
                dataValue.put(key, newValue);
            }
        }
        return "Successfully Updated the required rows";
    }

    /**
     * Checks if this row is equal to another row.
     *
     * @param rowToCompare the row to compare with
     * @return true if the rows are equal, otherwise false
     */
    public boolean equals(Row rowToCompare) {
        if (rowToCompare.dataValue.equals(dataValue)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Serializes the row to a string with the specified indentation.
     *
     * @param indent the indentation to use
     * @return the serialized string representation of the row
     */
    public String serialize(String indent) {
        StringBuilder sb = new StringBuilder();
        sb.append(indent).append("- Row:\n");
        for (Map.Entry<String, String> entry : dataValue.entrySet()) {
            sb.append(indent).append("  ").append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
        }
        return sb.toString();
    }

    /**
     * Deserializes the row from a string.
     *
     * @param str the string to deserialize from
     */
    public void deserialize(String str) {
        String[] parts = str.split(": ");
        if (parts.length > 1) {
            dataValue.put(parts[0].trim(), parts[1].trim());
        }
    }

    /**
     * Creates and returns a copy of this row.
     *
     * @return a clone of this row
     * @throws CloneNotSupportedException if the row cannot be cloned
     */
    @Override
    public Object clone() throws CloneNotSupportedException {
        Row cloned = (Row) super.clone();
        cloned.dataValue = new LinkedHashMap<>(this.dataValue);
        return cloned;
    }
}
