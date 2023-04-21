    private void readStaticHeader() throws SQLException {
        long length = file.length();
        database.notifyFileSize(length);
        file.seek(FileStore.HEADER_LENGTH);
        Data page = Data.create(database, new byte[PAGE_SIZE_MIN - FileStore.HEADER_LENGTH]);
        file.readFully(page.getBytes(), 0, PAGE_SIZE_MIN - FileStore.HEADER_LENGTH);
        setPageSize(page.readInt());
        int writeVersion = page.readByte();
        int readVersion = page.readByte();
        if (readVersion != 0) {
            throw Message.getSQLException(ErrorCode.FILE_VERSION_ERROR_1, fileName);
        }
        if (writeVersion != 0) {
            close();
            database.setReadOnly(true);
            accessMode = "r";
            file = database.openFile(fileName, accessMode, true);
        }
    }
