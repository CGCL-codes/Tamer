    protected boolean writeBundle(IExtractorInputReader reader, MarkerSetHolder holder, File file) {
        SpantusBundle bundle = new SpantusBundle();
        bundle.setReader(reader);
        bundle.setHolder(holder);
        WorkServiceFactory.createBundleDao().write(bundle, file);
        return true;
    }
