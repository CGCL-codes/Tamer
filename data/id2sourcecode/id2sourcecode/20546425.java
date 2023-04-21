    public java.nio.channels.ReadableByteChannel getChannel(ProgressMonitor monitor) throws FileNotFoundException {
        return ((FileInputStream) getInputStream(monitor)).getChannel();
    }
