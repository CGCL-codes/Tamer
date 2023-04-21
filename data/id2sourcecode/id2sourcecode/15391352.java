    public static File showSaveFileDialog(JFrame frame, File startLocation, FileFilter filter, String extension) {
        return showSaveFileDialog(frame, startLocation, filter, extension, "Save File...", "OK", "Cancel", "Replace", "Cannot write to selected file", "The selected file already exists.\nDo you want to overwrite it?", "Error");
    }
