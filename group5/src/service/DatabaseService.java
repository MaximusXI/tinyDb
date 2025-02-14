package service;

import Utils.DatabaseInfo;
import Utils.QueryLogger;
import entities.Column;
import entities.Database;
import entities.ForeignKey;
import entities.Row;
import entities.SQLQueryParser;
import entities.Table;
import entities.TinyDb;
import queries.CreateDatabaseQuery;
import queries.CreateTableQuery;
import queries.DeleteQuery;
import queries.DropTableQuery;
import queries.InsertQuery;
import queries.SelectQuery;
import queries.TransactionQuery;
import queries.UpdateQuery;
import queries.UseDatabaseQuery;

import java.io.*;
import java.util.*;

/**
 * Provides services for managing and interacting with databases.
 * The {@code DatabaseService} class allows users to perform various operations on databases,
 * including loading and saving databases, processing SQL queries, managing transactions,
 * generating Entity-Relationship Diagrams (ERD), and dumping database schemas and data.
 */
public class DatabaseService {
    TinyDb tinyDb;

    SQLQueryParser sqlQueryParser;

    static Database currentDatabase;

    Database databaseCopy;

    boolean isTransactionMode = false;

    public String databaseName="";

    ForeignKey foreignKey;

    /**
     * Constructs a new {@code DatabaseService} instance and initializes the service.
     * Loads the database from a file and sets up the SQL query parser.
     */
    public DatabaseService() {
        tinyDb = new TinyDb();
        loadDatabaseFromFile();
        sqlQueryParser = new SQLQueryParser();
        currentDatabase = null;
        databaseCopy = null;
    }


    /**
     * Loads the database from a file named {@code "database.txt"}.
     * If the file does not exist, an error message is printed.
     */
    private void loadDatabaseFromFile() {
        try {
            tinyDb.loadFromFile("database.txt");
        } catch (IOException e) {
            System.out.println("The database configurations doesn't exist.");
        }
    }

