        public void actionPerformed(ActionEvent e) {
            JFileChooser chooser = new JFileChooser(lastSelectionDir);
            chooser.setDialogTitle("Save As");
            ExtensionFileFilter filter = new ExtensionFileFilter();
            filter.addExtension("xml");
            chooser.setFileFilter(filter);
            int result = chooser.showSaveDialog(MainFrame.this);
            if (result == JFileChooser.APPROVE_OPTION) {
                File file = chooser.getSelectedFile();
                boolean proceed = true;
                if (file.exists()) {
                    result = JOptionPane.showConfirmDialog(MainFrame.this, "The selected file already exists, overwrite?", "File exists", JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
                    proceed = (result == JOptionPane.OK_OPTION);
                }
                if (proceed) {
                    setFile(file);
                    saveFile();
                }
            }
        }
