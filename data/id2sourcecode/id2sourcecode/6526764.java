    public void start() throws RitaException {
        Connector connector = new SelectChannelConnector();
        connector.setPort(8080);
        m_Server = new Server();
        m_Server.addConnector(connector);
        WebAppContext wac = new WebAppContext();
        wac.setContextPath("/");
        if (m_EnvironmentName != null) {
            Map<String, String> parameters = new HashMap<String, String>();
            parameters.put("environment", m_EnvironmentName);
            wac.setInitParams(parameters);
        }
        URL webDirUrl = JettyLoader.class.getResource("/web/");
        if (webDirUrl != null) {
            log.info("This is an executable JAR, loading web resources " + "from the classpath: " + webDirUrl);
            wac.setBaseResource(new ClassPathResource("/web/"));
        } else {
            webDirUrl = getWebDirUrlBySearch();
            log.info("This is a deployed application, loading web resources " + "from directory: " + webDirUrl);
            wac.setResourceBase(webDirUrl.toString());
        }
        Pattern pat = Pattern.compile("^jar:(.*)!(.*)");
        Matcher mat = pat.matcher(webDirUrl.toString());
        if (mat.matches()) {
            try {
                final URL jar = new URL(mat.group(1));
                URLClassLoader cl = new URLClassLoader(new URL[] { jar }, new WebAppClassLoader(wac));
                wac.setClassLoader(cl);
            } catch (Exception e) {
                throw new ServerStartupFailed(e);
            }
        }
        wac.setParentLoaderPriority(true);
        wac.getServletHandler().setStartWithUnavailable(false);
        final List<Throwable> lifecycleExceptions = new ArrayList<Throwable>();
        wac.addLifeCycleListener(new LifeCycle.Listener() {

            @Override
            public void lifeCycleFailure(LifeCycle event, Throwable cause) {
                lifecycleExceptions.add(cause);
            }

            public void lifeCycleStarted(LifeCycle event) {
            }

            public void lifeCycleStarting(LifeCycle event) {
            }

            public void lifeCycleStopped(LifeCycle event) {
            }

            public void lifeCycleStopping(LifeCycle event) {
            }
        });
        m_Server.setHandler(wac);
        m_Server.setStopAtShutdown(true);
        URL loginUrl, setupUrl;
        try {
            STARTING_IMG = new URL(webDirUrl, "img/aircraft.png");
            RUNNING_IMG = new URL(webDirUrl, "img/wfp_small.png");
            STOPPING_IMG = new URL(webDirUrl, "img/spinner.gif");
            APP_URL = new URL("http", getCurrentEnvironmentNetworkIp(), connector.getPort(), "/");
            loginUrl = new URL(APP_URL, "public/login.xhtml");
            setupUrl = new URL(APP_URL, "setup/");
        } catch (MalformedURLException e) {
            throw new ServerStartupFailed(e);
        }
        if (SystemTray.isSupported()) {
            m_Tray = SystemTray.getSystemTray();
            Image image = Toolkit.getDefaultToolkit().getImage(STARTING_IMG);
            PopupMenu popup = new PopupMenu();
            if (Desktop.isDesktopSupported()) {
                UrlListener loginEvent = new UrlListener(loginUrl);
                MenuItem loginMenuItem = new MenuItem("Open RITA");
                loginMenuItem.addActionListener(loginEvent);
                popup.add(loginMenuItem);
                UrlListener setupEvent = new UrlListener(setupUrl);
                MenuItem setupMenuItem = new MenuItem("Configure RITA");
                setupMenuItem.addActionListener(setupEvent);
                popup.add(setupMenuItem);
            }
            {
                ShutdownListener shutdownEvent = new ShutdownListener();
                MenuItem shutdownMenuItem = new MenuItem("Shut down RITA Server");
                shutdownMenuItem.addActionListener(shutdownEvent);
                popup.add(shutdownMenuItem);
            }
            m_Icon = new TrayIcon(image, "RITA Server is Starting: " + APP_URL, popup);
            m_Icon.setImageAutoSize(true);
            try {
                m_Tray.add(m_Icon);
            } catch (AWTException e) {
                JOptionPane.showMessageDialog(null, e.getMessage());
            }
        }
        try {
            m_Server.start();
        } catch (Exception e) {
            throw new ServerStartupFailed(e);
        }
        try {
            if (wac.getUnavailableException() != null) {
                log.error("Server shutting down because web application " + "failed to start: " + wac.getUnavailableException().toString());
                throw new ServerStartupFailed(wac.getUnavailableException());
            } else if (wac.isFailed()) {
                if (lifecycleExceptions.size() > 0) {
                    log.error("Server shutting down because web application " + "failed to start: Handler failed: " + lifecycleExceptions.get(0).toString());
                    throw new ServerStartupFailed(lifecycleExceptions.get(0));
                } else {
                    log.error("Server shutting down because web application " + "failed to start without recording an exception " + "(probably a listener failed to start)");
                    throw new ServerStartupFailed("No exception recorded " + "(probably a listener failed to start)");
                }
            }
        } catch (ServerStartupFailed e) {
            try {
                stop();
            } catch (Exception e2) {
                log.error("Failed to shut down the server after startup failed", e2);
            }
            throw e;
        }
        log.info("Server running and waiting for requests.");
        if (m_Icon != null) {
            Image image = Toolkit.getDefaultToolkit().getImage(RUNNING_IMG);
            m_Icon.setToolTip("RITA Server is Running: " + APP_URL);
            m_Icon.setImage(image);
        }
    }
