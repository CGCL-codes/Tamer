    public BinVariableSearchFilter(final boolean readAccess, final boolean writeAccess, final boolean showDuplicateLines, final boolean goToSingleUsage, boolean runWithDefaultSettings) {
        super(showDuplicateLines, goToSingleUsage, runWithDefaultSettings);
        this.includeReadAccess = readAccess;
        this.includeWriteAccess = writeAccess;
    }