    /**
     * Saves the current state of the database to a file named {@code "database.txt"}.
     * Updates the TinyDb object with the current database and handles exceptions that may occur.
     */
    public void saveToFile() {
        try {
            tinyDb.updateDatabase(currentDatabase);
            tinyDb.saveToFile("database.txt");
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Processes the given SQL query and performs the corresponding database operation.
     * Logs query events and execution time.
     *
     * @param query the SQL query to be processed
     * @param userId the ID of the user executing the query
     * @throws Exception if an error occurs during query processing
     */
    public void processQueries(String query, String userId) throws Exception {

        try{
            long startTime = 0;
            long endTime = 0;
            long duration = 0;
            long durationInMillis = 0;
            startTime = System.nanoTime();
            Object object = sqlQueryParser.parse(query);

            if (object instanceof UseDatabaseQuery) {

                databaseName = ((UseDatabaseQuery) object).getDatabaseName();
                currentDatabase = tinyDb.getDatabaseByName(databaseName);
                currentDatabase.logDatabaseState(databaseName);
            } else if (object instanceof CreateDatabaseQuery) {

                String databaseToCreate = ((CreateDatabaseQuery) object).getDatabaseName();
                tinyDb.addNewDatabase(databaseToCreate);
                tinyDb.saveToFile("database.txt");

                // Log the event
                QueryLogger.logQuery(userId, "Create Database","Created database: " + databaseToCreate);
                currentDatabase.logDatabaseState(databaseName);
            } else if (object instanceof CreateTableQuery) {
                /*if (Objects.isNull(currentDatabase)) {
                    System.out.println("No Database is Selected");
                    QueryLogger.logQuery(userId, "NO DATABASE SELECTED",query);
                } else {
                    currentDatabase.addTable(((CreateTableQuery) object).getTableName(),
                            ((CreateTableQuery) object).getColumnNames(),
                            ((CreateTableQuery) object).getColumnDataTypes(),
                            ((CreateTableQuery) object).getColumnSizes(),
                            ((CreateTableQuery) object).getPrimaryKeyColumn());
                    tinyDb.saveToFile("database.txt");
                    if (((CreateTableQuery) object).getForeignKey() != null) {
                        System.out.println(((CreateTableQuery) object).getForeignKey().getForeignKeys());
                    ((CreateTableQuery) object).getForeignKey().generateForeignKeyMetadata("foreignkey.txt", currentDatabase.getDatabaseName(), ((CreateTableQuery) object).getTableName());
                } else {
                    System.out.println("getForeignKey() != null");
                }
                // Log the event
                QueryLogger.logQuery(userId, "CREATE TABLE",query);
                currentDatabase.logDatabaseState(databaseName);
            }*/
            if (Objects.isNull(currentDatabase)) {
                System.out.println("No Database is Selected");
                QueryLogger.logQuery(userId, "NO DATABASE SELECTED", query);
            } else {
                Map<String, Set<String>> databaseMetadata;
                try {
                    databaseMetadata = readDatabaseMetadata("database.txt");
                    System.out.println("databaseMetadata" + databaseMetadata);
                } catch (IOException e) {
                    System.out.println("Error reading database metadata: " + e.getMessage());
                    return;
                }

                CreateTableQuery createTableQuery = (CreateTableQuery) object;
                String tableName = createTableQuery.getTableName();
                ForeignKey foreignKey = createTableQuery.getForeignKey();

                boolean foreignKeyValid = validateForeignKeyConstraints(foreignKey, databaseMetadata);

                if (foreignKeyValid) {
                    currentDatabase.addTable(
                            tableName,
                            createTableQuery.getColumnNames(),
                            createTableQuery.getColumnDataTypes(),
                            createTableQuery.getColumnSizes(),
                            createTableQuery.getPrimaryKeyColumn()
                    );
                    tinyDb.saveToFile("database.txt");

                    if (foreignKey != null) {
                        System.out.println(foreignKey.getForeignKeys());
                        foreignKey.generateForeignKeyMetadata("foreignkey.txt", currentDatabase.getDatabaseName(), tableName);
                    } else {
                        System.out.println("No foreign key constraints");
                    }

                    // Log the event
                    QueryLogger.logQuery(userId, "CREATE TABLE", query);
                    currentDatabase.logDatabaseState(databaseName);
                } else {
                    System.out.println("Failed to create table due to invalid foreign key constraints.");
                }
                }
            } else if (object instanceof InsertQuery) {
                if (Objects.isNull(currentDatabase)) {
                    System.out.println("No Database is Selected");
                    QueryLogger.logQuery(userId, "NO DATABASE SELECTED",query);
                } else {
                    tinyDb.loadFromFile("database.txt");
                    currentDatabase = tinyDb.getDatabaseByName(databaseName);
                    if (!isTransactionMode) {
                        boolean isTableLock = checkTableTransactionLockInFile(((InsertQuery) object).getTableName(), userId);
                        if (isTableLock) {
                            System.out.println("Cannot access the Table");
                            QueryLogger.logEvent(userId, "tried accessing " + ((DeleteQuery) object).getTableName() + " but its already locked.");
                            return;
                        }
                        String result = currentDatabase.insertData(((InsertQuery) object).getTableName(),
                                ((InsertQuery) object).getColumnNames(),
                                ((InsertQuery) object).getValues());
                        if (result.equalsIgnoreCase("-1")) {
                            System.out.println("Invalid entry in the insert query.");
                        } else {
                            System.out.println("Successfully added entry with Id:" + result);
                        }
                        tinyDb.saveToFile("database.txt");

                        // Log the event
                        QueryLogger.logQuery(userId,"INSERT ROWS", query);
                        currentDatabase.logDatabaseState(databaseName);
                    } else {
                        boolean isTableLock = checkTableTransactionLockInFile(((InsertQuery) object).getTableName(), userId);
                        if (isTableLock) {
                            System.out.println("Cannot access the Table");
                            QueryLogger.logEvent(userId, "tried accessing " + ((DeleteQuery) object).getTableName() + " but its already locked.");
                            return;
                        }
                        boolean successWrite = saveTransactionLockInFile(((InsertQuery) object).getTableName(), userId);
                        String result = databaseCopy.insertData(((InsertQuery) object).getTableName(),
                                ((InsertQuery) object).getColumnNames(),
                                ((InsertQuery) object).getValues());
                        if (result.equalsIgnoreCase("-1")) {
                            System.out.println("Invalid entry in the insert query.");
                        } else {
                            System.out.println("Successfully added entry with Id:" + result);
                        }
                    }
                }
            } else if (object instanceof SelectQuery) {
                if (Objects.isNull(currentDatabase)) {
                    System.out.println("No Database is Selected");
                    QueryLogger.logQuery(userId, "NO DATABASE SELECTED",query);
                } else {
                    tinyDb.loadFromFile("database.txt");
                    currentDatabase = tinyDb.getDatabaseByName(databaseName);
                    List<Row> result = currentDatabase.selectData(((SelectQuery) object).getTableName(),
                            ((SelectQuery) object).getColumns(),
                            ((SelectQuery) object).getConditionColumn(),
                            ((SelectQuery) object).getConditionValue());
                    for (Row row : result) {
                        System.out.println(row.getDataValue());
                    }
                    // Log the event
                    QueryLogger.logQuery(userId,"SELECT ROWS", query);
                    currentDatabase.logDatabaseState(databaseName);
                }
            } else if (object instanceof UpdateQuery) {
                if (Objects.isNull(currentDatabase)) {
                    System.out.println("No Database is Selected");
                    QueryLogger.logQuery(userId, "NO DATABASE SELECTED",query);
                } else {
                    tinyDb.loadFromFile("database.txt");
                    currentDatabase = tinyDb.getDatabaseByName(databaseName);
                    if (!isTransactionMode) {
                        boolean isTableLock = checkTableTransactionLockInFile(((UpdateQuery) object).getTableName(), userId);
                        if (isTableLock) {
                            System.out.println("Cannot access the Table");
                            QueryLogger.logEvent(userId, "tried accessing " + ((DeleteQuery) object).getTableName() + " but its already locked.");
                            return;
                        }
                        String result = currentDatabase.updateData(((UpdateQuery) object).getTableName(),
                                ((UpdateQuery) object).getSetColumn(),
                                ((UpdateQuery) object).getSetValue(),
                                ((UpdateQuery) object).getConditionColumn(),
                                ((UpdateQuery) object).getConditionValue());
                        System.out.println("Successfully updated the entry:" + result);
                        tinyDb.saveToFile("database.txt");

                        // Log the event
                        QueryLogger.logQuery(userId,"UPDATE ROWS", query);
                        currentDatabase.logDatabaseState(databaseName);
                    } else {
                        boolean isTableLock = checkTableTransactionLockInFile(((UpdateQuery) object).getTableName(), userId);
                        if (isTableLock) {
                            System.out.println("Cannot access the Table");
                            QueryLogger.logEvent(userId, "tried accessing " + ((DeleteQuery) object).getTableName() + " but its already locked.");
                            return;
                        }
                        boolean successWrite = saveTransactionLockInFile(((UpdateQuery) object).getTableName(), userId);
                        String result = databaseCopy.updateData(((UpdateQuery) object).getTableName(),
                                ((UpdateQuery) object).getSetColumn(),
                                ((UpdateQuery) object).getSetValue(),
                                ((UpdateQuery) object).getConditionColumn(),
                                ((UpdateQuery) object).getConditionValue());
                        System.out.println("Successfully updated the entry:" + result);
                        currentDatabase.logDatabaseState(databaseName);
                    }

                }
            } else if (object instanceof DeleteQuery) {
                if (Objects.isNull(currentDatabase)) {
                    System.out.println("No Database is Selected");
                    QueryLogger.logQuery(userId, "NO DATABASE SELECTED",query);
                } else {
                    tinyDb.loadFromFile("database.txt");
                    currentDatabase = tinyDb.getDatabaseByName(databaseName);
                    if (!isTransactionMode) {
                        boolean isTableLock = checkTableTransactionLockInFile(((DeleteQuery) object).getTableName(), userId);
                        if (isTableLock) {
                            System.out.println("Cannot access the Table");
                            QueryLogger.logEvent(userId, "tried accessing " + ((DeleteQuery) object).getTableName() + " but its already locked.");
                            currentDatabase.logDatabaseState(databaseName);
                            return;
                        }
                        String result = currentDatabase.deleteData(((DeleteQuery) object).getTableName(),
                                ((DeleteQuery) object).getConditionColumn(),
                                ((DeleteQuery) object).getConditionValue());
                        tinyDb.saveToFile("database.txt");

                        // Log the event
                        QueryLogger.logQuery(userId, "DELETE ROWS",query);
                        currentDatabase.logDatabaseState(databaseName);
                    } else {
                        boolean isTableLock = checkTableTransactionLockInFile(((DeleteQuery) object).getTableName(), userId);
                        if (isTableLock) {
                            System.out.println("Cannot access the Table");
                            QueryLogger.logEvent(userId, "tried accessing " + ((DeleteQuery) object).getTableName() + " but its already locked.");
                            return;
                        }
                        boolean successWrite = saveTransactionLockInFile(((DeleteQuery) object).getTableName(), userId);
                        String result = databaseCopy.deleteData(((DeleteQuery) object).getTableName(),
                                ((DeleteQuery) object).getConditionColumn(),
                                ((DeleteQuery) object).getConditionValue());
                        currentDatabase.logDatabaseState(databaseName);
                    }
                }
            } else if (object instanceof DropTableQuery) {
            if (Objects.isNull(currentDatabase)) {
                System.out.println("No Database is Selected");
                QueryLogger.logQuery(userId, "NO DATABASE SELECTED", query);
            } else {
                String tableName = ((DropTableQuery) object).getTableName();
                String result = currentDatabase.dropTable(tableName);
                if (result.equalsIgnoreCase("1")) {
                    tinyDb.saveToFile("database.txt");
                    ForeignKey.removeForeignKeyMetadata("foreignkey.txt", tableName);
                    System.out.println("Successfully dropped the table with name:" + tableName);
                } else if (result.equalsIgnoreCase("-1")) {
                    System.out.println("Table does not exist");
                } else {
                    System.out.println("Something went wrong!");
                }
                QueryLogger.logQuery(userId, "DROP TABLE", query);
                currentDatabase.logDatabaseState(databaseName);
            }
        } else if (object instanceof TransactionQuery) {
            if (Objects.isNull(currentDatabase)) {
                System.out.println("No Database is Selected");
                QueryLogger.logQuery(userId, "NO DATABASE SELECTED",query);
            } else {
                String transactionStatement = ((TransactionQuery) object).getStatement();
                if (transactionStatement.equalsIgnoreCase("transaction")) {
                    //Create copy
                    isTransactionMode = true;
                    databaseCopy = (Database) currentDatabase.clone();
                    QueryLogger.logQuery(userId, "TRANSACTION DETECTED",query);
                    currentDatabase.logDatabaseState(databaseName);
                } else if (transactionStatement.equalsIgnoreCase("commit")) {
                    //Save the Copy
                    currentDatabase = null;
                    currentDatabase = (Database) databaseCopy.clone();
                    databaseCopy = null;
                    isTransactionMode = false;
                    tinyDb.updateDatabase(currentDatabase);
                    tinyDb.saveToFile("database.txt");
                    clearFileContent("transactionTable.txt", userId);
                    QueryLogger.logQuery(userId, "TRANSACTION COMMIT",query);
                    currentDatabase.logDatabaseState(databaseName);
                } else if (transactionStatement.equalsIgnoreCase("rollback")) {
                    //Discard the copy
                    databaseCopy = null;
                    isTransactionMode = false;
                    clearFileContent("transactionTable.txt", userId);
                    QueryLogger.logQuery(userId, "TRANSACTION ROLLBACK",query);
                    currentDatabase.logDatabaseState(databaseName);
                }
            }
        } else {
            System.out.println("Invalid Query");
        }
            endTime = System.nanoTime(); // Capture end time
            duration = endTime - startTime; // Calculate duration
            double durationInMs = duration / 1000000.0;
            QueryLogger.logGeneral("Execution Time", "Query: "+query +"\n Execution Time: " + durationInMs + " ms");
        }
        catch (Exception e){
            QueryLogger.logEvent("System", "Error occurred while parsing query: "+e.getMessage());
        }
    }

    /**
     * Saves a transaction lock for the specified table and user to a file named {@code "transactionTable.txt"}.
     *
     * @param tableName the name of the table being locked
     * @param userId the ID of the user performing the transaction
     * @return {@code true} if the lock was successfully saved, {@code false} otherwise
     * @throws IOException if an error occurs while writing to the file
     */
    private boolean saveTransactionLockInFile(String tableName, String userId) throws IOException {
        //Saving to the file in the formal as below
        // TableName:userId
        QueryLogger.logEvent(userId, tableName +" is locked to " +userId + " because of transaction.");
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(tableName).append(":").append(userId).append("\n");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("transactionTable.txt"))) {
            writer.write(stringBuilder.toString());
            return true;
        }
    }

