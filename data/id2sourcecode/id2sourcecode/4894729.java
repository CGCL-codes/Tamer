    public void process(boolean emptyTables, boolean recordDetails, boolean fieldDetails, Writer output) throws IOException, CSVParseException, NoPrimaryKeyInCSVTableException, CSVWriteDownException {
        for (int i = 0; i < tables.size(); i++) ((CSVTable) tables.elementAt(i)).load();
        output.write("Loaded files\n");
        output.write("Trying to get exclusive lock on the database\n");
        db.beginExclusiveLock();
        output.write("Got exclusive lock on the database!!!\n");
        if (emptyTables) {
            for (int i = 0; i < tables.size(); i++) ((CSVTable) tables.elementAt(i)).emptyTable();
            PoemThread.writeDown();
        }
        output.write("Emptied all tables\n");
        System.err.println("Emptied all tables");
        writeData(output);
        output.write("Written records\n");
        db.endExclusiveLock();
        output.write("Ended exclusive lock on the database!!!\n");
        output.write("***** REPORT ******\n");
        for (int i = 0; i < tables.size(); i++) ((CSVTable) tables.elementAt(i)).report(recordDetails, fieldDetails, output);
    }
