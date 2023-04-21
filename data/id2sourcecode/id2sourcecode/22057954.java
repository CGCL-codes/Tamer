    protected URLConnection openConnection(URL url) throws IOException {
        return new RepositoryConnection(RepositoryUtil.getThreadRepositoryContext(), url);
    }