    /**
     * Checks if a table is locked for a user by reading from a file named {@code "transactionTable.txt"}.
     *
     * @param tableName the name of the table to check
     * @param userId the ID of the user
     * @return {@code true} if the table is locked by another user, {@code false} otherwise
     * @throws IOException if an error occurs while reading from the file
     */
    private boolean checkTableTransactionLockInFile(String tableName, String userId) throws IOException {
        //Reading the transaction lock table entries
        try (BufferedReader reader = new BufferedReader(new FileReader("transactionTable.txt"))) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(":");
                if (data[0].equalsIgnoreCase(tableName)) {
                    if (!data[1].equalsIgnoreCase(userId)) {
                        return true;
                    }
                }
            }

        }
        return false;
    }

    /**
     * Clears the content of the specified file and logs the event.
     *
     * @param fileName the name of the file to clear
     * @param userId the ID of the user performing the operation
     * @return {@code true} if the file content was successfully cleared, {@code false} otherwise
     * @throws IOException if an error occurs while clearing the file
     */
    private boolean clearFileContent(String fileName, String userId) throws IOException {
        try {
            FileWriter fw = new FileWriter(fileName, false);
            PrintWriter pw = new PrintWriter(fw, false);
            pw.flush();
            pw.close();
            fw.close();
            QueryLogger.logEvent(userId, "Releasing all locks from the tables.");
        } catch (Exception exception) {
            System.out.println("Exception occurred: " + exception.getMessage());
            QueryLogger.logEvent("System", exception.getMessage());
            return false;
        }
        return true;
    }

    public void generateERD(String foreignKeyFile, String databaseFile, String selectedDatabase) {
        try {
            DatabaseInfo databaseInfo = parseDatabaseFile(databaseFile);

            String erdFileName = selectedDatabase + "_erd.txt";

            File erdFile = new File(erdFileName);
            if (erdFile.exists()) {
                erdFile.delete();
            }

            boolean isEntryWritten = false;

            try (BufferedReader reader = new BufferedReader(new FileReader(foreignKeyFile))) {
                String line;
                StringBuilder sb = new StringBuilder();
                while ((line = reader.readLine()) != null) {
                    sb.append(line).append("\n");
                }
                String[] entries = sb.toString().split("(?=Database:)");

                for (String entry : entries) {
                    if (entry.trim().isEmpty()) continue;

                    String[] lines = entry.split("\n");

                    String databaseName = null;
                    String tableName = null;
                    String foreignKey = null;
                    String referenceTable = null;
                    String referenceTableColumn = null;

                    for (String currentLine : lines) {
                        if (currentLine.trim().startsWith("Database: ")) {
                            databaseName = currentLine.trim().substring(10);
                        } else if (currentLine.trim().startsWith("Table: ")) {
                            tableName = currentLine.trim().substring(7);
                        } else if (currentLine.trim().startsWith("ForeignKeys: ")) {
                            foreignKey = currentLine.trim().substring(13);
                        } else if (currentLine.trim().startsWith("ReferenceTables: ")) {
                            referenceTable = currentLine.trim().substring(17);
                        } else if (currentLine.trim().startsWith("ReferenceTableColumns: ")) {
                            referenceTableColumn = currentLine.trim().substring(22);
                        }
                    }

                    if (!selectedDatabase.equals(databaseName)) {
                        continue;
                    }

                    String referenceTableCardinality = databaseInfo.isPrimaryKey(referenceTable, referenceTableColumn) ? "1" : "N";
                    String foreignKeyCardinality = databaseInfo.isPrimaryKey(tableName, foreignKey) ? "1" : "N";
                    String cardinality = referenceTableCardinality + " to " + foreignKeyCardinality;

                    try (PrintWriter writer = new PrintWriter(new FileWriter(erdFileName, true))) {
                        //writer.println(referenceTable + " (" + referenceTableColumn + ") is related to " + tableName + " (" + foreignKey + ") [" + cardinality + "]");
                        writer.println(referenceTable + " (" + referenceTableColumn + ") has " + tableName + " (" + foreignKey + ") [" + cardinality + "]");
                        isEntryWritten = true;
                    } catch (IOException e) {
                        System.out.println("Failed to write ERD file: " + e.getMessage());
                        QueryLogger.logEvent("System", "Failed to write ERD file: "+e.getMessage());
                    }
                }
            }

            if (isEntryWritten) {
                System.out.println("Successfully generated ERD in file: " + erdFileName);
            } else {
                System.out.println("No entries found for the specified database: " + selectedDatabase);
            }
        } catch (IOException e) {
            System.out.println("Error reading from file: " + e.getMessage());
            QueryLogger.logEvent("System", "Error reading from file: "+e.getMessage());
        }
    }



    /**
     * Parses a database file to extract database information.
     *
     * @param databaseFile the path to the file containing database schema
     * @return a {@code DatabaseInfo} instance with parsed database information
     * @throws IOException if an error occurs while reading the file
     */
    private DatabaseInfo parseDatabaseFile(String databaseFile) {
        DatabaseInfo databaseInfo = new DatabaseInfo();
        try (BufferedReader reader = new BufferedReader(new FileReader(databaseFile))) {
            String line;
            String currentTable = null;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.startsWith("- Table: ")) {
                    currentTable = line.substring(9);
                } else if (line.startsWith("PrimaryKey: ")) {
                    String primaryKey = line.substring(12).trim();
                    if (!primaryKey.equals("null") && currentTable != null) {
                        databaseInfo.addPrimaryKey(currentTable, primaryKey);
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading database file: " + e.getMessage());
            QueryLogger.logEvent("System", "Error reading database file: "+e.getMessage());
        }
        return databaseInfo;
    }


    /**
     * Reads the database metadata from a file.
     *
     * @param filename the name of the file containing the database metadata
     * @return a map where the key is the table name and the value is a set of column names in the table
     * @throws IOException if an I/O error occurs reading from the file
     */
    private static Map<String, Set<String>> readDatabaseMetadata(String filename) throws IOException {
        Map<String, Set<String>> metadata = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            String currentTable = null;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.startsWith("- Table: ")) {
                    currentTable = line.substring(8).trim();
                    metadata.put(currentTable, new HashSet<>());
                } else if (line.startsWith("- Column: ") && currentTable != null) {
                    String columnName = line.substring(9).trim();
                    metadata.get(currentTable).add(columnName);
                }
            }
        }
        return metadata;
    }

    /**
     * Validates foreign key constraints against the database metadata.
     *
     * @param foreignKey the foreign key object containing the foreign key constraints
     * @param databaseMetadata the database metadata containing table and column information
     * @return true if all foreign key constraints are valid otherwise false
     */
    private boolean validateForeignKeyConstraints(ForeignKey foreignKey, Map<String, Set<String>> databaseMetadata) {
        boolean isValid = true;
        for (int i = 0; i < foreignKey.getForeignKeys().size(); i++) {
            String referencedTable = foreignKey.getReferenceTables().get(i);
            String referencedColumn = foreignKey.getReferenceTableColumns().get(i);

            System.out.println("Checking foreign key: " + referencedTable + "(" + referencedColumn + ")");

            if (!databaseMetadata.containsKey(referencedTable)) {
                isValid = false;
                System.out.println("Referenced table does not exist: " + referencedTable);
                break;
            }

            if (!databaseMetadata.get(referencedTable).contains(referencedColumn)) {
                isValid = false;
                System.out.println("Referenced column does not exist in table " + referencedTable + ": " + referencedColumn);
                break;
            }
        }
        return isValid;
    }



    /**
     * Gets all databases and then iterate through each database to get tables and records to create a sql dump will which contains table both data and metadata
     *
     * @return boolean to show if sql dump was successfully done or not.
     */
    public boolean DumpDB() {
        boolean sqlDumpGenerated = false;
        List<Database> databases = tinyDb.getDatabases();

        for (Database database : databases) {
            String dbName = database.getDatabaseName();
            StringBuilder sqlDump = new StringBuilder();

            // Iterate each table present in each db.
            for (Table table : database.getTables()) {
                String tableName = table.getTableName();

                // Append the table schema definition.
                sqlDump.append("DROP TABLE IF EXISTS ").append(tableName).append(";\n");
                sqlDump.append("CREATE TABLE ").append(tableName).append(" (\n");
                List<Column> columns = table.getColumns();
                for (int i = 0; i < columns.size(); i++) {
                    Column column = columns.get(i);
                    String columnName = column.getColumnName();
                    String columnDataType = column.getColumnDataType();
                    int columnSize = column.getDataTypeSize();

                    // Include column size if type is VARCHAR
                    if ("VARCHAR".equalsIgnoreCase(columnDataType)) {
                        sqlDump.append("    ").append(columnName).append(" ").append(columnDataType).append("(").append(columnSize).append(")");
                    } else {
                        sqlDump.append("    ").append(columnName).append(" ").append(columnDataType);
                    }

                    if (i < columns.size() - 1) {
                        sqlDump.append(",");
                    }
                    sqlDump.append("\n");
                }
                sqlDump.append(");\n");

                // Append the row data into sqlDump
                List<Row> rows = table.getData();
                if (!rows.isEmpty()) {
                    sqlDump.append("INSERT INTO ").append(tableName).append(" VALUES \n");
                    for (int i = 0; i < rows.size(); i++) {
                        Row row = rows.get(i);
                        sqlDump.append("    (");
                        Map<String, String> dataValue = row.getDataValue();
                        int j = 0;
                        for (Column column : columns) {
                            String columnName = column.getColumnName();
                            String columnType = column.getColumnDataType();
                            String value = dataValue.get(columnName);

                            if ("INT".equalsIgnoreCase(columnType)) {
                                sqlDump.append(value != null ? value : "NULL");
                            } else {
                                sqlDump.append(value != null ? "'" + value.replace("'", "") + "'" : "NULL");
                            }

                            if (j < columns.size() - 1) {
                                sqlDump.append(", ");
                            }
                            j++;
                        }
                        sqlDump.append(")");
                        if (i < rows.size() - 1) {
                            sqlDump.append(",");
                        }
                        sqlDump.append("\n");
                    }
                    sqlDump.append(";\n");
                }
            }

            // Finally write all generate sqldump into a file, each database will have separate sqldump file.
            try (FileWriter fileWriter = new FileWriter(dbName + "_dump.sql")) {
                fileWriter.write(sqlDump.toString());
                sqlDumpGenerated = true;
            } catch (IOException e) {
                sqlDumpGenerated = false;
                throw new RuntimeException(e.getMessage());
            }
            if(sqlDump != null && !sqlDump.isEmpty()){
                sqlDumpGenerated = true;
            }
        }

        return sqlDumpGenerated;
    }
}
