    private void merge() {
        try {
            mergeFile = txtMFile.getText();
            if (!mergeFile.endsWith(".jfs000")) {
                File f = new File(mergeFile + ".jfs000");
                if (!f.exists()) {
                    throw new Exception("Specify a file of \".jfs000\" type!");
                }
            } else {
                mergeFile = mergeFile.substring(0, mergeFile.length() - 7);
            }
            File f = new File(mergeFile);
            if (f.exists()) {
                if (JOptionPane.showConfirmDialog(dlg, "File " + mergeFile + " already exist. Overwrite it?", "Warning", JOptionPane.YES_NO_OPTION) == JOptionPane.NO_OPTION) {
                    return;
                }
            }
            merge(mergeFile, mergeFile);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
    }
