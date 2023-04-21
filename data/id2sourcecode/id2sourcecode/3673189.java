    public String getFriendSummary(String name) throws ClientProtocolException, IOException {
        HttpGet request = new HttpGet("http://us.playstation.com/playstation/psn/profile/get_gamer_summary_data?id=" + name);
        addDefaultHeaders(request);
        request.addHeader(HTTP_HEADER_REFERER, "http://us.playstation.com/playstation/psn/profile/friends?id=" + Math.random());
        request.addHeader(HTTP_HEADER_X_REQUESTED_WITH, "XMLHttpRequest");
        HttpResponse response = httpClient.execute(request, context);
        String jsonString = getInputStreamAsString(response.getEntity().getContent());
        response.getEntity().consumeContent();
        return jsonString;
    }
