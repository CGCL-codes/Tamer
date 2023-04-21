    public void init(FilterConfig config) throws ServletException {
        String file = config.getInitParameter("config-file");
        if (file == null) throw new ServletException("init-param \"config-file\" is required");
        try {
            gatekeeper = new Gatekeeper();
            gatekeeper.setConfig(ConfigDigester.digest(file));
            log.info("Loaded gatekeeper config from " + file + ": " + gatekeeper.getConfig());
        } catch (Exception e) {
            log.error("error trying to load gatekeeper config from " + file, e);
            throw new ServletException("error trying to load gatekeeper config from " + file, e);
        }
    }
