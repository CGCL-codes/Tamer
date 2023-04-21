    protected InputStream getConnection(int command, String value) {
        InputStream in = null;
        try {
            URL url = new URL(_saddr + "?" + command + "=" + value);
            _serverCon = url.openConnection();
            _serverCon.setDoInput(true);
            _serverCon.setUseCaches(false);
            _serverCon.setRequestProperty("Content_Type", "text/xml");
            in = _serverCon.getInputStream();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return in;
    }
