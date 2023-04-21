    public long getSize() throws IOException {
        if (fileSize == -1) {
            fileSize = getFileInputStream().getChannel().size();
        }
        return fileSize;
    }
