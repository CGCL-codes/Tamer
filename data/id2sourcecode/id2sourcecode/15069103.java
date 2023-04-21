    private void reopen() throws IOException {
        channel = raf.getChannel();
        bi = ChunkedMemoryMappedFile.mapFile(channel);
    }
