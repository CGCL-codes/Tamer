    protected final Hashtable<String, Object> processRequest(Hashtable<String, Object> params, String servlet) {
        Hashtable<String, Object> response = null;
        try {
            URL url = new URL(Admin.server + servlet);
            URLConnection conn = url.openConnection();
            conn.setUseCaches(false);
            conn.setDoOutput(true);
            this.doRequest(params, conn);
            response = this.processResponse(conn);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }
