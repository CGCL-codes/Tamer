    public static void insertDocumentToURL(byte[] data, String target) throws IOException {
        final URL url = new URL(target);
        final URLConnection connection = url.openConnection();
        InputStream is = null;
        OutputStream os = null;
        try {
            is = new ByteArrayInputStream(data);
            os = connection.getOutputStream();
            TestTools.copyStream(is, os);
            os.flush();
        } finally {
            if (is != null) {
                is.close();
            }
            if (os != null) {
                os.close();
            }
        }
    }
