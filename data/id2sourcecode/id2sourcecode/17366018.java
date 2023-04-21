    @Override
    public InputStream getInputStream() throws IOException {
        try {
            int port = m_url.getPort() > 0 ? m_url.getPort() : m_url.getDefaultPort();
            String[] userInfo = m_url.getUserInfo() == null ? null : m_url.getUserInfo().split(":");
            HttpClient client = new DefaultHttpClient();
            HttpGet request = new HttpGet(URIUtils.createURI(m_url.getProtocol(), m_url.getHost(), port, m_url.getPath(), m_url.getQuery(), null));
            if (userInfo != null) {
                UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(userInfo[0], userInfo[1]);
                request.addHeader(BasicScheme.authenticate(credentials, "UTF-8", false));
            }
            HttpResponse response = client.execute(request);
            return new ByteArrayInputStream(EntityUtils.toByteArray(response.getEntity()));
        } catch (Exception e) {
            throw new IOException("Can't retrieve " + m_url.getPath() + " from " + m_url.getHost() + " because " + e.getMessage());
        }
    }
