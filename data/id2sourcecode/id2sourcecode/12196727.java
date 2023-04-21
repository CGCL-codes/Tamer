    public static ConcreteSingleThreadOverwriteConflictsIterativeFileManager getInstance() {
        if (instance == null) instance = new ConcreteSingleThreadOverwriteConflictsIterativeFileManager();
        return instance;
    }
