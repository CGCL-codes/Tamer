    public void setBasisFile(String filename) throws IOException {
        super.setBasisFile(filename);
        mappedFile = null;
        if (basisFile != null && basisFile.length() >= mapLimit) {
            mappedFile = basisFile.getChannel().map(FileChannel.MapMode.READ_ONLY, 0, Math.min(mapSize, basisFile.length()));
            mapOffset = 0;
        }
    }
