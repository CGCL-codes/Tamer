    private void export() throws Exception {
        File file = new File(tfFile.getText());
        if (file.exists()) {
            int r = JOptionPane.showConfirmDialog(this, Messages.getString("File already exist, do you want to overwrite?"), "File already exist", JOptionPane.YES_NO_OPTION);
            if (r != JOptionPane.YES_OPTION) {
                return;
            }
        }
        long start = OdbTime.getCurrentTimeInMs();
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        XMLExporter exporter = new XMLExporter(storageEngine);
        exporter.setExternalLogger(loggerPanel);
        exporter.export(file.getParent(), file.getName());
        disableFields();
        setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        long end = OdbTime.getCurrentTimeInMs();
        loggerPanel.info((end - start) + " ms");
    }
