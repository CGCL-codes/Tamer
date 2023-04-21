    void setupChunks() throws IOException {
        fileChannel = fileInputStream.getChannel();
        fileSize = fileChannel.size();
        chunkSizeBytes = eventSizeBytes * CHUNK_SIZE_EVENTS;
        numChunks = (int) ((fileSize / chunkSizeBytes) + 1);
        log.info("fileSize=" + fileSize + " chunkSizeBytes=" + chunkSizeBytes + " numChunks=" + numChunks);
        mapChunk(0);
    }
