    public static List<HypericAlertStatus> fetchHypericAlerts(String hypericUrl, List<String> alertIds) throws IOException, JAXBException, XMLStreamException {
        List<HypericAlertStatus> retval = new ArrayList<HypericAlertStatus>();
        if (alertIds.size() < 1) {
            return retval;
        }
        for (int i = 0; i < alertIds.size(); i++) {
            StringBuffer alertIdString = new StringBuffer();
            alertIdString.append("?");
            for (int j = 0; (j < ALERTS_PER_HTTP_TRANSACTION) && (i < alertIds.size()); j++, i++) {
                if (j > 0) alertIdString.append("&");
                alertIdString.append("id=").append(alertIds.get(i));
            }
            DefaultHttpClient httpClient = new DefaultHttpClient();
            httpClient.getParams().setIntParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 3000);
            httpClient.getParams().setIntParameter(CoreConnectionPNames.SO_TIMEOUT, 3000);
            HttpUriRequest httpMethod = new HttpGet(hypericUrl + alertIdString.toString());
            httpMethod.getParams().setParameter(CoreProtocolPNames.USER_AGENT, "OpenNMS-Ackd.HypericAckProcessor");
            String userinfo = null;
            try {
                URI hypericUri = new URI(hypericUrl);
                userinfo = hypericUri.getUserInfo();
            } catch (URISyntaxException e) {
                log().warn("Could not parse URI to get username/password stanza: " + hypericUrl, e);
            }
            if (userinfo != null && !"".equals(userinfo)) {
                httpClient.getCredentialsProvider().setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(userinfo));
                HttpRequestInterceptor preemptiveAuth = new HttpRequestInterceptor() {

                    public void process(final HttpRequest request, final HttpContext context) throws IOException {
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
                httpClient.addRequestInterceptor(preemptiveAuth, 0);
            }
            try {
                HttpResponse response = httpClient.execute(httpMethod);
                retval = parseHypericAlerts(new StringReader(EntityUtils.toString(response.getEntity())));
            } finally {
            }
        }
        return retval;
    }
