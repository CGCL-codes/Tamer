    protected void handleLocalConnection(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        InputStream sin = null;
        ByteArrayOutputStream sout;
        OutputStream resOut = null;
        SimpleServletContextFactory ctx = getContextFactory(req, res);
        JavaBridge bridge = ctx.getBridge();
        ctx.setSessionFactory(req);
        bridge.in = sin = req.getInputStream();
        bridge.out = sout = new ByteArrayOutputStream();
        Request r = bridge.request = new Request(bridge);
        if (r.init(sin, sout)) {
            AbstractChannelName channelName = contextServer.getChannelName(ctx);
            res.setHeader("X_JAVABRIDGE_REDIRECT", channelName.getName());
            contextServer.start(channelName, logger);
            if (!r.handleOneRequest()) throw new IOException("parse error");
            res.setContentLength(sout.size());
            resOut = res.getOutputStream();
            sout.writeTo(resOut);
            if (bridge.logLevel > 3) bridge.logDebug("redirecting to port# " + channelName);
            sin.close();
            try {
                res.flushBuffer();
            } catch (Throwable t) {
                Util.printStackTrace(t);
            }
            try {
                resOut.close();
            } catch (Throwable t) {
                Util.printStackTrace(t);
            }
            this.waitForContext(ctx);
        } else {
            Util.warn("handleLocalConnection init failed");
            ctx.destroy();
        }
    }
