    public Object getChannelObject() throws XAwareException {
        HttpClient httpClient = new HttpClient();
        HostConfiguration hostConfiguration = new HostConfiguration();
        try {
            hostConfiguration.setHost(new URI(m_urlString, false));
        } catch (Exception e) {
            throw new XAwareException("Failed to parse the url: " + m_urlString, e);
        }
        httpClient.setHostConfiguration(hostConfiguration);
        if (m_timeout >= 0) {
            httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(m_timeout);
        }
        HttpState httpState = null;
        if (m_uid != null && !"".equals(m_uid.trim())) {
            AuthScope authScope = new AuthScope(hostConfiguration.getHost(), hostConfiguration.getPort());
            if (httpState == null) {
                httpState = new HttpState();
            }
            httpState.setCredentials(authScope, new UsernamePasswordCredentials(m_uid, m_pwd));
        }
        if (m_proxyUser != null && !"".equals(m_proxyUser.trim())) {
            AuthScope authScope = new AuthScope(AuthScope.ANY_HOST, AuthScope.ANY_PORT);
            if (m_proxyHost != null && !"".equals(m_proxyHost.trim())) {
                authScope = new AuthScope(m_proxyHost, m_proxyPort);
            }
            if (httpState == null) {
                httpState = new HttpState();
            }
            httpState.setProxyCredentials(authScope, new UsernamePasswordCredentials(m_proxyUser, m_proxyPwd));
        }
        if (httpState != null) {
            httpClient.setState(httpState);
        }
        SoapTemplate template = new SoapTemplate(httpClient);
        template.setServiceAddress(m_urlString);
        return template;
    }
