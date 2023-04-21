    public void setObjectFieldAbs(int absFieldNo, Object newValue) {
        throw BindingSupportImpl.getInstance().invalidOperation("Not allowed to read/write to a instance marked for deletion");
    }
