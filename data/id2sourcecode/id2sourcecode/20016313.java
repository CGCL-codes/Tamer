    private static void copyFile(AssetManager am, String path, File destRoot) throws IOException {
        InputStream is;
        int size;
        byte buf[] = new byte[4096];
        try {
            is = am.open(Constants._ENV_AT_ASSETS_BASE_ + path);
        } catch (IOException e) {
            throw e;
        }
        File dest = new File(destRoot, path);
        if (dest.exists()) {
            dest.delete();
        }
        try {
            dest.getParentFile().mkdirs();
            FileOutputStream fos = new FileOutputStream(dest);
            while (-1 != (size = is.read(buf))) fos.write(buf, 0, size);
        } catch (IOException e) {
            throw e;
        } finally {
            is.close();
        }
    }
