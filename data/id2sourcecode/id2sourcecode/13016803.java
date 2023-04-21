    private File cache(File assetsCache, String path) throws IOException {
        File file = new File(assetsCache, path);
        if (!file.exists()) {
            InputStream in = getContext().getAssets().open(path);
            try {
                file.getParentFile().mkdirs();
                OutputStream out = new FileOutputStream(file);
                try {
                    byte[] buffer = new byte[4096];
                    int read;
                    while ((read = in.read(buffer)) != -1) {
                        out.write(buffer, 0, read);
                    }
                } finally {
                    out.close();
                }
            } finally {
                in.close();
            }
        }
        return file;
    }
