    private Dataset loadDataset(URL url) throws IOException {
        url = checkHost(url);
        java.net.HttpURLConnection httpUrlConn = (java.net.HttpURLConnection) url.openConnection();
        InputStream bis = httpUrlConn.getInputStream();
        Dataset ds = DcmObjectFactory.getInstance().newDataset();
        try {
            ds.readFile(bis, FileFormat.DICOM_FILE, -1);
        } finally {
            try {
                bis.close();
            } catch (IOException ignore) {
            }
        }
        return ds;
    }
