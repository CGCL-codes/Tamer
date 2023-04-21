    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        context = config.getServletContext();
        String value = (String) config.getInitParameter("php_request_uri_is_unique");
        if ("on".equalsIgnoreCase(value) || "true".equalsIgnoreCase(value)) phpRequestURIisUnique = true;
        contextLoaderListener = (ContextLoaderListener) context.getAttribute(ContextLoaderListener.CONTEXT_LOADER_LISTENER);
        serverInfo = config.getServletName();
        if (serverInfo == null) serverInfo = "FastCGIServlet";
        documentRoot = ServletUtil.getRealPath(context, "");
        serverSignature = context.getServerInfo();
        connectionPool = contextLoaderListener.getConnectionPool();
        if (connectionPool == null) {
            try {
                contextLoaderListener.getChannelName().test();
            } catch (FCGIConnectException e) {
                throw new ServletException(e);
            }
            throw new ServletException("No connection pool");
        }
    }
