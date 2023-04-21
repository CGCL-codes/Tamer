    public void copyFile(byte[] hash, File destinationFile) throws IOException {
        int read;
        byte[] buffer = new byte[512 * 1024];
        FileOutputStream outputStream;
        ReadableFile file = getFile(hash);
        if (file == null) throw new FileNotFoundException("File not found");
        outputStream = new FileOutputStream(destinationFile);
        while ((read = file.read(buffer)) > 0) {
            outputStream.write(buffer, 0, read);
        }
        outputStream.close();
        file.close();
    }
