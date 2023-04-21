    public ClassMetaData getClassMetaData(ModelMetaData jmd) {
        throw BindingSupportImpl.getInstance().invalidOperation("Not allowed to read/write to a instance marked for deletion");
    }
