    private void writeDataBuffer(File file) throws IOException {
        int readBytes = 0;
        byte dataBuffer[] = new byte[DATA_BUFFER_SIZE];
        InputStream fis = new BufferedInputStream(new FileInputStream(file));
        while ((readBytes = fis.read(dataBuffer)) != -1) {
            zis.write(dataBuffer, 0, readBytes);
        }
        zis.flush();
        fis.close();
    }
