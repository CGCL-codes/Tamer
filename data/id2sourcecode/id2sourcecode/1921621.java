    public static Response getUrlContentAsBytes(String url, boolean followRedirect) throws Exception {
        Response response = new Response();
        HttpClientParams httpParams = new HttpClientParams();
        HttpClient httpClient = new HttpClient(httpParams);
        GetMethod gm = new GetMethod(url);
        gm.setFollowRedirects(followRedirect);
        httpClient.getParams().setParameter("http.protocol.allow-circular-redirects", true);
        httpClient.executeMethod(gm);
        response.setResponseUrl(gm.getURI().toString());
        InputStream input = gm.getResponseBodyAsStream();
        ByteArrayOutputStream bOut = new ByteArrayOutputStream();
        byte[] buff = new byte[1000];
        int read = 0;
        while ((read = input.read(buff)) > 0) {
            bOut.write(buff, 0, read);
        }
        Header hdr = gm.getResponseHeader("Content-Type");
        if (hdr != null) {
            response.setContentType(hdr.getValue());
        }
        response.setResponseAsBytes(bOut.toByteArray());
        return response;
    }
