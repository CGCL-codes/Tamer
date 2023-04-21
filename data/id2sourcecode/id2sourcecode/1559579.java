    public void export(Article article, boolean overwriteFile) throws FileAlreadyExistsException {
        Origin origin = OriginsBO.getInstance().findOrigin(article.getOriginDescription());
        article.setOrigin(origin);
        articleDAO.export(article, overwriteFile);
    }
