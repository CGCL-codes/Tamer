    @Override
    public InputStream getResource() {
        try {
            URLConnection urlConn = url.openConnection();
            urlConn.setRequestProperty("Cookie", "toolbar_hash=" + this.toolbarHash.replaceAll(" ", "_") + "; toolbar_code=" + this.toolbarCode + ";");
            urlConn.connect();
            return urlConn.getInputStream();
        } catch (Exception ex) {
            return null;
        }
    }
