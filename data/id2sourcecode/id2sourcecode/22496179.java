    public void doSave() {
        if (savefile == null) {
            String dir = System.getProperty("user.dir");
            JFileChooser jfc = new JFileChooser(dir);
            jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
            int answer = 0;
            if (isShowing()) answer = jfc.showSaveDialog(this); else answer = jfc.showSaveDialog(null);
            if (answer == JFileChooser.APPROVE_OPTION) {
                File file = jfc.getSelectedFile();
                if (infile != null) {
                    if (file.toString().equals(infile) && !overwrite) {
                        if (isShowing()) JOptionPane.showMessageDialog(this, "File already exists, cannot overwrite.", "Error!", JOptionPane.ERROR_MESSAGE); else JOptionPane.showMessageDialog(null, "File already exists, cannot overwrite.", "Error!", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }
                savefile = file.toString();
            }
        }
        if (savefile != null && savefile.length() != 0) {
            writeTabFile(savefile);
        }
    }
