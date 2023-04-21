    public void performAdd(Triple t) {
        if (!writeable) throw new AddDeniedException("graph is readonly");
        Statement stri = jena2sesame(t);
        try {
            repository.getConnection().add(stri.getSubject(), stri.getPredicate(), stri.getObject());
        } catch (RepositoryException e) {
            throw new RuntimeException(e);
        }
    }
