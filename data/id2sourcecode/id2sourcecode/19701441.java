    protected void cleanPhpBlock(InlineStringReader reader, InlineStringWriter writer) throws IOException, CleanerException {
        getPhpCleaner().cleanPhpBlock(reader, writer);
    }
