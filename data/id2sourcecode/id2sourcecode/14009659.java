    @Override
    public FileChannel getChannel() throws IOException {
        if (inputRepresentation instanceof FileRepresentation) {
            return (FileChannel) inputRepresentation.getChannel();
        }
        return null;
    }
