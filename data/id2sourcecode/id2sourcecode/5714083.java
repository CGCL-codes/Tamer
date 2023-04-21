    private List<ConfigFileEntry> createEntries(String filename) {
        List<ConfigFileEntry> data = new ArrayList<ConfigFileEntry>();
        try {
            InputStream stream = null;
            URL url = getClass().getClassLoader().getResource(filename);
            if (url != null) {
                stream = url.openStream();
            }
            if (stream == null) {
                File file = new File(filename);
                if (file.exists()) {
                    stream = new FileInputStream(filename);
                } else {
                    LOG.warn("Unable to find file at " + file.getAbsolutePath());
                }
            }
            if (stream != null) {
                readStream(stream, data);
            } else {
                LOG.error("No file found:" + filename);
            }
        } catch (IOException e) {
            LOG.error("Unable to load properties file " + new File(filename).getAbsolutePath(), e);
        }
        return data;
    }
