    private byte[] sendFileContents(File file, long length) throws IOException {
        final FileInputStream fileStream = new FileInputStream(file);
        messageDigest.reset();
        try {
            ByteBuffer buffer = ByteBuffer.allocate(TRANSFER_BYTES);
            for (long bytes = length; bytes > 0; ) {
                int readSize = (int) Math.min(TRANSFER_BYTES, bytes);
                int readBytes = fileStream.read(buffer.array(), 0, readSize);
                if (readBytes == -1) {
                    throw new IOException("Premature EOF. Was expecting: " + readSize);
                }
                bytes -= readBytes;
                buffer.position(0);
                buffer.limit(readBytes);
                buffer.mark();
                namedChannel.getChannel().write(buffer);
                buffer.reset();
                messageDigest.update(buffer);
            }
            LoggerUtils.fine(logger, feederManager.getEnvImpl(), "Sent file: " + file + " Length: " + length + " bytes");
        } finally {
            fileStream.close();
        }
        return messageDigest.digest();
    }
