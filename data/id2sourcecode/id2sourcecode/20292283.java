    public void open() throws IOException, DPWSException {
        client = (HttpClient) ((HttpChannel) getMessage().getChannel()).getProperty(HTTP_CLIENT);
        if (client == null) {
            client = new HttpClient();
            ((HttpChannel) getMessage().getChannel()).setProperty(HTTP_CLIENT, client);
        }
        DPWSContextImpl context = getMessageContext();
        HttpClientParams params = (HttpClientParams) context.getContextualProperty(HTTP_CLIENT_PARAMS);
        if (params == null) {
            params = client.getParams();
            client.getParams().setParameter("http.useragent", USER_AGENT);
            client.getParams().setBooleanParameter("http.protocol.expect-continue", true);
            client.getParams().setVersion(HttpVersion.HTTP_1_1);
        } else {
            client.setParams(params);
        }
        String proxyHost = (String) context.getContextualProperty(HTTP_PROXY_HOST);
        if (proxyHost != null) {
            String portS = (String) context.getContextualProperty(HTTP_PROXY_PORT);
            int port = 80;
            if (portS != null) port = Integer.parseInt(portS);
            client.getHostConfiguration().setProxy(proxyHost, port);
        }
        state = (HttpState) context.getContextualProperty(HTTP_STATE);
        if (state == null) state = getHttpState();
        postMethod = new PostMethod(getUri());
        String username = (String) context.getContextualProperty(Channel.USERNAME);
        if (username != null) {
            String password = (String) context.getContextualProperty(Channel.PASSWORD);
            client.getParams().setAuthenticationPreemptive(true);
            state.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(username, password));
        }
        if (getSoapAction() != null) {
            postMethod.setRequestHeader("SOAPAction", getQuotedSoapAction());
        }
        OutMessage message = getMessage();
        boolean mtomEnabled = Boolean.valueOf((String) context.getContextualProperty(SoapConstants.MTOM_ENABLED)).booleanValue();
        Attachments atts = message.getAttachments();
        if (mtomEnabled || atts != null) {
            if (atts == null) {
                atts = new JavaMailAttachments();
                message.setAttachments(atts);
            }
            OutMessageDataSource source = new OutMessageDataSource(context, message);
            DataHandler soapHandler = new DataHandler(source);
            atts.setSoapContentType(HttpChannel.getSoapMimeType(message));
            atts.setSoapMessage(new SimpleAttachment(source.getName(), soapHandler));
            postMethod.setRequestHeader("Content-Type", atts.getContentType());
        } else {
            postMethod.setRequestHeader("Content-Type", HttpChannel.getSoapMimeType(getMessage()));
        }
    }
