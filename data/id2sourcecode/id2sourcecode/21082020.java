    public static boolean renameFile(final File fileToRename, final File newFileName, final boolean delete) {
        boolean success = fileToRename.renameTo(newFileName);
        if (!success) {
            System.err.println("The file " + fileToRename.getName() + " was not renamed.");
            if (delete) {
                System.err.println("Try to copy the content into the new file with the new name.");
                try {
                    boolean copied = CopyFileUtils.copyFile(fileToRename, newFileName);
                    if (copied) {
                        System.err.println("Sucessfully copied the old file " + fileToRename.getName() + " to the new file " + newFileName.getName() + ".");
                    } else {
                        System.err.println("Try to copy file " + fileToRename.getName() + " into the new file " + newFileName.getName() + " failed.");
                    }
                } catch (final IOException e) {
                    System.err.println("Try to copy file " + fileToRename.getName() + " into the new file " + newFileName.getName() + " failed.");
                } catch (FileIsADirectoryException e) {
                    e.printStackTrace();
                }
                System.err.println("Try to delete the old file " + fileToRename.getName() + ".");
                try {
                    DeleteFileUtils.delete(fileToRename);
                    success = true;
                } catch (final IOException e) {
                    System.err.println("Try to delete the old file " + fileToRename.getName() + " failed.");
                }
            }
        }
        return success;
    }
