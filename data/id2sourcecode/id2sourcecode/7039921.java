    protected void handle(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        try {
            execute(req, res);
        } catch (IOException e) {
            try {
                res.reset();
            } catch (Exception ex) {
            }
            StringBuffer buf = new StringBuffer("PHP FastCGI server not running. Please see server log for details.");
            if (contextLoaderListener.getChannelName() != null && context != null) {
                buf.append(" Or start a PHP FastCGI server using the command:\n");
                buf.append(contextLoaderListener.getChannelName().getFcgiStartCommand(ServletUtil.getRealPath(context, ContextLoaderListener.CGI_DIR), contextLoaderListener.getPhpMaxRequests()));
            }
            IOException ex = new IOException(buf.toString());
            ex.initCause(e);
            throw ex;
        } catch (ServletException e) {
            try {
                res.reset();
            } catch (Exception ex) {
            }
            throw e;
        } catch (Throwable t) {
            try {
                res.reset();
            } catch (Exception ex) {
            }
            if (Util.logLevel > 4) Util.printStackTrace(t);
            throw new ServletException(t);
        }
    }
