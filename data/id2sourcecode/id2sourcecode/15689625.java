    public void initFromModel(DbLoaderHelper helper, String tableName) {
        this.helper = helper;
        this.message.setText("DataMap already contains table '" + tableName + "'. Overwrite?");
        validate();
        pack();
    }
