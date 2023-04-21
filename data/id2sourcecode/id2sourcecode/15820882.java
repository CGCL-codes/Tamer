    public void performDelete(Triple t) {
        if (!writeable) throw new DeleteDeniedException("graph is readonly");
        Statement stri = jena2sesame(t);
        try {
            repository.getConnection().remove(stri.getSubject(), stri.getPredicate(), stri.getObject());
        } catch (RepositoryException e) {
            throw new RuntimeException(e);
        }
    }
