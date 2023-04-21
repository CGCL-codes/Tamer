    protected boolean online(Webservice.Description wsdesc) {
        URLConnection conn;
        URL url;
        conn = null;
        try {
            url = new URL(wsdesc.getWsdlURL());
            conn = url.openConnection();
            conn.connect();
            return true;
        } catch (Exception e) {
            return false;
        } finally {
            try {
                conn.getOutputStream().close();
            } catch (Exception e) {
            }
        }
    }
