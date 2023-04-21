    public void setObjectFieldUnresolvedAbs(int field, Object newValue) {
        throw BindingSupportImpl.getInstance().invalidOperation("Not allowed to read/write to a instance marked for deletion");
    }
