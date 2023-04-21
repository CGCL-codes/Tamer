    public final void process(ArticleForm articleForm, boolean overwriteFile) throws ValidationException, FileAlreadyExistsException {
        validate(articleForm);
        Article article = preProcess(articleForm);
        generateOutput(article, overwriteFile);
    }
