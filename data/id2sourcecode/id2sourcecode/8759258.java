    public synchronized void export() {
        if (worker != null && !worker.isDone()) {
            return;
        }
        File file = new File(jTextFieldFile.getText());
        if (file.exists()) {
            int answer = JOptionPane.showConfirmDialog(SwingUtilities.getWindowAncestor(this), MessageFormat.format(bundle.getString("File_{0}_already_exists!\nDo_you_really_want_to_overwrite_it?"), file.getPath()), bundle.getString("File_exists"), JOptionPane.YES_NO_OPTION);
            if (answer != JOptionPane.YES_OPTION) {
                return;
            }
        }
        actionExport.setEnabled(false);
        Date tinit = null;
        Date tfinal = null;
        if (jCheckBoxTime.isSelected()) {
            tinit = (Date) jSpinnerInitTime.getValue();
            tfinal = (Date) jSpinnerFinalTime.getValue();
        }
        VariableTableModel model = (VariableTableModel) jTableVars.getModel();
        worker = new CSVExportWorker(file, tinit, tfinal, model.getSelectedVars(), jCheckBoxHeader.isSelected());
        worker.getPropertyChangeSupport().addPropertyChangeListener("state", workerListener);
        simpleStatusBar.addWorker(worker);
        worker.execute();
    }
