    public void storeEntrySet(File entryXml, String providerName) {
        if (log.isDebugEnabled()) {
            log.debug("Adding entry: " + entryXml + " (Provider: " + providerName + ")");
        }
        ImexRepositoryContext context = ImexRepositoryContext.getInstance();
        Provider provider = context.getImexServiceProvider().getProviderService().findByName(providerName);
        if (provider == null) {
            throw new RepoEntityNotFoundException("No provider found with name: " + providerName);
        }
        String entryName = entryXml.getName();
        String name = fileDate(entryName);
        RepoEntrySet repoEntrySet = new RepoEntrySet(provider, name);
        RepositoryHelper repoHelper = new RepositoryHelper(this);
        File newFile = repoHelper.getEntrySetFile(repoEntrySet);
        if (log.isDebugEnabled()) {
            log.debug("Copying file to: " + newFile);
        }
        try {
            FileUtils.copyFile(entryXml, newFile);
        } catch (IOException e) {
            throw new RepositoryException(e);
        }
        beginTransaction();
        context.getImexServiceProvider().getRepoEntrySetService().saveRepoEntrySet(repoEntrySet);
        commitTransaction();
        beginTransaction();
        EntrySetSplitter splitter = new DefaultEntrySetSplitter();
        List<RepoEntry> splittedEntries = null;
        try {
            splittedEntries = splitter.splitRepoEntrySet(repoEntrySet);
        } catch (IOException e) {
            throw new RepositoryException(e);
        }
        commitTransaction();
        EntryEnricher enricher = new DefaultEntryEnricher();
        for (RepoEntry repoEntry : splittedEntries) {
            if (repoEntry.isValid()) {
                beginTransaction();
                try {
                    enricher.enrichEntry(repoEntry);
                } catch (IOException e) {
                    throw new RepositoryException(e);
                }
                commitTransaction();
            }
        }
    }
