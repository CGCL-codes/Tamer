    public static synchronized IndexReader getReader(boolean readOnly) throws CorruptIndexException, IOException {
        if (reader == null) {
            log.finer("Opening new reader in " + (readOnly ? "read only" : "read/write") + " mode");
            reader = IndexReader.open(settings.getIndexDir(), readOnly);
        } else {
            IndexReader newReader = reader.reopen();
            if (newReader != reader) {
                reader.close();
            }
            reader = newReader;
        }
        return reader;
    }
