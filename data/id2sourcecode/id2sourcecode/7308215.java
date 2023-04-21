    private void export() {
        if (worker != null && !worker.isDone()) {
            return;
        }
        File file = new File(jTextFieldFile.getText());
        if (file.exists()) {
            int answer = JOptionPane.showConfirmDialog(this, MessageFormat.format(bundle.getString("File_{0}_already_exists!\nDo_you_really_want_to_overwrite_it?"), file.getPath()), bundle.getString("File_exists"), JOptionPane.YES_NO_OPTION);
            if (answer != JOptionPane.YES_OPTION) {
                return;
            }
        }
        jButtonExport.setEnabled(false);
        worker = new CSVExportWorker(file, model, jCheckBoxProperties.isSelected());
        worker.getPropertyChangeSupport().addPropertyChangeListener("state", new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if (worker.isDone()) {
                    jButtonExport.setEnabled(true);
                }
            }
        });
        worker.execute();
        simpleStatusBar.addWorker(worker);
    }
