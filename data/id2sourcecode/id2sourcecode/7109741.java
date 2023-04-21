    private boolean outfileIsValid(String outfile, String name) {
        boolean goodFile;
        outfile.trim();
        if (name.indexOf(File.separator) != -1) {
            JOptionPane.showMessageDialog(this, "Error! Invalid Filename");
            return false;
        }
        File outFile = new File(outfile);
        if (outFile.isDirectory()) {
            JOptionPane.showMessageDialog(this, "The output file path is a directory.  Please add a file name.", "Directory Found", JOptionPane.OK_OPTION);
            return false;
        } else if (outFile.exists()) {
            int result = JOptionPane.showConfirmDialog(this, "The file " + outFile.getPath() + " already exists.  Overwrite this file?", "Overwrite File?", JOptionPane.YES_NO_OPTION);
            if (result == JOptionPane.YES_OPTION) {
                outFile.delete();
                return true;
            } else return false;
        } else if (manager.fileExists(outFile.getParentFile().getName() + File.separator + outFile.getName())) {
            JOptionPane.showMessageDialog(this, "Error! This Filename Has Already Been Created In The Task Manager.\nPlease Select Another Name");
            return false;
        }
        return true;
    }
