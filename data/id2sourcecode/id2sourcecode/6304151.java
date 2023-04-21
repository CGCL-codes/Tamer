    public static void send(Connection conn, File storagePath, String filename) throws EncryptionException, IOException {
        CommandInterface.sendCommand(Command.SEND_FILE, conn.getActiveOut());
        filename = filename.replace(File.pathSeparatorChar, '/');
        DataInterface.sendNextString(filename, conn.getActiveOut());
        File file = new File(storagePath, filename);
        InputStream fileIn = new FileInputStream(file);
        try {
            conn.getActiveOut().writeLong(file.length());
            byte[] buf = new byte[DataConstants.FILE_BUFFER_SIZE];
            int readLength;
            while ((readLength = fileIn.read(buf)) > 0) {
                conn.getActiveOut().write(buf, 0, readLength);
            }
        } finally {
            fileIn.close();
        }
        conn.flushOut();
    }
