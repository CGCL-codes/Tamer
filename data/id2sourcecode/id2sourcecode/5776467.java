    @NotNull
    protected static Map<String, String> loadAnimTree(@NotNull final URL url) throws IOException {
        final Map<String, String> animations;
        final InputStream inputStream = url.openStream();
        try {
            final Reader reader = new InputStreamReader(inputStream, IOUtils.MAP_ENCODING);
            try {
                final BufferedReader bufferedReader = new BufferedReader(reader);
                try {
                    animations = loadAnimTree(bufferedReader);
                } finally {
                    bufferedReader.close();
                }
            } finally {
                reader.close();
            }
        } finally {
            inputStream.close();
        }
        if (log.isInfoEnabled()) {
            log.info("Loaded " + animations.size() + " animations from '" + url + "'.");
        }
        return animations;
    }
