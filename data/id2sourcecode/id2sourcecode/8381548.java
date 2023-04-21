    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
        HttpServletResponse response = (HttpServletResponse) resp;
        HttpServletRequest request = (HttpServletRequest) req;
        String path = request.getRequestURI().substring(request.getContextPath().length());
        URL url = servletContext.getResource(path);
        if (url != null) {
            chain.doFilter(req, resp);
        } else {
            log.debug("Serving resource: " + path);
            url = ResourceFilter.class.getResource(path);
            if (url != null) {
                URLConnection conn = url.openConnection();
                InputStream in = conn.getInputStream();
                response.setContentType(servletContext.getMimeType(path));
                response.setDateHeader("Last-Modified", conn.getLastModified());
                response.setHeader("Cache-Control", "No-Cache");
                BufferedInputStream bin = new BufferedInputStream(in);
                ServletOutputStream out = response.getOutputStream();
                byte[] buf = new byte[1024];
                int count;
                while ((count = bin.read(buf)) > 0) {
                    out.write(buf, 0, count);
                }
                out.close();
                bin.close();
                in.close();
            } else {
                log.warn("Resource not found: " + path);
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        }
    }
