    @Override
    public InputStream getXMLInputStream() {
        try {
            URLConnection urlConn = scriptURL.openConnection();
            return new BufferedInputStream(urlConn.getInputStream());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
