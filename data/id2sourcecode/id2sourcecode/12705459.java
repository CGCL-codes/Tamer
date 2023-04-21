    public Object getObjectField(int stateFieldNo, PersistenceCapable owningPC, PersistenceContext sm, OID oid) {
        throw BindingSupportImpl.getInstance().invalidOperation("Not allowed to read/write to a instance marked for deletion");
    }
