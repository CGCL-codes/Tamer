    public Map<String, ThsTable> loadSchema() throws SQLException {
        Map<String, ThsTable> tables = new HashMap<String, ThsTable>();
        try {
            File defFileHnd = new File(dataDir, DEF_FILENAME);
            defFile = new RandomAccessFile(defFileHnd, "rw");
            FileChannel fCh = defFile.getChannel();
            defFileLock = fCh.lock(0, fCh.size(), true);
            String line;
            while ((line = defFile.readLine()) != null) {
                ThsTable table = parseTableDef(line);
                String tableName = table.getName();
                File tableFile = new File(dataDir, tableName);
                if (!tableFile.exists()) throw new SQLException("Table file not found");
                tables.put(tableName, table);
                tableEntities.put(tableName, new PhysTableEntity(tableFile));
            }
            return tables;
        } catch (IOException e) {
            throw (SQLException) new SQLException().initCause(e);
        }
    }
