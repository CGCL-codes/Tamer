    public static void copyFile(final File srcFile, final File destFile, final int override) throws FileNotFoundException, IOException {
        if (override == OVERRIDE_NEVER && destFile.exists()) {
            return;
        }
        if (override == OVERRIDE_UPDATE && destFile.lastModified() > srcFile.lastModified()) {
            return;
        }
        if (!destFile.exists()) {
            File parent = destFile.getParentFile();
            if (parent != null) {
                parent.mkdirs();
            }
        }
        FileInputStream fis = null;
        FileOutputStream fos = null;
        try {
            fis = new FileInputStream(srcFile);
            fos = new FileOutputStream(destFile);
            fis.getChannel().transferTo(0, srcFile.length(), fos.getChannel());
        } finally {
            close(fis);
            close(fos);
        }
    }
