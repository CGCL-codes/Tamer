    public static File showSaveFileDialog(JFrame frame, FileFilter filter, String extension) {
        return showSaveFileDialog(frame, filter, extension, "Save File...", "OK", "Cancel", "Replace", "Cannot write to selected file", "Error", "The selected file already exists.\nDo you want to overwrite it?", "Overwrite File?");
    }
