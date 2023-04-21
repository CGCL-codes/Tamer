    public FileWriter(final Format format, final Properties properties) throws IOException {
        super(format);
        final boolean append = Boolean.valueOf(properties.getProperty("append"));
        final String fileName = properties.getProperty("filename");
        if (fileName == null) {
            throw new NullPointerException("The filename property has not been set");
        }
        final FileOutputStream fileOut = new FileOutputStream(fileName, append);
        fileLock = fileOut.getChannel().tryLock();
        Runtime.getRuntime().addShutdownHook(new Thread() {

            public void run() {
                try {
                    if (fileLock != null) {
                        fileLock.release();
                    }
                } catch (IOException e) {
                }
            }
        });
        final BufferedOutputStream bufferedOut = new BufferedOutputStream(fileOut);
        out = new PrintStream(bufferedOut);
    }
