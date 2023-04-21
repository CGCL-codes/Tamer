    public void writeStreamBody(final MessageHeader header, final InputStream messageBodyInputStream) throws IOException {
        final long dataLength = header.getBodyLength();
        BufferedInputStream bufferedMessageBodyInputStream;
        if (messageBodyInputStream instanceof BufferedInputStream) {
            bufferedMessageBodyInputStream = (BufferedInputStream) messageBodyInputStream;
        } else {
            bufferedMessageBodyInputStream = new BufferedInputStream(messageBodyInputStream);
        }
        if (dataLength > 0) {
            int readBytes = 0;
            long dataLeftToWrite = dataLength;
            int dataTransferCount;
            while (dataLeftToWrite > 0) {
                dataTransferCount = (dataLeftToWrite >= streamBufferSize) ? streamBufferSize : (int) dataLeftToWrite;
                readBytes = bufferedMessageBodyInputStream.read(streamBuffer, 0, dataTransferCount);
                if (readBytes < 0) {
                    throw new IOException("Error occurred while reading data from input stream! Got unexpected end of file! Data left to write: " + dataLeftToWrite + ", data transfer count: " + dataTransferCount + ".");
                }
                this.endPointOutputStream.write(streamBuffer, 0, readBytes);
                dataLeftToWrite -= readBytes;
            }
        }
    }
