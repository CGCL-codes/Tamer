    @Test
    public void testBasicRequest() throws Exception {
        m_server.start();
        HttpClient client = new DefaultHttpClient();
        HttpGet get = new HttpGet("http://localhost:" + s_PORT + "/?a=1&b=2");
        get.setHeader("Content-Type", "text/plain");
        HttpResponse resp = client.execute(get);
        HttpEntity entity = resp.getEntity();
        JsonNode node = parseResponse(entity);
        ApplicationContext curCtx = WebApplicationContextUtils.getWebApplicationContext(m_servletHolder.getServlet().getServletConfig().getServletContext());
        JmsTemplate tmpl = (JmsTemplate) curCtx.getBean("consumeTempl");
        Object obj = tmpl.receiveAndConvert();
        assertNotNull(obj);
        assertTrue(obj instanceof TelemetryHttpSession);
        TelemetryHttpSession hsess = (TelemetryHttpSession) obj;
        validateHeaders(node, resp, hsess);
        validateParams(node, hsess);
        m_server.stop();
    }
