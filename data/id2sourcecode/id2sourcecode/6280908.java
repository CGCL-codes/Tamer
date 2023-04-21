    private void saveMap() {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Select filename for the map");
        chooser.setFileFilter(new DialogFilter());
        int r = JOptionPane.NO_OPTION;
        while (r == JOptionPane.NO_OPTION) {
            int returnVal = chooser.showSaveDialog(this);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File f = chooser.getSelectedFile();
                String filnavn = f.getAbsolutePath();
                if (!filnavn.endsWith(".map")) {
                    f = new File(filnavn += ".map");
                }
                if (f.exists()) {
                    r = JOptionPane.showConfirmDialog(this, "The file you chose already exists. Overwrite?", "File exists", JOptionPane.YES_NO_CANCEL_OPTION);
                    if (r == JOptionPane.YES_OPTION) {
                        serialize(f);
                    }
                } else {
                    serialize(f);
                    r = JOptionPane.YES_OPTION;
                }
            } else r = JOptionPane.YES_OPTION;
        }
    }
