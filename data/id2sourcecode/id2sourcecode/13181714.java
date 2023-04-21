    public HttpResponse get(String toFetchStr, String headers[][]) throws IOException, ClientProtocolException {
        HttpClient httpClient = makeHTTPClient();
        String fetchUrl = null == toFetchStr ? "http://goo" + ".gl/service/search.html?searchTerm=jcrontab" : toFetchStr;
        HttpUriRequest m = new HttpGet(fetchUrl);
        for (String[] nextHeader : headers) {
            String headerNameTmp = nextHeader[0];
            String headerValTmp = nextHeader[1];
            log.trace("HEADER{}={}", headerNameTmp, headerValTmp);
            m.addHeader(headerNameTmp, headerValTmp);
        }
        addCookies(m);
        m.addHeader("Host", m.getURI().getHost());
        HttpResponse respTmp = httpClient.execute(m);
        respTmp = makeAuth(toFetchStr, httpClient, m, respTmp);
        this.parseCookies(m, respTmp);
        return respTmp;
    }
