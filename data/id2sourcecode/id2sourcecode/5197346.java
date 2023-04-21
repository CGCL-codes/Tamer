    @Override
    protected long getLastModified(HttpServletRequest req) {
        try {
            ServletContext ctx = this.getServletContext();
            URL url = ctx.getResource(req.getServletPath());
            if (url == null) return -1;
            return url.openConnection().getLastModified();
        } catch (IOException e) {
            return -1;
        }
    }
