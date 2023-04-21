    public AnalyzedContext getAnalyzedExistingData2(final Context context, final File savedConcordance) {
        log.entering("Persistance", "getAnalyzedExistingData", savedConcordance.getName());
        final Reader reader = FileUtils.buildReader(savedConcordance);
        try {
            final Object mapObj = MAP_XSTREAM.fromXML(reader);
            final Map<String, Integer> map = (Map<String, Integer>) mapObj;
            final AnalyzedContext analyzed = new AnalyzedContextByMap(map);
            log.exiting("Persistance", "getAnalyzedExistingData", "Map of size " + analyzed.getSize());
            return analyzed;
        } catch (Exception ex) {
            log.log(Level.SEVERE, "Failed to read from file: " + savedConcordance.getName() + ". Re-write file.", ex);
            try {
                reader.close();
            } catch (IOException e) {
                log.log(Level.SEVERE, null, e);
            }
            final AnalyzedContext analyzed = buildAnalyzedNewData(context, savedConcordance);
            return analyzed;
        } finally {
            try {
                reader.close();
            } catch (IOException ex) {
                log.log(Level.SEVERE, null, ex);
            }
        }
    }
