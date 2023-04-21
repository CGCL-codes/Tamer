    private ByteArrayOutputStream outputToBoundary() throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        int readedByte = read();
        while (readedByte != -1) {
            output.write(readedByte);
            readedByte = read();
        }
        return output;
    }
