    public Object getValueAt(int row, int col) {
        Channel ch = updateManager.getChannelByOrder(row);
        switch(col) {
            case COL_NAME:
                return ch.getName();
            case COL_ALIAS:
                return ch.getAlias();
            case COL_ACTIVE:
                return new Boolean(ch.isActive());
        }
        return null;
    }
