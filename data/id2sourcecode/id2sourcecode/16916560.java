    private boolean saveFileIsValid(String outfile) {
        outfile.trim();
        File outFile = new File(outfile);
        if (outFile.isDirectory()) {
            JOptionPane.showMessageDialog(this, "The output file path is a directory.  Please add a file name.", "Directory Found", JOptionPane.OK_OPTION);
            return false;
        } else if (outFile.exists()) {
            String[] options = new String[2];
            options[0] = UIManager.getString("OptionPane.yesButtonText");
            options[1] = UIManager.getString("OptionPane.noButtonText");
            int result = JOptionPane.showOptionDialog(parent, "The file " + outFile.getPath() + " already exists.  Overwrite this file?", "Overwrite File?", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[1]);
            if (result == JOptionPane.YES_OPTION) {
                outFile.delete();
                return true;
            } else return false;
        } else {
            outFile.getParentFile().mkdirs();
            return true;
        }
    }
