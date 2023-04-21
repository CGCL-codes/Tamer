    protected void updateBytesRead() {
        long bytesRead = getReadBytes();
        if (bytesRead >= nextBytesRead) {
            BytesRead sbr = new BytesRead((int) bytesRead);
            getChannel((byte) 2).write(sbr);
            log.info(sbr);
            nextBytesRead += bytesReadInterval;
        }
    }
