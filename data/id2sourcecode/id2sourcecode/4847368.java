    private String transportMessage(HttpPost postRequest) throws UnsupportedEncodingException, IOException {
        postRequest.setHeader("User-Agent", USER_AGENT);
        HttpResponse response = client.execute(postRequest);
        return convertStreamToString(response.getEntity().getContent());
    }
