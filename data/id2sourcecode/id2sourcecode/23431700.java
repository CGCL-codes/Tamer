    public void feedRepositoryFromFile(URL url) {
        if (!repositoryEnabled()) throw new RuntimeException("readFromFile() was called when repository is not enabled. Quitting.");
        try {
            repository.getConnection().add(url.openStream(), "", RDFFormat.RDFXML, new URIImpl(url.toString()));
        } catch (Exception e) {
            e.printStackTrace();
            if (e instanceof RuntimeException) throw (RuntimeException) e; else throw new RuntimeException(e);
        }
    }
