    private void processGeneralInformationRecord(final PdbRecord record, final Database database) throws Exception {
        database.writeCreationDate(record.readDbDate(0x08));
        dbCommitDate = record.readDbDate(0x0a);
        isDbCommitted = dbCommitDate != 0;
        if (isDbCommitted) {
            database.writeCommitDate(dbCommitDate);
        }
    }
