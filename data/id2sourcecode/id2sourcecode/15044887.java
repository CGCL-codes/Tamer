    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        try {
            String servletPath = httpRequest.getServletPath();
            int pos = servletPath.indexOf(urlPath);
            if (pos > -1) {
                if (pos > 0) {
                    servletPath = servletPath.substring(pos);
                }
                ResponseUtil.setContentType(response, HttpUtil.getContentType(servletPath));
                StreamUtil.write(response.getOutputStream(), UrlUtil.openStream(ClassUtil.getResource(servletPath.replaceAll(urlPath, filePath))));
            }
        } catch (RuntimeException e) {
            WdLogs.warn(e);
        }
    }
