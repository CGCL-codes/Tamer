    static String getTicketGrantingTicket(final String server, final String username, final String password) throws ClientProtocolException, IOException {
        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost(server);
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
        nameValuePairs.add(new BasicNameValuePair("username", username));
        nameValuePairs.add(new BasicNameValuePair("password", password));
        post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
        HttpResponse responseHttp = client.execute(post);
        String response = _getResponseBody(responseHttp.getEntity());
        switch(responseHttp.getStatusLine().getStatusCode()) {
            case 201:
                {
                    final Matcher matcher = Pattern.compile(".*action=\".*/(.*?)\".*").matcher(response);
                    if (matcher.matches()) return matcher.group(1);
                    LOG.warning("Successful ticket granting request, but no ticket found!");
                    LOG.info("Response (1k): " + response.substring(0, Math.min(1024, response.length())));
                    break;
                }
            default:
                LOG.warning("Invalid response code (" + responseHttp.getStatusLine().getStatusCode() + ") from CAS server!");
                LOG.info("Response (1k): " + response.substring(0, Math.min(1024, response.length())));
                break;
        }
        return null;
    }
