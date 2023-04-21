    @Override
    protected void generateOutput(Article article, boolean overwriteFile) throws FileAlreadyExistsException {
        ArticleBO.getInstance().export(article, overwriteFile);
    }
