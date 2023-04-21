    protected File createTemporaryFile(String name, ByteBuffer data) throws IOException {
        String validFileName = validateFileName(name);
        File temporaryFile = TemporaryFolder.getFolder(getApplicationName()).subFolder("dnd").createFile(validFileName);
        FileChannel fileChannel = new FileOutputStream(temporaryFile).getChannel();
        try {
            fileChannel.write(data);
        } finally {
            fileChannel.close();
        }
        return temporaryFile;
    }
