    public static boolean okToWriteFile(Frame parent, String fileName) {
        File f = new File(fileName);
        if (f.isDirectory()) {
            JOptionPane.showMessageDialog(parent, fileName + " is a directory.", "Error!", JOptionPane.ERROR_MESSAGE);
            return false;
        } else if (f.exists()) {
            int saveAnswer = JOptionPane.showConfirmDialog(parent, "File " + fileName + " already exists.\nDo you want to overwrite?", "Question", JOptionPane.OK_CANCEL_OPTION);
            return (saveAnswer == JOptionPane.OK_OPTION);
        }
        return true;
    }
