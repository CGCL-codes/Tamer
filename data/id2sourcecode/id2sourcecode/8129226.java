    private Context createContext(final URL url) throws IOException {
        assert url != null;
        Properties props = new Properties();
        InputStream input = url.openStream();
        try {
            props.load(input);
        } finally {
            input.close();
        }
        if (log.isDebugEnabled()) {
            dumpProperties(props);
        }
        return new Context(props);
    }
