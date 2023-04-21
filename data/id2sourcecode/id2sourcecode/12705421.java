    public boolean fillToStoreState(State stateToStore, PersistenceContext sm, VersantStateManager pcStateMan) {
        throw BindingSupportImpl.getInstance().invalidOperation("Not allowed to read/write to a instance marked for deletion");
    }
