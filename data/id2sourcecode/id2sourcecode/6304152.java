    public static void read(Connection conn, File storagePath) throws MaxLengthExceededException, IOException {
        String filename = DataInterface.receiveNextString(conn.getActiveIn(), DataConstants.MAX_FILE_NAME_LENGTH);
        File file = new File(storagePath, filename);
        if (file.exists()) {
            throw new IOException("File already exists");
        }
        File parentDir = file.getParentFile();
        if (!parentDir.exists()) {
            parentDir.mkdirs();
        }
        OutputStream fileOut = new FileOutputStream(file);
        try {
            long fileSize = conn.getActiveIn().readLong();
            byte[] buf = new byte[DataConstants.FILE_BUFFER_SIZE];
            int readLength;
            int totalReadLength = 0;
            while (true) {
                readLength = buf.length;
                if (readLength > fileSize - totalReadLength) {
                    readLength = (int) (fileSize - totalReadLength);
                }
                readLength = conn.getActiveIn().read(buf, 0, readLength);
                totalReadLength += readLength;
                fileOut.write(buf, 0, readLength);
            }
        } finally {
            fileOut.close();
        }
    }
