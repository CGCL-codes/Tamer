    public static String getUrlDataAuth(String url, String username, String password) throws IOException, ClientProtocolException {
        String reponse = null;
        DefaultHttpClient httpclient = new DefaultHttpClient();
        if (Common.DEBUG) Log.d(Common.TAG, "GET auth : " + url);
        HttpGet httpget = new HttpGet(url);
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
        AuthScope authScope = new AuthScope(httpget.getURI().getHost(), httpget.getURI().getPort(), AuthScope.ANY_REALM);
        UsernamePasswordCredentials userpass = new UsernamePasswordCredentials(username, password);
        httpclient.getCredentialsProvider().setCredentials(authScope, userpass);
        httpclient.addRequestInterceptor(preemptiveAuth, 0);
        try {
            HttpResponse response = httpclient.execute(httpget);
            if (Common.DEBUG) Log.i(Common.TAG, response.getStatusLine().toString());
            if (response.getStatusLine().getStatusCode() == 200) {
                HttpEntity entity = response.getEntity();
                if (Common.DEBUG) Log.d(Common.TAG, "Response size=" + entity.getContentLength());
                if (entity != null) {
                    reponse = Common.convertStreamToString(entity);
                } else {
                    reponse = response.getStatusLine().toString();
                }
            } else {
                ClientProtocolException e = new ClientProtocolException(response.getStatusLine().toString());
                throw e;
            }
        } catch (ClientProtocolException e) {
            throw e;
        } catch (IOException e) {
            throw e;
        }
        if (Common.DEBUG) Log.i(Common.TAG, "GET AUTH response : " + reponse);
        return reponse;
    }
