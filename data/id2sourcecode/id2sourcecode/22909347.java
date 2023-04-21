    public void loadList(@NotNull final File baseDir) {
        try {
            final URL url = IOUtils.getResource(baseDir, FILENAME);
            try {
                final InputStream inputStream = url.openStream();
                try {
                    final Reader reader = new InputStreamReader(inputStream, IOUtils.MAP_ENCODING);
                    try {
                        final BufferedReader stream = new BufferedReader(reader);
                        try {
                            loadList(url, stream);
                        } finally {
                            stream.close();
                        }
                    } finally {
                        reader.close();
                    }
                } finally {
                    inputStream.close();
                }
            } catch (final IOException ex) {
                errorView.addWarning(ErrorViewCategory.AUTOJOIN_FILE_INVALID, url + ": " + ex.getMessage());
            }
        } catch (final IOException ex) {
            errorView.addWarning(ErrorViewCategory.AUTOJOIN_FILE_INVALID, FILENAME + ": " + ex.getMessage());
        }
    }
