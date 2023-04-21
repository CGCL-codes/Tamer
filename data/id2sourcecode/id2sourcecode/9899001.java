    @Override
    public WebResponse sendRequest(WebRequest request) throws IOException, Exception {
        for (Entry<String, String> entry : request.getHeaders().entrySet()) {
            m_httpMethod.addHeader(entry.getKey(), entry.getValue());
        }
        try {
            HttpResponse response = m_httpClient.execute(m_httpMethod);
            return new WebResponse(request, response);
        } catch (Exception e) {
            log().info(e.getMessage(), e);
            return new WebResponse(request, null);
        }
    }
