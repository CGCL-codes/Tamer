    public void setBooleanFieldAbs(int absFieldNo, boolean newValue) {
        throw BindingSupportImpl.getInstance().invalidOperation("Not allowed to read/write to a instance marked for deletion");
    }
