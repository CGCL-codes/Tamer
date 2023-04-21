    public static File getFileCustom(JFileChooser fileChooser) {
        fileChooser.rescanCurrentDirectory();
        int retVal = fileChooser.showDialog(null, null);
        if (retVal != JFileChooser.APPROVE_OPTION) return null;
        File file = fileChooser.getSelectedFile();
        if (fileChooser.getFileFilter() != fileChooser.getAcceptAllFileFilter()) {
            String fileName = file.getAbsolutePath();
            String fileNameLC = fileName.toLowerCase();
            if (!fileNameLC.endsWith(".xml")) {
                fileName = fileName + ".xml";
                file = new File(fileName);
            }
        }
        if (log.isDebugEnabled()) log.debug("Save file: " + file.getPath());
        if (file.exists()) {
            int selectedValue = JOptionPane.showConfirmDialog(null, "File " + file.getName() + " already exists, overwrite it?", "Overwrite file?", JOptionPane.OK_CANCEL_OPTION);
            if (selectedValue != JOptionPane.OK_OPTION) return null;
        }
        return file;
    }
