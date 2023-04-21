    public static void moveFile(final File oldfile, final File newfile) {
        FileUtils.copyFile(oldfile, newfile);
        oldfile.delete();
    }
