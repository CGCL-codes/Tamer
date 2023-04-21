    @SuppressWarnings("unchecked")
    public static void displayZoteroExport(final String urlString) {
        AccessController.doPrivileged(new PrivilegedAction() {

            public Object run() {
                try {
                    File tempFile = null;
                    tempFile = File.createTempFile(new Long(System.currentTimeMillis()).toString(), null);
                    InputStream io = null;
                    io = getInputStream(urlString);
                    byte[] buf = new byte[256];
                    int read = 0;
                    java.io.FileOutputStream fos = null;
                    fos = new java.io.FileOutputStream(tempFile);
                    while ((read = io.read(buf)) > 0) {
                        fos.write(buf, 0, read);
                    }
                    tufts.vue.action.TextOpenAction.displayMap(tempFile);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
        });
    }
