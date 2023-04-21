    protected void loadArchetypes(@NotNull final ErrorView errorView, @NotNull final List<G> invObjects) {
        archetypeSet.setLoadedFromArchive(true);
        try {
            final int archetypeCount = archetypeSet.getArchetypeCount();
            final URL url = IOUtils.getResource(collectedDirectory, archetypesFile);
            try {
                final InputStream inputStream = url.openStream();
                try {
                    final Reader reader = new InputStreamReader(inputStream, IOUtils.MAP_ENCODING);
                    try {
                        final BufferedReader bufferedReader = new BufferedReader(reader);
                        try {
                            archetypeParser.parseArchetypeFromStream(bufferedReader, null, null, null, "default", "default", "", invObjects, new ErrorViewCollector(errorView, url));
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
                errorView.addWarning(ErrorViewCategory.ARCHETYPES_FILE_INVALID, url + ": " + ex.getMessage());
            }
            if (log.isInfoEnabled()) {
                log.info("Loaded " + (archetypeSet.getArchetypeCount() - archetypeCount) + " archetypes from '" + url + "'.");
            }
        } catch (final IOException ex) {
            errorView.addWarning(ErrorViewCategory.ARCHETYPES_FILE_INVALID, archetypesFile + ": " + ex.getMessage());
        }
    }
