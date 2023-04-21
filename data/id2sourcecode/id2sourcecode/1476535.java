    public void add_view(String name, String definition) {
        Transaction tran = readwriteTran();
        try {
            if (null != getView(tran, name)) throw new SuException("view: '" + name + "' already exists");
            Data.add_any_record(tran, "views", new Record().add(name).add(definition));
            tran.ck_complete();
        } finally {
            tran.abortIfNotComplete();
        }
    }
