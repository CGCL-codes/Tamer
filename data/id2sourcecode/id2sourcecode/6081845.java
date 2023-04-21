    public static String post(String url, Map<String, String> parameters) throws URISyntaxException, ClientProtocolException, IOException, ServiceException, HttpHostConnectException {
        DefaultHttpClient client = new DefaultHttpClient();
        String xmlData = null;
        URI uri = new URI(url);
        List<NameValuePair> requestParameters = null;
        HttpPost method = new HttpPost(uri);
        method.addHeader("User-Agent", "User-Agent: Mozilla/5.0 (Windows; U; Windows NT 6.0; en-US; rv:1.8.1.10) Gecko/20071115 Firefox/2.0.0.10");
        method.addHeader("Pragma", "no-cache");
        method.addHeader("Content-Type", "application/x-www-form-urlencoded");
        if (parameters != null && parameters.size() > 0) {
            requestParameters = new ArrayList<NameValuePair>();
            for (String parameter : parameters.keySet()) {
                requestParameters.add(new BasicNameValuePair(parameter, parameters.get(parameter)));
            }
        }
        HttpEntity entity = new UrlEncodedFormEntity(requestParameters, "UTF-8");
        method.setEntity(entity);
        HttpResponse res = client.execute(method);
        InputStream ips = res.getEntity().getContent();
        xmlData = geraString(ips);
        return xmlData;
    }
