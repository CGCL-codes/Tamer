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
            try {
                IO.save(file.toString(), eid.getPicture());
                Logging.info("Picture saved to: " + file.getPath());
            } catch (IOException e) {
                Logging.severe(e);
            }
        }
    }
