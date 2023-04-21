    public void actionPerformed(ActionEvent _e) {
        int option = fileChooser.showSaveDialog(getTool());
        if (option != JFileChooser.APPROVE_OPTION) {
            return;
        }
        ExtensionFileFilter.fixChooserSelection(fileChooser);
        final File f = fileChooser.getSelectedFile().getAbsoluteFile();
        if (f.exists()) {
            option = JOptionPane.showConfirmDialog(getTool(), "File already exists. Overwrite?\n" + f, "Confirmation", JOptionPane.YES_NO_OPTION);
            if (option != JFileChooser.APPROVE_OPTION) {
                return;
            }
        }
        final ProgressMonitorEx pm = new ProgressMonitorEx(getTool(), "Generating Traceability Matrix", null, 0, 5);
        pm.setMillisToDecideToPopup(0);
        pm.setMillisToPopup(0);
        pm.incrementProgressLater();
        Thread t = new Thread() {

            @Override
            public void run() {
                try {
                    generateTraceabilityMatrix(f, pm);
                } finally {
                    SwingUtilities.invokeLater(new Runnable() {

                        public void run() {
                            pm.close();
                        }
                    });
                }
            }
        };
        t.setPriority(Thread.MIN_PRIORITY);
        t.start();
    }
