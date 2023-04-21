    public void saveSpreadActionPerformed(ActionEvent e) {
        if (SpreadManager.getSelectedSpread() == null) {
        } else {
            JFileChooser stDatFileChooser = new JFileChooser(SpreadProperties.getProperty("DATDIR"));
            stDatFileChooser.setDialogTitle("Save Spread As");
            int result = stDatFileChooser.showSaveDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                log.info("Save Spread [" + SpreadManager.getSelectedSpread().getSpreadID() + "] To File : " + stDatFileChooser.getSelectedFile());
                SpreadWriter _write = new SpreadWriter(stDatFileChooser.getSelectedFile());
                _write.WriteSpread(SpreadManager.getSelectedSpread());
            }
        }
    }
