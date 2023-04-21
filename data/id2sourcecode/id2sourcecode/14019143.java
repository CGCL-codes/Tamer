    public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/gzip");
        LOGGER.info("Getting URL connection to sitemap...");
        ApplicationContext context = WebApplicationContextUtils.getWebApplicationContext(getServletContext());
        RheaConfig rheaConfig = (RheaConfig) context.getBean("rheaConfig");
        ;
        URL url = new URL(rheaConfig.getSitemapUrl());
        URLConnection con = url.openConnection(java.net.Proxy.NO_PROXY);
        LOGGER.info("Connecting to sitemap...");
        LOGGER.info("Connected");
        InputStream is = null;
        try {
            is = con.getInputStream();
            int r = -1;
            LOGGER.debug("Starting to read sitemap...");
            while ((r = is.read()) != -1) {
                response.getOutputStream().write(r);
            }
            response.getOutputStream().flush();
            response.flushBuffer();
            LOGGER.debug("... Read and served");
        } finally {
            if (is != null) is.close();
        }
    }
