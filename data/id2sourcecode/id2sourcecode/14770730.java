    public static boolean moveOrRename(File dest, File source) {
        boolean rv = false;
        if (source.renameTo(dest)) rv = true; else if (FileUtils.copyFile(dest, source)) {
            source.delete();
            rv = true;
        }
        return rv;
    }
