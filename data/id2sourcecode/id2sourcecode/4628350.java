    public static String postUrlDataAuth(String url, String username, String password, List<NameValuePair> params) throws ClientProtocolException, IOException {
        String reponse = null;
        DefaultHttpClient httpclient = new DefaultHttpClient();
        if (Common.DEBUG) Log.d(Common.TAG, "POST auth : " + url);
        HttpPost post = new HttpPost(url);
        HttpRequestInterceptor preemptiveAuth = new HttpRequestInterceptor() {

            public void process(final HttpRequest request, final HttpContext context) throws HttpException, IOException {
                AuthState authState = (AuthState) context.getAttribute(ClientContext.TARGET_AUTH_STATE);
                CredentialsProvider credsProvider = (CredentialsProvider) context.getAttribute(ClientContext.CREDS_PROVIDER);
                HttpHost targetHost = (HttpHost) context.getAttribute(ExecutionContext.HTTP_TARGET_HOST);
                if (authState.getAuthScheme() == null) {
                    AuthScope authScope = new AuthScope(targetHost.getHostName(), targetHost.getPort());
                    Credentials creds = credsProvider.getCredentials(authScope);
                    if (creds != null) {
                        authState.setAuthScheme(new BasicScheme());
                        authState.setCredentials(creds);
                    }
                }
            }
        };
        AuthScope authScope = new AuthScope(post.getURI().getHost(), post.getURI().getPort(), AuthScope.ANY_REALM);
        UsernamePasswordCredentials userpass = new UsernamePasswordCredentials(username, password);
        httpclient.getCredentialsProvider().setCredentials(authScope, userpass);
        httpclient.addRequestInterceptor(preemptiveAuth, 0);
        try {
            UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params, HTTP.UTF_8);
            post.setEntity(ent);
            HttpResponse responsePOST = httpclient.execute(post);
            if (Common.DEBUG) Log.i(Common.TAG, "reponse " + responsePOST.getStatusLine());
            HttpEntity entity = responsePOST.getEntity();
            if (Common.DEBUG) Log.d(Common.TAG, "Response size=" + entity.getContentLength());
            if (responsePOST.getStatusLine().getStatusCode() == 200) {
                if (entity != null) {
                    reponse = Common.convertStreamToString(entity);
                } else {
                    reponse = responsePOST.getStatusLine().toString();
                }
            } else {
                ClientProtocolException e = new ClientProtocolException(responsePOST.getStatusLine().toString());
                throw e;
            }
        } catch (ClientProtocolException e) {
            throw e;
        } catch (IOException e) {
            throw e;
        }
        if (Common.DEBUG) Log.d(Common.TAG, "POST auth reponse: " + reponse);
        return reponse;
    }
