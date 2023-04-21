    public static void copy(final InputStream input, final OutputStream output, final long dataLength, final byte[] copyBuffer) throws IOException {
        int readBytes = 0;
        long dataLeftToWrite = dataLength;
        int copyCount;
        int bufferSize = copyBuffer.length;
        while (dataLeftToWrite > 0) {
            copyCount = (dataLeftToWrite >= bufferSize) ? bufferSize : (int) dataLeftToWrite;
            readBytes = input.read(copyBuffer, 0, copyCount);
            if (readBytes < 0) throw new IOException("Error occurred while reading data from input stream! Got unexpected end of file!");
            output.write(copyBuffer, 0, readBytes);
            dataLeftToWrite -= readBytes;
        }
        output.flush();
    }
