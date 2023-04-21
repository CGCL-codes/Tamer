    private void showAddThemeEntry(AddThemeEntry entry) {
        if (getCurrentStyleTable() == constantsTable) {
            ConstantEditor prompt = new ConstantEditor(null, null, resources);
            if (JOptionPane.showConfirmDialog(this, prompt, "Add", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE) == JOptionPane.OK_OPTION) {
                if (prompt.isValidState()) {
                    getCurrentStyleModel().addKeyValue(prompt.getConstant(), prompt.getValue());
                    resources.setModified();
                }
            }
            return;
        }
        if (JOptionPane.showConfirmDialog(this, entry, "Add", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE) == JOptionPane.OK_OPTION) {
            String uiid = entry.getUIID();
            Hashtable tmp = new Hashtable(themeHash);
            if (uiid == null || uiid.length() == 0) {
                entry.updateThemeHashtable(tmp);
            } else {
                for (Object k : getCurrentStyleModel().keys) {
                    if (uiid.equals(k)) {
                        int res = JOptionPane.showConfirmDialog(this, "The property " + uiid + " is already defined.\nDo you want to overwrite it?", "Selector Already Defined", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
                        if (res != JOptionPane.OK_OPTION) {
                            return;
                        }
                    }
                }
                entry.updateThemeHashtable(tmp);
            }
            resources.setTheme(themeName, tmp);
            themeHash = resources.getTheme(themeName);
            refreshTheme(themeHash);
            initTableModel(theme, null);
            initTableModel(selectedStyles, "sel#");
            initTableModel(pressedStyles, "press#");
            initTableModel(disabledStyles, "dis#");
        } else {
            refreshTheme(themeHash);
        }
    }
