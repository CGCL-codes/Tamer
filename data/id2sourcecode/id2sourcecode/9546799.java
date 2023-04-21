    private void readStreamContent() throws IOException {
        int readByte;
        while ((readByte = inputStream.read()) != -1) streamBuffer.write(readByte);
    }
