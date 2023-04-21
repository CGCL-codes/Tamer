    @Override
    public void actionPerformed(ActionEvent arg0) {
        BufferedWriter osw;
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
                osw = new BufferedWriter(new FileWriter(file));
                try {
                    osw.append(eid.getData().toString());
                    osw.append(eid.getAddress().toString());
                    osw.flush();
                    Logging.info("Data saved to: " + file.getPath());
                } finally {
                    osw.close();
                }
            } catch (IOException e) {
                Logging.severe(e);
            }
        }
    }
