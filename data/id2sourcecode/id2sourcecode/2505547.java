    protected InputStream openConnection(String url) throws HttpException, IOException {
        Protocol.registerProtocol("https", new Protocol("https", new EasySSLProtocolSocketFactory(), 443));
        m_client = new HttpClient();
        if (m_credentials != null) {
            IPreferenceStore store = Activator.getDefault().getPreferenceStore();
            String userId = store.getString(PreferenceConstants.USERCONTEXT_USERID);
            PasswordCipher.Credentials credentials = PasswordCipher.decode(CRYPT_ADD + userId + CRYPT_ADD, m_credentials);
            m_client.getState().setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(credentials.getUserId(), credentials.getPassword()));
        }
        m_method = new GetMethod(url);
        m_method.setDoAuthentication(m_credentials != null);
        if (m_client.executeMethod(m_method) == HttpStatus.SC_OK) return m_method.getResponseBodyAsStream();
        return null;
    }
