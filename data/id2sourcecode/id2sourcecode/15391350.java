    public static File showSmartOpenDialog(JFrame frame, File startLocation, FileFilter filter) {
        return showSmartOpenDialog(frame, startLocation, filter, "Open File...", "OK", "Cannot write to selected file", "Cannot read from selected file", "Error");
    }
