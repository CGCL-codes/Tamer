    public BinVariableSearchFilter(final boolean readAccess, final boolean writeAccess, final boolean showDuplicateLines, final boolean goToSingleUsage, boolean runWithDefaultSettings, final boolean searchSubtypes) {
        super(showDuplicateLines, goToSingleUsage, runWithDefaultSettings, searchSubtypes, true);
        this.includeReadAccess = readAccess;
        this.includeWriteAccess = writeAccess;
    }
