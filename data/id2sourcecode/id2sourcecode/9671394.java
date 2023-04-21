    public static String selectFile(String title, String initialName, Frame owner) {
        FileDialog dir = new FileDialog(owner, title, FileDialog.SAVE);
        dir.setFile(initialName);
        dir.setVisible(true);
        if (dir.getFile() == null) return null;
        String filepath = dir.getDirectory() + "/" + dir.getFile();
        if (FileUtils.exists(filepath)) {
            String question = "<html><p>Target file " + dir.getFile() + " already exists on directory " + dir.getDirectory() + ".</p>" + "<p>Do you want to overwrite the file?</p></html>";
            String[] options = new String[] { "Yes, overwrite", "No" };
            if (showConfirmDialog("Overwrite file?", question, options, 1, owner) == 0) return filepath; else return null;
        }
        return filepath;
    }
