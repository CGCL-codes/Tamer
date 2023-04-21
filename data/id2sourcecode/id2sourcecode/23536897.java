    private void remapFile(long newOffset) throws IOException {
        long newLen = Math.min(mapSize, 2 * (basisFile.length() - newOffset));
        mapOffset = newOffset - (newLen / 2);
        if (mapOffset < 0) mapOffset = 0;
        mappedFile = basisFile.getChannel().map(FileChannel.MapMode.READ_ONLY, mapOffset, newLen);
    }
