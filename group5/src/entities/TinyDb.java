package entities;

import queries.CreateTableQuery;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Represents a database management system with functionalities to manage multiple databases.
 */
public class TinyDb {
    List<Database> databases;
    Database database;

    /**
     * Constructs a new TinyDb instance with an empty list of databases.
     */
    public TinyDb() {
        databases = new ArrayList<>();
    }

    /**
     * Gets the list of databases managed by TinyDb.
     *
     * @return the list of databases
     */
    public List<Database> getDatabases(){
        return databases;
    }

    /**
     * Retrieves a database by its name.
     *
     * @param databaseName the name of the database to retrieve
     * @return the database with the specified name, or null if not found
     */
    public Database getDatabaseByName(String databaseName) {
        for (Database database : databases) {
            if (database.getDatabaseName().equalsIgnoreCase(databaseName)) {
                return database;
            }
        }
        return null;
    }

    /**
     * Adds a new database to TinyDb.
     *
     * @param databaseName the name of the database to add
     * @return a message indicating the result of the operation
     */
    public String addNewDatabase(String databaseName) {
        Database databaseDb = getDatabaseByName(databaseName);
        if (Objects.isNull(databaseDb)) {
            Database database = new Database(databaseName);
            databases.add(database);
        }
        return "Successfully Created the Database";
    }

    /**
     * Updates an existing database in TinyDb.
     *
     * @param database the database object with updated details
     * @return a message indicating the result of the update operation
     * @throws CloneNotSupportedException if cloning is not supported
     */
    public String updateDatabase(Database database) throws CloneNotSupportedException {
        Database databasedb = getDatabaseByName(database.getDatabaseName());
        if (Objects.nonNull(databasedb)) {
            databases.remove(databasedb);
            databasedb = database;
            databases.add(databasedb);
            return "Successfully updated the Database in TinyDb Object";
        } else {
            return "Failed to update the Database in TinyDb Object";
        }
    }

    /**
     * Saves the state of TinyDb to a file.
     *
     * @param filename the name of the file to save to
     * @throws IOException if an I/O error occurs
     */
    public void saveToFile(String filename) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            writer.write(serialize());
        }
    }

    /**
     * Loads the state of TinyDb from a file.
     *
     * @param filename the name of the file to load from
     * @throws IOException if an I/O error occurs
     */
    public void loadFromFile(String filename) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
            deserialize(sb.toString());
        }
    }

    /**
     * Drops a database from TinyDb.
     *
     * @param databaseName the name of the database to drop
     */
    public void dropDatabase(String databaseName) {
        Database databaseToDrop = getDatabaseByName(databaseName);
        databases.remove(databaseToDrop);
        System.out.println("Successfully Dropped the database:" + databaseName);
    }

    /**
     * Serializes the state of TinyDb to a string representation.
     *
     * @return the serialized string representation of TinyDb
     */
    public String serialize() {
        StringBuilder sb = new StringBuilder();
        sb.append("TinyDb:\n");
        for (Database db : databases) {
            sb.append(db.serialize("  "));
        }
        return sb.toString();
    }

    /**
     * Deserializes TinyDb from a string representation.
     *
     * @param str the string representation of TinyDb
     */
    public void deserialize(String str) {
        String[] lines = str.split("\n");
        databases.clear();
        Database currentDb = null;
        Table currentTable = null;

        for (String line : lines) {
            if (line.startsWith("  - Database:")) {
                if (currentDb != null) {
                    if (currentTable != null) {
                        currentDb.tables.add(currentTable);
                    }
                    databases.add(currentDb);
                }
                currentDb = new Database(line.split(": ")[1].trim());
                currentTable = null;
            } else if (line.startsWith("    - Table:")) {
                if (currentTable != null) {
                    currentDb.tables.add(currentTable);
                }
                currentTable = new Table(line.split(": ")[1].trim());
            } else if (line.trim().startsWith("PrimaryKey:")) {
                if (currentTable != null) {
                    currentTable.primaryKeyColumnName = line.split(": ")[1].trim();
                }
            } else if (line.trim().startsWith("- Column:")) {
                Column column = new Column(line.split(": ")[1].trim(), "", 0);
                currentTable.columns.add(column);
            } else if (line.trim().startsWith("DataType:")) {
                currentTable.columns.get(currentTable.columns.size() - 1).columnDataType = line.split(": ")[1].trim();
            } else if (line.trim().startsWith("Size:")) {
                currentTable.columns.get(currentTable.columns.size() - 1).dataTypeSize = Integer.parseInt(line.split(": ")[1].trim());
            } else if (line.trim().startsWith("- Row:")) {
                currentTable.data.add(new Row());
            } else if (line.contains(":") && currentTable != null && !currentTable.data.isEmpty()) {
                String[] keyValue = line.trim().split(": ");
                currentTable.data.get(currentTable.data.size() - 1).dataValue.put(keyValue[0], keyValue[1]);
            }
        }

        if (currentDb != null) {
            if (currentTable != null) {
                currentDb.tables.add(currentTable);
            }
            databases.add(currentDb);
        }
    }
}
