    private void updateClassModified(InvalidClassException exc) {
        ConsoleDialog.writeError("The " + SAVE_FILE_NAME + " is unreadable " + "by this version of Buttress, your Feeds may be recovered, " + "but your Filters are lost.", exc);
        JOptionPane.showMessageDialog(null, "The " + SAVE_FILE_NAME + " file is unreadable by this " + "version of" + "\nButtress, your Feeds may be recovered, but your Filters" + "\nare lost." + "\n\nIf you have a version of Buttress that the " + SAVE_FILE_NAME + "\nfile worked with, open that version of Buttress, copy out " + "\nyour filters now, and quit it, before you close this version " + "\nof Buttress." + "\n\nWhen Buttress closes, it will overwrite your old save file " + "to " + "\nthe new version.", "Cannot Read Save File", JOptionPane.WARNING_MESSAGE);
        updateClassChanged = true;
    }
