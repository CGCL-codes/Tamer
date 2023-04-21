    public static InputStream createInputStreamFromFile(File file, Range range) throws IOException {
        final FileInputStream fileInputStream = new FileInputStream(file);
        FileChannel fastaFileChannel = null;
        try {
            fastaFileChannel = fileInputStream.getChannel();
            ByteBuffer buf = ByteBuffer.allocate((int) range.size());
            fastaFileChannel.position((int) range.getStart());
            int bytesRead = fastaFileChannel.read(buf);
            if (bytesRead < 0) {
                throw new IOException("could not read any bytes from file");
            }
            final ByteArrayInputStream inputStream = new ByteArrayInputStream(buf.array());
            return inputStream;
        } finally {
            IOUtil.closeAndIgnoreErrors(fileInputStream, fastaFileChannel);
        }
    }
