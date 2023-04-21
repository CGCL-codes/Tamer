    final String[] getColumnNames(int index) {
        final SQLTableModelColumn col = this.model.getLinesSource().getParent().getColumn(index);
        final String[] res = new String[] { col.getName(), col.getToolTip(), col.getIdentifier() };
        assert res[res.length - 1] != null : "Null identifier for " + col;
        for (int i = res.length - 2; i >= 0; i--) {
            if (res[i] == null) res[i] = res[i + 1];
        }
        return res;
    }
