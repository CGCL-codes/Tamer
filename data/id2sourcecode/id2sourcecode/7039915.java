    protected void setupCGIEnvironment(HttpServletRequest req, HttpServletResponse res, Environment env) throws ServletException {
        HashMap envp = (HashMap) contextLoaderListener.getEnvironment().clone();
        envp.put("SERVER_SOFTWARE", serverInfo);
        envp.put("SERVER_NAME", ServletUtil.nullsToBlanks(req.getServerName()));
        envp.put("GATEWAY_INTERFACE", "CGI/1.1");
        envp.put("SERVER_PROTOCOL", ServletUtil.nullsToBlanks(req.getProtocol()));
        int port = ServletUtil.getLocalPort(req);
        Integer iPort = (port == 0 ? new Integer(-1) : new Integer(port));
        envp.put("SERVER_PORT", iPort.toString());
        envp.put("REQUEST_METHOD", ServletUtil.nullsToBlanks(req.getMethod()));
        envp.put("SCRIPT_NAME", env.contextPath + env.servletPath);
        envp.put("QUERY_STRING", ServletUtil.nullsToBlanks(env.queryString));
        envp.put("REMOTE_HOST", ServletUtil.nullsToBlanks(req.getRemoteHost()));
        envp.put("REMOTE_ADDR", ServletUtil.nullsToBlanks(req.getRemoteAddr()));
        envp.put("AUTH_TYPE", ServletUtil.nullsToBlanks(req.getAuthType()));
        envp.put("REMOTE_USER", ServletUtil.nullsToBlanks(req.getRemoteUser()));
        envp.put("REMOTE_IDENT", "");
        envp.put("CONTENT_TYPE", ServletUtil.nullsToBlanks(req.getContentType()));
        setPathInfo(req, envp, env);
        int contentLength = req.getContentLength();
        String sContentLength = (contentLength <= 0 ? "" : (new Integer(contentLength)).toString());
        envp.put("CONTENT_LENGTH", sContentLength);
        Enumeration headers = req.getHeaderNames();
        String header = null;
        StringBuffer buffer = new StringBuffer();
        while (headers.hasMoreElements()) {
            header = ((String) headers.nextElement()).toUpperCase();
            if ("AUTHORIZATION".equalsIgnoreCase(header) || "PROXY_AUTHORIZATION".equalsIgnoreCase(header)) {
            } else if ("HOST".equalsIgnoreCase(header)) {
                String host = req.getHeader(header);
                int idx = host.indexOf(":");
                if (idx < 0) idx = host.length();
                envp.put("HTTP_" + header.replace('-', '_'), host.substring(0, idx));
            } else if (header.startsWith("X_")) {
                envp.put(header, req.getHeader(header));
            } else {
                envp.put("HTTP_" + header.replace('-', '_'), ServletUtil.getHeaders(buffer, req.getHeaders(header)));
            }
        }
        env.environment = envp;
        if (env.includedJava) {
            env.environment.put("X_JAVABRIDGE_INCLUDE_ONLY", "1");
            env.environment.put("X_JAVABRIDGE_INCLUDE", ServletUtil.getRealPath(getServletContext(), env.servletPath));
        }
        env.environment.put("REDIRECT_STATUS", "200");
        env.environment.put("SERVER_SOFTWARE", Util.EXTENSION_NAME);
        String sPort = (String) env.environment.get("SERVER_PORT");
        String standardPort = req.isSecure() ? _443 : _80;
        StringBuffer httpHost = new StringBuffer((String) env.environment.get("SERVER_NAME"));
        if (!standardPort.equals(sPort)) {
            httpHost.append(":");
            httpHost.append(sPort);
        }
        env.environment.put("HTTP_HOST", httpHost.toString());
        String remotePort = null;
        try {
            remotePort = String.valueOf(req.getRemotePort());
        } catch (Throwable t) {
            remotePort = String.valueOf(t);
        }
        env.environment.put("REMOTE_PORT", remotePort);
        String query = env.queryString;
        if (query != null) env.environment.put("REQUEST_URI", ServletUtil.nullsToBlanks(env.requestUri + "?" + query)); else env.environment.put("REQUEST_URI", ServletUtil.nullsToBlanks(env.requestUri));
        env.environment.put("SERVER_ADDR", req.getServerName());
        env.environment.put("SERVER_SIGNATURE", serverSignature);
        env.environment.put("DOCUMENT_ROOT", documentRoot);
        if (req.isSecure()) env.environment.put("HTTPS", "On");
        String id = PhpJavaServlet.getHeader(Util.X_JAVABRIDGE_CONTEXT, req);
        if (id == null) {
            id = (env.ctx = ServletContextFactory.addNew(contextLoaderListener.getContextServer(), this, getServletContext(), req, req, res)).getId();
            AbstractChannelName channelName = contextLoaderListener.getContextServer().getChannelName(env.ctx);
            if (channelName != null) {
                env.environment.put(Util.X_JAVABRIDGE_REDIRECT, channelName.getName());
                env.ctx.getBridge();
                contextLoaderListener.getContextServer().start(channelName, contextLoaderListener.getLogger());
            }
        }
        env.environment.put(Util.X_JAVABRIDGE_CONTEXT, id);
    }
