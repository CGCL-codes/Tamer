    private void savebuttonActionPerformed(java.awt.event.ActionEvent evt) {
        String istr = mapival.getText();
        String jstr = mapjval.getText();
        int i, j;
        try {
            i = Integer.parseInt(istr);
            j = Integer.parseInt(jstr);
            if (i < 0 || j < 0) {
                JOptionPane.showMessageDialog(this, "i and j must be postive integers!", "Couldn't Save", JOptionPane.ERROR_MESSAGE);
                this.setVisible(false);
                this.dispose();
                return;
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "i and j must be postive integers!", "Couldn't Save", JOptionPane.ERROR_MESSAGE);
            this.setVisible(false);
            this.dispose();
            return;
        }
        File dir = new File(foldername.getText());
        if (!dir.isDirectory()) {
            JOptionPane.showMessageDialog(this, "Folder must be a valid directory!", "Couldn't Save", JOptionPane.ERROR_MESSAGE);
            this.setVisible(false);
            this.dispose();
            return;
        }
        File file = new File(foldername.getText() + Main.getMapString(i, j));
        if (file.isFile()) {
            int o = JOptionPane.showConfirmDialog(this, "Warning! There is already a map with that name, Continue?", "Overwrite?", JOptionPane.NO_OPTION, JOptionPane.WARNING_MESSAGE);
            EditorFrame.print("Confirm dialog returned: " + o);
            if (o == 2 || o == 1) {
                this.setVisible(false);
                this.dispose();
                return;
            }
        }
        try {
            if (Map.saveMap(map, foldername.getText() + Main.getMapString(i, j))) JOptionPane.showMessageDialog(this, "Save of " + Main.getMapString(i, j) + " was sucessful!", jstr, JOptionPane.INFORMATION_MESSAGE); else throw new IOException();
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Encountered error when trying to write map file!", "Couldn't Save", JOptionPane.ERROR_MESSAGE);
        }
        this.setVisible(false);
        this.dispose();
    }
