    private void initComponents() {
        overwriteData_cb = new JCheckBox("Overwrite existing data in mitigation prioritization Excel spreadsheet");
        overwriteData_cb.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent e) {
                if (overwriteData_cb.isSelected()) wizardData.put(KEY_OVERWRITE_DATA, Boolean.TRUE); else wizardData.put(KEY_OVERWRITE_DATA, Boolean.FALSE);
            }
        });
        wizardData.put(KEY_OVERWRITE_DATA, Boolean.FALSE);
        if (!(Boolean) wizardData.get(FileSelectionPanel.KEY_APPENDING_DATA)) {
            overwriteData_cb.setEnabled(true);
        }
    }
