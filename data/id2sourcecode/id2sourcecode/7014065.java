    public Response httpRequest(String url, PostParameter[] postParams, boolean authenticated, String httpMethod) throws WeiboException {
        org.apache.http.client.HttpClient client = HttpConfig.newInstance();
        HttpRequestBase request;
        if (null != postParams || "POST".equals(httpMethod)) {
            request = new HttpPost(url);
            request.addHeader("Content-Type", "application/x-www-form-urlencoded");
            List<NameValuePair> nvps = new ArrayList<NameValuePair>();
            for (PostParameter p : postParams) {
                nvps.add(new BasicNameValuePair(p.name, p.value));
            }
            try {
                ((HttpPost) request).setEntity(new UrlEncodedFormEntity(nvps, "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        } else if ("DELETE".equals(httpMethod)) {
            request = new HttpDelete(url);
        } else {
            request = new HttpGet(url);
        }
        if (authenticated) {
            if (basic == null && oauth == null) {
            }
            String authorization = null;
            if (null != oauth) {
                authorization = oauth.generateAuthorizationHeader(httpMethod, url, postParams, oauthToken);
            } else if (null != basic) {
                authorization = this.basic;
            } else {
                throw new IllegalStateException("Neither user ID/password combination nor OAuth consumer key/secret combination supplied");
            }
            request.addHeader("Authorization", authorization);
            log("Authorization: " + authorization);
        }
        for (String key : requestHeaders.keySet()) {
            request.addHeader(key, requestHeaders.get(key));
            log(key + ": " + requestHeaders.get(key));
        }
        Response res = null;
        int responseCode = -1;
        try {
            HttpResponse response = client.execute(request);
            res = new Response(response);
            responseCode = response.getStatusLine().getStatusCode();
            if (responseCode != OK) {
                if (responseCode < INTERNAL_SERVER_ERROR) {
                    throw new WeiboException(getCause(responseCode) + "\n" + res.asString(), responseCode);
                }
            }
        } catch (Exception e) {
            throw new WeiboException(e.getMessage(), e, responseCode);
        }
        return res;
    }
