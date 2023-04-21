    public AnalyzedContext getAnalyzedExistingData(final Context context, final File savedConcordance) {
        log.entering("Persistance", "getAnalyzedExistingData", savedConcordance.getName());
        AnalyzedContext analyzed = null;
        try {
            analyzed = new AnalyzedContextByXpath(savedConcordance);
        } catch (Exception e) {
            log.log(Level.SEVERE, "Failed to read from file: " + savedConcordance.getName() + ". Re-write file.", e);
            analyzed = buildAnalyzedNewData(context, savedConcordance);
        }
        log.exiting("Persistance", "getAnalyzedExistingData", "Map of size " + analyzed.getSize());
        return analyzed;
    }
