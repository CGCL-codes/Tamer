    public static boolean renameFile(File fileToRename, File newFileName, boolean delete) {
        boolean success = fileToRename.renameTo(newFileName);
        if (!success) {
            System.err.println("The file " + fileToRename.getName() + " was not renamed.");
            if (delete) {
                System.err.println("Try to delete the old file and " + "copy the content into the new file with the new name.");
                try {
                    FileUtils.copyFile(fileToRename, newFileName);
                    FileUtils.delete(fileToRename);
                    success = true;
                } catch (IOException e) {
                    System.err.println("The file " + fileToRename.getName() + " was not renamed." + " Failed to copy the old file into the new File.");
                }
            }
        }
        return success;
    }
