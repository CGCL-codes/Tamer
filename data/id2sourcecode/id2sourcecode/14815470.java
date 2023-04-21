    public void setValueAt(Object value, int row, int col) {
        Channel ch = updateManager.getChannelByOrder(row);
        switch(col) {
            case COL_NAME:
                ch.setName((String) value);
                return;
            case COL_ALIAS:
                ch.setAlias((String) value);
                return;
            case COL_ACTIVE:
                ch.setActive(((Boolean) value).booleanValue());
                return;
        }
    }
