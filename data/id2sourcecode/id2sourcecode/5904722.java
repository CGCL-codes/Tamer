    public static int getFileLength(final File file) {
        if (file == null) {
            return 0;
        }
        try {
            final URL url = new URL("file:///" + file.getCanonicalPath());
            if (url == null) {
                return 0;
            }
            final URLConnection uc = url.openConnection();
            if (uc == null) {
                return 0;
            }
            return uc.getContentLength();
        } catch (IOException e) {
            return 0;
        }
    }
