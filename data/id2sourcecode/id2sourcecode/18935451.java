    public void init(SiteContext site, Configuration config) throws Exception {
        log.info(NAME + " starting for: " + site.getServletContext().getServletContextName());
        this.site = site;
        String cayenneConfigPath = config.getChildValue("cayenne-config-path", "WEB-INF");
        log.info("Initializing the " + CAYENNE_VERSION + "framework from: " + cayenneConfigPath);
        conf = new DefaultConfiguration(org.apache.cayenne.conf.Configuration.DEFAULT_DOMAIN_FILE, createLocator(site.getServletContext(), cayenneConfigPath));
        org.apache.cayenne.conf.Configuration.initializeSharedConfiguration(conf);
        String value = config.getChildValue("auto-rollback", "true");
        autoRollback = "true".equalsIgnoreCase(value);
        value = config.getChildValue("session-scope", "true");
        sessionScope = "true".equalsIgnoreCase(value);
        value = config.getChildValue("shared-cache", "true");
        sharedCache = "true".equalsIgnoreCase(value);
        value = config.getChildValue("debug", "false");
        debugEnabled = "true".equalsIgnoreCase(value);
        String msg = "Cayenne DataContext initialized with: auto-rollback=" + autoRollback + ", session-scope=" + sessionScope + ", shared-cache=" + sharedCache;
        log.info(msg);
        log.info("Mapping Cayenne support...");
        List urls = config.getChild("cayenne-enabled-urls").getChildren();
        if (urls != null && !urls.isEmpty()) {
            for (int i = 0; i < urls.size(); i++) {
                Configuration url = (Configuration) urls.get(i);
                String path = url.getAttribute("path");
                if (path != null) {
                    ActionWrapper aw;
                    boolean ro = url.getAttribute("readonly", "false").equalsIgnoreCase("true");
                    if (ro) {
                        aw = new ActionWrapper(new PathAction(path, new CayenneReadOnlySupportAction(this)), config);
                    } else {
                        aw = new ActionWrapper(new PathAction(path, new CayenneSupportBeforeAction(this)), config);
                        site.getActionManager().getPostEvaluationActions().add(new ActionWrapper(new PathAction(path, new CayenneSupportAfterAction(this)), config));
                    }
                    site.getActionManager().getPathActions().add(aw);
                    log.info("... added " + (ro ? "read-only" : "read-write") + " support for: " + path);
                }
            }
        } else {
            log.info(" ... the 'cayenne-enabled-urls' node contains no mappings. " + "Cayenne support will be unavailable to the web requests.");
        }
        DataContext dataContext = (DataContext) site.getAttribute(ServletUtil.DATA_CONTEXT_KEY);
        if (dataContext == null) {
            dataContext = DataContext.createDataContext(isSharedCache());
            site.setAttribute(ServletUtil.DATA_CONTEXT_KEY, dataContext);
            if (isDebugEnabled()) {
                log.info("Created an Application wide DataContext with shared-cache=" + isSharedCache());
            }
        }
        DataContext.bindThreadDataContext(dataContext);
        site.setAttribute(JPCayenneModule.JPCAYENNE_SERVICE_NAME, new JPCayenneService());
        log.info(this.toString() + " started.");
    }
