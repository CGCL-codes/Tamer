    public void setBasisFile(File file) throws IOException {
        super.setBasisFile(file);
        mappedFile = null;
        if (file != null && file.length() >= mapLimit) {
            mappedFile = basisFile.getChannel().map(FileChannel.MapMode.READ_ONLY, 0, Math.min(mapSize, file.length()));
            mapOffset = 0;
        }
    }
