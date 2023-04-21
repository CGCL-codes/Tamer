    public void addFetchGroupStatesToDCS(FetchGroup fg, DetachStateContainer dcs, VersantPersistenceManagerImp pm, OID oid) {
        throw BindingSupportImpl.getInstance().invalidOperation("Not allowed to read/write to a instance marked for deletion");
    }
