    public static File showOpenDialog(JFrame frame, FileFilter filter) {
        return showOpenDialog(frame, filter, "Open File...", "OK", "Cannot write to selected file", "Cannot read from selected file", "Error");
    }
