    private boolean outfileIsValid(String outfile) {
        boolean goodFile;
        outfile.trim();
        File outFile = new File(outfile);
        if (outFile.isDirectory()) {
            JOptionPane.showMessageDialog(parentFrame, "The output file path is a directory.  Please add a file name.", "Directory Found", JOptionPane.OK_OPTION);
            return false;
        } else if (outFile.exists()) {
            int result = JOptionPane.showConfirmDialog(parentFrame, "The file " + outFile.getPath() + " already exists.  Overwrite this file?", "Overwrite File?", JOptionPane.YES_NO_OPTION);
            if (result == JOptionPane.YES_OPTION) {
                outFile.delete();
                return true;
            } else return false;
        }
        return true;
    }
