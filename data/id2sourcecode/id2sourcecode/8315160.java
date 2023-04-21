    public void report(boolean recordDetails, boolean fieldDetails, Writer output) throws IOException {
        output.write("*** TABLE: " + table.getName().toUpperCase() + " **\n\n");
        output.write("** I have read " + records.size() + " records of " + columnsInUploadOrder.size() + " fields\n");
        if (recordDetails) {
            for (int i = 0; i < records.size(); i++) {
                CSVRecord record = (CSVRecord) records.elementAt(i);
                output.write("   Record: CSV primary key = " + record.primaryKeyValue);
                if (record.poemRecord == null) output.write(", No Poem Persistent written\n"); else output.write(", Poem Troid = " + record.poemRecord.troid() + "\n");
                if (fieldDetails) {
                    for (int j = 0; j < record.size(); j++) {
                        CSVField field = (CSVField) record.elementAt(j);
                        output.write(field.column + "=\"" + field.value);
                        if (j < record.size() - 1) output.write("\","); else output.write("\"\n");
                    }
                }
            }
        }
        output.write("** Currently " + table.count(null) + " Persistents in this table\n\n");
    }
