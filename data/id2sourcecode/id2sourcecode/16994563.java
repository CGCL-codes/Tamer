    public void setBrowserReadWrite(boolean flag, String tableAlias) {
        tableModel.setTableEditable(flag, tableAlias);
        log.log(this, "Set browser read and write " + flag);
    }
