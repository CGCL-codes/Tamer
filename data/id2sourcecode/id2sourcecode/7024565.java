    public java.nio.channels.ReadableByteChannel getChannel(ProgressMonitor monitor) throws FileNotFoundException, IOException {
        InputStream in = getInputStream(monitor);
        return Channels.newChannel(in);
    }
