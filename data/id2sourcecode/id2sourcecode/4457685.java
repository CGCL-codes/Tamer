    public void executeJob() throws JobException {
        try {
            Property indexProperty = Property.getProperty("TextIndexPath");
            LuceneIndex index = new LuceneIndex(indexProperty.getValue());
            index.optimise();
        } catch (IOException e) {
            throw new JobException("Unable to read or write the index", e);
        } catch (PersistentModelException e) {
            throw new JobException("Unable to find the directory to put the index into", e);
        }
    }
