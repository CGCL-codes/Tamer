    public static File createTempFile(InputSource input) throws IOException {
        File tempFile = File.createTempFile("tempwriter", ".tmp");
        FileOutputStream outStream = new FileOutputStream(tempFile);
        InputStream inStream = input.getByteStream();
        byte[] readBuff = new byte[READ_BUFFER_SIZE];
        int bytesRead = inStream.read(readBuff);
        while (bytesRead > 0) {
            outStream.write(readBuff, 0, bytesRead);
            bytesRead = inStream.read(readBuff);
        }
        return tempFile;
    }
