    public ReadableByteChannel getChannel() throws FileNotFoundException, IOException {
        return getChannel(new NullProgressMonitor());
    }
