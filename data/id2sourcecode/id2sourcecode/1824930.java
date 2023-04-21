    private static void copyMoveFolderRecursive(File src, File dest, boolean move) throws IOException {
        File[] fs = Utils.listFilesForFolder(src);
        File f;
        for (int i = 0; i < fs.length; i++) {
            f = fs[i];
            if (!f.canRead()) {
                throw new IOException("no read access to " + f.getAbsolutePath());
            }
            if (f.isFile()) {
                copyFile(f, new File(dest, f.getName()));
            } else if (f.isDirectory()) {
                File destFolder = new File(dest, f.getName());
                if (!destFolder.exists()) {
                    destFolder.mkdir();
                }
                copyMoveFolderRecursive(f, destFolder, move);
            }
            if (move && f.canWrite()) {
                f.delete();
            }
        }
    }
