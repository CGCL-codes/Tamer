    @Override
    public ReadableByteChannel getChannel(ProgressMonitor monitor) throws FileNotFoundException, IOException {
        return ((FileInputStream) getInputStream(monitor)).getChannel();
    }
