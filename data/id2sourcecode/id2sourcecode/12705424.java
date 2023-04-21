    public void copyFieldsForOptimisticLocking(State state, VersantPersistenceManagerImp sm) {
        throw BindingSupportImpl.getInstance().invalidOperation("Not allowed to read/write to a instance marked for deletion");
    }
