    public Properties echoInitParameters() throws IOException {
        DefaultHttpClient hc = new DefaultHttpClient();
        try {
            String url = contextURL + "/DumpInitPropsServlet";
            HttpGet get = new HttpGet(url);
            HttpResponse response = hc.execute(get);
            if (response.getStatusLine().getStatusCode() != 200) {
                throw new IOException("Unexpected result code: " + response.getStatusLine().getStatusCode());
            }
            Properties result = new Properties();
            result.load(response.getEntity().getContent());
            return result;
        } finally {
            hc.getConnectionManager().shutdown();
        }
    }
