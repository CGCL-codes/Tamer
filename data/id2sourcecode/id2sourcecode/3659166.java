    public static File selectFile(Component parent, File file, String approveButtonText, boolean forSave) {
        File ret = null;
        final JFileChooser fc = new JFileChooser();
        if (file != null) {
            fc.setSelectedFile(file);
        }
        if (fc.showDialog(parent, approveButtonText) == JFileChooser.APPROVE_OPTION) {
            file = fc.getSelectedFile();
            if (forSave && file.exists()) {
                int value = JOptionPane.showConfirmDialog(parent, file + " already exists.\n" + "Do you want to over write it?", "Please Confirm", JOptionPane.YES_NO_OPTION);
                if (value == JOptionPane.YES_OPTION) {
                    ret = file;
                }
            } else {
                ret = file;
            }
        }
        return ret;
    }
