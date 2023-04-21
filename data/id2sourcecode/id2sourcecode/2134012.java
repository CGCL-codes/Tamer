    protected InputStream doPost(HttpClient client, String url, NameValuePair data[], String referer) throws ContactListImporterException, HttpException, IOException, InterruptedException, URISyntaxException {
        log.info((new StringBuilder("POST ")).append(url).toString());
        HttpPost post = new HttpPost(url);
        setHeaders(post, referer);
        post.addHeader("Content-Type", "application/x-www-form-urlencoded");
        post.setEntity(new UrlEncodedFormEntity(data, "UTF-8"));
        HttpProtocolParams.setUseExpectContinue(client.getParams(), false);
        HttpProtocolParams.setUseExpectContinue(post.getParams(), false);
        HttpResponse resp = client.execute(post, client.getDefaultContext());
        updateCurrentUrl(client);
        InputStream content = resp.getEntity().getContent();
        return content;
    }
