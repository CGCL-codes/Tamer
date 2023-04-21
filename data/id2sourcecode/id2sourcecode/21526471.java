    @Override
    public void actionPerformed(ActionEvent arg0) {
        DataOutputStream dos;
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
                dos = new DataOutputStream(new FileOutputStream(file));
                try {
                    dos.writeUTF(eid.getData().toString());
                    dos.writeUTF(eid.getAddress().toString());
                    dos.writeInt(eid.getPicture().length);
                    dos.write(eid.getPicture());
                    dos.flush();
                    Logging.info("Dump saved to " + file.getPath());
                } finally {
                    dos.close();
                }
            } catch (IOException e) {
                Logging.severe(e);
            }
        }
    }
