    private void renameQuery() {
        boolean okay = true;
        String oldQuery = (String) queryCombo.getSelectedItem();
        if (oldQuery == null) return;
        String newQuery = (String) JOptionPane.showInputDialog(this, "Enter Query Name:", "Rename Query", JOptionPane.PLAIN_MESSAGE, null, null, oldQuery);
        if (database.getQuerySql(newQuery) != null) okay = JOptionPane.showConfirmDialog(this, newQuery + " already exists.  Overwrite it?", "Rename Query", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE) == JOptionPane.YES_OPTION;
        if (newQuery != null && okay) {
            database.saveQuery(newQuery, database.getQuerySql(oldQuery));
            database.deleteQuery(oldQuery);
            String currentSql = sqlField.getText();
            queryCombo.setModel(new DefaultComboBoxModel(database.getAllQueries().toArray()));
            queryCombo.setSelectedItem(newQuery);
            if (!currentSql.equals(sqlField.getText())) sqlField.setText(currentSql);
            if (parent != null) parent.saveRequested(this);
        }
    }
