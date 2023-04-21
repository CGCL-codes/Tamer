    public void loadDataset(URL url, URI context) throws RepositoryException {
        try {
            Long since = lastModified.get(url);
            URLConnection urlCon = url.openConnection();
            if (since != null) {
                urlCon.setIfModifiedSince(since);
            }
            if (since == null || since < urlCon.getLastModified()) {
                load(url, urlCon, context);
            }
        } catch (RDFParseException e) {
            throw new RepositoryException(e);
        } catch (IOException e) {
            throw new RepositoryException(e);
        }
    }
