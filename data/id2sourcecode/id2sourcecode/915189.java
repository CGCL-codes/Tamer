    protected static final void contactService(String service) throws Exception {
        HttpURLConnection connection;
        URL url;
        try {
            java.security.Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
            System.getProperties().put("java.protocol.handler.pkgs", "com.sun.net.ssl.internal.www.protocol");
            url = new URL(server + service);
            connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setUseCaches(false);
            String queryString = getXMLIn().toString();
            OutputStream out = connection.getOutputStream();
            out.write(queryString.getBytes());
            out.close();
            String data = "";
            try {
                data = readURLConnection(connection);
            } catch (Exception e) {
                System.out.println("Error in reading URL Connection" + e.getMessage());
                throw e;
            }
            setXMLOut(new StringBuffer(data));
        } catch (Exception ex) {
            ex.printStackTrace();
            new ErrorHandler(ex);
        }
    }
