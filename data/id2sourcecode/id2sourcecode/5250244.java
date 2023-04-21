    public boolean checkOverwrite(java.awt.Component parent) {
        if (getSelectedFile().canRead()) {
            int returnval = JOptionPane.showConfirmDialog(parent, "File " + filename + " already exists. \n Overwrite?", "Warning", javax.swing.JOptionPane.OK_CANCEL_OPTION);
            if (returnval != javax.swing.JOptionPane.OK_OPTION) {
                return false;
            }
        }
        return true;
    }
