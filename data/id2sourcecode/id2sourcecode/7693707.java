    protected void getFile(File file) throws IOException, ProtocolException, DigestException {
        logger.fine("Requesting file: " + file);
        protocol.write(protocol.new FileReq(file.getName()), channel);
        FileStart fileResp = protocol.read(channel, Protocol.FileStart.class);
        File tmpFile = new File(envDir, file.getName() + FileManager.TMP_SUFFIX);
        if (tmpFile.exists()) {
            boolean deleted = tmpFile.delete();
            if (!deleted) {
                throw EnvironmentFailureException.unexpectedState("Could not delete file: " + tmpFile);
            }
        }
        ByteBuffer buffer = ByteBuffer.allocate(LogFileFeeder.TRANSFER_BYTES);
        messageDigest.reset();
        final FileOutputStream fileStream = new FileOutputStream(tmpFile);
        try {
            for (long bytes = fileResp.getFileLength(); bytes > 0; ) {
                int readSize = (int) Math.min(LogFileFeeder.TRANSFER_BYTES, bytes);
                buffer.rewind();
                buffer.limit(readSize);
                int actualBytes = channel.read(buffer);
                if (actualBytes == -1) {
                    throw new IOException("Premature EOF. Was expecting:" + readSize);
                }
                bytes -= actualBytes;
                fileStream.write(buffer.array(), 0, actualBytes);
                messageDigest.update(buffer.array(), 0, actualBytes);
            }
            logger.info(String.format("Fetched log file: %s, size: %,d bytes", file.getName(), fileResp.getFileLength()));
        } finally {
            fileStream.close();
        }
        FileEnd fileEnd = protocol.read(channel, Protocol.FileEnd.class);
        if (!Arrays.equals(messageDigest.digest(), fileEnd.getDigestSHA1())) {
            logger.warning("digest mismatch on file: " + file);
            throw new DigestException();
        }
        if (file.exists()) {
            disposeObsoleteFiles(file);
        }
        logger.fine("Renamed " + tmpFile + " to " + file);
        boolean renamed = tmpFile.renameTo(file);
        if (!renamed) {
            throw EnvironmentFailureException.unexpectedState("Rename from: " + tmpFile + " to " + file + " failed");
        }
        if (!file.setLastModified(fileResp.getLastModifiedTime())) {
            throw EnvironmentFailureException.unexpectedState("File.setlastModifiedTime() for:" + file + " and time " + new Date(fileResp.getLastModifiedTime()) + " failed.");
        }
    }
