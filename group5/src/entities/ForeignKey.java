package entities;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;


/**
 * The ForeignKey class represents foreign key constraints for a database table.
 * It provides methods for serialization, deserialization and file operations
 * to manage foreign key metadata.
 */
public class ForeignKey {
    private String tableName;
    private List<String> foreignKeys;
    private List<String> referenceTables;
    private List<String> referenceTableColumns;

    /**
     * Constructs a ForeignKey object with the specified table name, foreign keys,
     * reference tables, and reference table columns.
     *
     * @param tableName             the name of the table
     * @param foreignKeys           the list of foreign keys
     * @param referenceTables       the list of reference tables
     * @param referenceTableColumns the list of reference table columns
     */
    public ForeignKey(String tableName, List<String> foreignKeys, List<String> referenceTables, List<String> referenceTableColumns) {
        this.tableName = tableName;
        this.foreignKeys = foreignKeys;
        this.referenceTables = referenceTables;
        this.referenceTableColumns = referenceTableColumns;
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
     * Gets the list of foreign keys.
     *
     * @return the list of foreign keys
     */
    public List<String> getForeignKeys() {
        return foreignKeys;
    }

    /**
     * Gets the list of reference tables.
     *
     * @return the list of reference tables
     */
    public List<String> getReferenceTables() {
        return referenceTables;
    }

    /**
     * Gets the list of reference table columns.
     *
     * @return the list of reference table columns
     */
    public List<String> getReferenceTableColumns() {
        return referenceTableColumns;
    }

    /**
     * Serializes the foreign key metadata to a string format.
     *
     * @param databaseName the name of the database
     * @param tableName    the name of the table
     * @return the serialized string representation of the foreign key metadata
     */
    public String serialize(String databaseName, String tableName) {
        StringBuilder sb = new StringBuilder();
        sb.append("Database: ").append(databaseName).append("\n");
        sb.append("Table: ").append(tableName).append("\n");
        sb.append("ForeignKeys: ").append(String.join(",", foreignKeys)).append("\n");
        sb.append("ReferenceTables: ").append(String.join(",", referenceTables)).append("\n");
        sb.append("ReferenceTableColumns: ").append(String.join(",", referenceTableColumns)).append("\n");
        return sb.toString();
    }

    /**
     * Saves the foreign key metadata to a file.
     *
     * @param filename     the name of the file
     * @param databaseName the name of the database
     * @param tableName    the name of the table
     * @throws IOException if an I/O error occurs
     */
    public void saveToFile(String filename, String databaseName, String tableName) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename, true))) {
            writer.write(serialize(databaseName, tableName));
        }
    }

    /**
     * Loads the foreign key metadata from a file.
     *
     * @param filename the name of the file
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
     * Deserializes the foreign key metadata from a string format.
     *
     * @param str the serialized string representation of the foreign key metadata
     */
    public void deserialize(String str) {
        String[] lines = str.split("\n");
        for (String line : lines) {
            if (line.trim().startsWith("ForeignKeys: ")) {
                foreignKeys = Arrays.asList(line.trim().substring(13).split(","));
            } else if (line.trim().startsWith("ReferenceTables: ")) {
                referenceTables = Arrays.asList(line.trim().substring(17).split(","));
            } else if (line.trim().startsWith("ReferenceTableColumns: ")) {
                referenceTableColumns = Arrays.asList(line.trim().substring(22).split(","));
            }
        }
    }

    /**
     * Generates foreign key metadata and saves it to a file if foreign keys are present.
     *
     * @param filename     the name of the file
     * @param databaseName the name of the database
     * @param tableName    the name of the table
     * @throws IOException if an I/O error occurs
     */
    public void generateForeignKeyMetadata(String filename, String databaseName, String tableName) throws IOException {
        if (!foreignKeys.isEmpty()) {
            saveToFile(filename, databaseName, tableName);
        }
    }


    /**
     * Removes the foreign key metadata for a specified table from a file.
     *
     * @param filename  the name of the file
     * @param tableName the name of the table
     * @throws IOException if an I/O error occurs
     */
    public static void removeForeignKeyMetadata(String filename, String tableName) throws IOException {
        File inputFile = new File(filename);
        File tempFile = new File("temp_" + filename);

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile));
             BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {

            String line;
            boolean skip = false;

            while ((line = reader.readLine()) != null) {
                if (line.trim().startsWith("Database: ")) {
                    writer.write(line);
                    writer.newLine();
                    line = reader.readLine();

                    if (line != null && line.trim().startsWith("Table: " + tableName)) {
                        skip = true;
                        while ((line = reader.readLine()) != null && !line.trim().startsWith("Database: ")) {
                            // for Database: "DatabaseName" in foreignkey.txt
                        }
                    } else if (line != null) {
                        writer.write(line);
                        writer.newLine();
                    }

                    while ((line = reader.readLine()) != null && !line.trim().startsWith("Database: ")) {
                        if (!skip) {
                            writer.write(line);
                            writer.newLine();
                        }
                    }
                    skip = false;
                } else {
                    writer.write(line);
                    writer.newLine();
                }
            }
        }

        if (!inputFile.delete()) {
            throw new IOException("Failed to delete original file");
        }
        if (!tempFile.renameTo(inputFile)) {
            throw new IOException("Failed to rename temp file");
        }
    }

}


