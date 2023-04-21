    private void newQuery(String sql) {
        boolean okay = true;
        String newQuery = JOptionPane.showInputDialog(this, "Enter Query Name:", "New Query", JOptionPane.PLAIN_MESSAGE);
        if (database.getQuerySql(newQuery) != null) okay = JOptionPane.showConfirmDialog(this, newQuery + " already exists.  Overwrite it?", "New Query", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE) == JOptionPane.YES_OPTION;
        if (newQuery != null && okay) {
            database.saveQuery(newQuery, sql);
            queryCombo.setModel(new DefaultComboBoxModel(database.getAllQueries().toArray()));
            sqlField.setText(sql);
            queryCombo.setSelectedItem(newQuery);
            if (parent != null) parent.saveRequested(this);
            saveButton.setEnabled(false);
        }
    }
