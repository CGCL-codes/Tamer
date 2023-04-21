    public String echo(String aMessage) throws PSIException {
        DefaultHttpClient hc = new DefaultHttpClient();
        try {
            String url;
            try {
                url = contextURL + "/EchoServlet?input=" + URLEncoder.encode(aMessage, "UTF-8");
            } catch (UnsupportedEncodingException uee) {
                throw new PSIException(uee);
            }
            HttpGet get = new HttpGet(url);
            try {
                HttpResponse response = hc.execute(get);
                InputStream entity = response.getEntity().getContent();
                String result = Util.convertToString(entity);
                return result;
            } catch (IOException t) {
                throw new PSIException(t);
            }
        } finally {
            hc.getConnectionManager().shutdown();
        }
    }
