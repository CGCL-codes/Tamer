    private DicomObject loadManifest(URL url) throws IOException {
        java.net.HttpURLConnection httpUrlConn = (java.net.HttpURLConnection) url.openConnection();
        InputStream bis = httpUrlConn.getInputStream();
        DicomObject manifest = null;
        try {
            DicomInputStream in = new DicomInputStream(bis);
            manifest = in.readDicomObject();
        } finally {
            try {
                bis.close();
            } catch (IOException ignore) {
            }
        }
        return manifest;
    }
