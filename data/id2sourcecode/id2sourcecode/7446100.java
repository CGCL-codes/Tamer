    public static void load(@NotNull final URL url, @NotNull final SmoothFaces smoothFaces, @NotNull final ErrorView errorView) {
        final ErrorViewCollector errorViewCollector = new ErrorViewCollector(errorView, url);
        try {
            final InputStream inputStream = url.openStream();
            try {
                final Reader reader = new InputStreamReader(inputStream, IOUtils.MAP_ENCODING);
                try {
                    final Reader bufferedReader = new BufferedReader(reader);
                    try {
                        load(url.toString(), bufferedReader, smoothFaces, errorViewCollector);
                    } finally {
                        bufferedReader.close();
                    }
                } finally {
                    reader.close();
                }
            } finally {
                inputStream.close();
            }
        } catch (final IOException ex) {
            errorViewCollector.addWarning(ErrorViewCategory.SMOOTH_FILE_INVALID, ex.getMessage());
        }
    }
