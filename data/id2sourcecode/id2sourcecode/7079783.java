    private static void updateArtifact(File from, File to, String urlStr) {
        if (!to.exists()) {
            OutputStream os = null;
            InputStream is = null;
            try {
                URL url = new URL(urlStr);
                is = url.openStream();
                os = new FileOutputStream(to, false);
                StreamUtil.copy(os, is);
            } catch (IOException e) {
            } finally {
                StreamUtil.close(os);
                StreamUtil.close(is);
                from.delete();
            }
        }
    }
