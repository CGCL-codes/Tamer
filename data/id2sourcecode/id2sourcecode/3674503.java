    private synchronized void loadMappingFromPropertiesFile(File mapFile) throws IOException {
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(mapFile);
            FileLock lock = fis.getChannel().lock(0, Long.MAX_VALUE, true);
            accessionToGAProps = new Properties();
            accessionToGAProps.load(fis);
            lock.release();
        } finally {
            if (fis != null) {
                fis.close();
            }
        }
    }
