    @Override
    public void actionPerformed(ActionEvent arg0) {
        EID eid = app.getEid();
        if (eid == null) {
            JOptionPane.showMessageDialog(app, "Nothing to save.");
            return;
        }
        int returnVal = fc.showSaveDialog(app);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
            if (file.exists()) {
                int confirm = JOptionPane.showConfirmDialog(app, "File already exist. Overwrite?");
                if (confirm != JOptionPane.YES_OPTION) return;
            }
            Export.pdf(eid, file.getPath());
            Logging.info("PDF saved to " + file.getPath());
        }
    }
