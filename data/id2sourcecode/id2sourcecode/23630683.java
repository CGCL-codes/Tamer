    private boolean confirmFileOverwrite(File file) {
        String message = "The file:\n" + file.getPath() + "\nalready exists. Are you sure you want to overwrite it?";
        return guiBuilder.showConfirm("File Overwrite", message, "");
    }
