package entities;

import Utils.QueryLogger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a database containing multiple tables.
 */
public class Database implements Cloneable {

    String databaseName;

    List<Table> tables;

    /**
     * Constructs a new Database with the specified name.
     *
     * @param databaseName the name of the database
     */
    public Database(String databaseName) {
        this.databaseName = databaseName;
        this.tables = new ArrayList<>();
    }

    /**
     * Gets the name of the database.
     *
     * @return the name of the database
     */
    public String getDatabaseName() {
        return databaseName;
    }

    /**
     * Adds a table to the database with the specified details.
     *
     * @param tableName the name of the table
     * @param columnNames the list of column names
     * @param columnDataTypes the list of column data types
     * @param columnSizes the list of column sizes
     * @param primaryKeyColumnName the name of the primary key column
     */
    public void addTable(String tableName, List<String> columnNames, List<String> columnDataTypes, List<Integer> columnSizes, String primaryKeyColumnName) {
        boolean isTableExists = isTablePresent(tableName);
        if (isTableExists) {
            return;
        }
        Table table = new Table(tableName);
        int i = 0;
        int size = columnNames.size();
        for (String columnName : columnNames) {
            if (i < size) {
                table.addColumn(columnName, columnDataTypes.get(i), columnSizes.get(i), primaryKeyColumnName);
                i++;
            }
        }
        tables.add(table);
        System.out.println("Successfully added the table");
    }

    /**
     * Drops a table from the database with the specified name.
     *
     * @param tableName the name of the table to drop
     * @return "1" if the table was successfully dropped, otherwise "-1"
     */
    public String dropTable(String tableName) {
        boolean isTableExists = isTablePresent(tableName);
        if (isTableExists) {
            for (Table table : tables) {
                if (table.getTableName().equalsIgnoreCase(tableName)) {
                    tables.remove(table);
                    break;
                }
            }
            return "1";
        } else {
            return "-1";
        }
    }

    /**
     * Gets the list of tables in the database.
     *
     * @return the list of tables
     */
    public List<Table> getTables(){
        return tables;
    }

    /**
     * Checks if a table with the specified name is present in the database.
     *
     * @param tableName the name of the table to check
     * @return true if the table is present, otherwise false
     */
    private boolean isTablePresent(String tableName) {
        for (Table table : tables) {
            if (table.getTableName().equalsIgnoreCase(tableName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Inserts data into a table in the database.
     *
     * @param tableName the name of the table
     * @param columnName the list of column names
     * @param values the list of values to insert
     * @return the result of the insert operation
     */
    public String insertData(String tableName, List<String> columnName, List<String> values) {
        for (Table table : tables) {
            if (table.getTableName().equalsIgnoreCase(tableName)) {
                String result = table.insertData(columnName, values);
                return result;
            }
        }
        return "Failed Operation";
    }

    /**
     * Selects data from a table in the database.
     *
     * @param tableName the name of the table
     * @param columnNames the list of column names to select
     * @param columnName the column name to filter by
     * @param value the value to filter by
     * @return the list of selected rows
     */
    public List<Row> selectData(String tableName, List<String> columnNames, String columnName, String value) {
        for (Table table : tables) {
            if (table.getTableName().equalsIgnoreCase(tableName)) {
                List<Row> rows = table.getDataFromTable(columnNames, columnName, value);
                return rows;
            }
        }
        return null;
    }

    /**
     * Updates data in a table in the database.
     *
     * @param tableName the name of the table
     * @param columnNames the list of column names to update
     * @param values the list of new values
     * @param whereColumn the column to filter by
     * @param whereValue the value to filter by
     * @return the result of the update operation
     */
    public String updateData(String tableName, List<String> columnNames, List<String> values, String whereColumn, String whereValue) {
        for (Table table : tables) {
            if (table.getTableName().equalsIgnoreCase(tableName)) {
                String result = table.updateData(columnNames, values, whereColumn, whereValue);
                return result;
            }
        }
        return null;
    }

    /**
     * Deletes data from a table in the database.
     *
     * @param tableName the name of the table
     * @param columnName the column name to filter by
     * @param value the value to filter by
     * @return the result of the delete operation
     */
    public String deleteData(String tableName, String columnName, String value) {
        List<Row> dataToBeDeleted = selectData(tableName, null, columnName, value);
        if (dataToBeDeleted.isEmpty()) {
            return "The given criteria records are not present";
        }
        for (Table table : tables) {
            if (table.getTableName().equalsIgnoreCase(tableName)) {
                String result = table.deleteData(columnName, value);
                return result;
            }
        }
        return "Successfully deleted the Given Records with required condition";
    }

    /**
     * Serializes the database to a string with the specified indentation.
     *
     * @param indent the indentation to use
     * @return the serialized string representation of the database
     */
    public String serialize(String indent) {
        StringBuilder sb = new StringBuilder();
        sb.append(indent).append("- Database: ").append(databaseName).append("\n");
        for (Table table : tables) {
            sb.append(table.serialize(indent + "  "));
        }
        return sb.toString();
    }

    /**
     * Deserializes the database from a string.
     *
     * @param str the string to deserialize from
     */
    public void deserialize(String str) {
        String[] parts = str.split(": ");
        if (parts.length > 1) {
            if (parts[0].trim().equals("- Database")) {
                databaseName = parts[1].trim();
            } else {
                if (!tables.isEmpty()) {
                    tables.get(tables.size() - 1).deserialize(str);
                } else {
                    Table table = new Table("");
                    table.deserialize(str);
                    tables.add(table);
                }
            }
        }
    }

    /**
     * Creates and returns a copy of this database.
     *
     * @return a clone of this database
     * @throws CloneNotSupportedException if the database cannot be cloned
     */
    @Override
    public Object clone() throws CloneNotSupportedException {
        Database cloned = (Database) super.clone();
        cloned.tables = new ArrayList<>(this.tables.size());
        for (Table table : this.tables) {
            cloned.tables.add((Table) table.clone());
        }
        return cloned;
    }

    /**
     * Logs the state of the database.
     *
     * @param databaseName the name of the database
     */
    public void logDatabaseState(String databaseName) {
        StringBuilder dbState = new StringBuilder();
        dbState.append("Database Name: ").append(databaseName).append("  ");
        dbState.append("Number of Tables: ").append(tables.size()).append("  ");

        for (Table table : tables) {
            dbState.append("Table Name: ").append(table.getTableName()).append("  ");
            dbState.append("Number of Records: ").append(table.data.size()).append("  ");
        }

        QueryLogger.logGeneral("DATABASE STATE", dbState.toString());
    }
}
