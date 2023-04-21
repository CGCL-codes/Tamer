    private static String getServiceTicket(final String server, final String ticketGrantingTicket, final String service) throws ClientProtocolException, IOException {
        if (ticketGrantingTicket == null) return null;
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost client = new HttpPost(server + "/" + ticketGrantingTicket);
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
        nameValuePairs.add(new BasicNameValuePair("service", service));
        client.setEntity(new UrlEncodedFormEntity(nameValuePairs));
        HttpResponse response = httpclient.execute(client);
        switch(response.getStatusLine().getStatusCode()) {
            case 200:
                return _getResponseBody(response.getEntity()).toString();
            default:
                LOG.warning("Invalid response code (" + response.getStatusLine().getStatusCode() + ") from CAS server!");
                LOG.info("Response (1k): " + response.toString().substring(0, Math.min(1024, response.toString().length())));
                break;
        }
        return null;
    }
