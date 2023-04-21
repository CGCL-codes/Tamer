    public void start(final BundleContext context) throws Exception {
        config = new Config(FrameworkImpl.INSTANCE.getConfig(), ResourceBundle.getBundle("Bundle"));
        config.load(context.getBundle().getResource("module.properties").openStream());
        log = new LogUtil(context, Core.class.getName(), config);
        this.bundleContext = context;
        File file = bundleContext.getDataFile(CoreConstants.INSTANCE_LOCK);
        if (!file.exists()) file.createNewFile();
        RandomAccessFile lockFile = new RandomAccessFile(file, "rw");
        FileChannel channel = lockFile.getChannel();
        lock = channel.tryLock();
        if (lock == null) {
            log.error(config.getResourceString(CoreConstants.KEY_ERRINSTANCE));
            FrameworkImpl.INSTANCE.stop();
            return;
        }
        context.addBundleListener(bundleListener);
        context.addFrameworkListener(frameworkListener);
        packageAdmin = new PackageAdminImpl(context);
        eventAdmin = new EventAdminImpl(context);
        preferencesService = new PreferencesServiceImpl(context);
        configurationAdmin = new ConfigurationAdminImpl(context);
        peerAdmin = new PeerAdminImpl(context);
        ServiceFactory factory = new ServiceFactory() {

            public Object getService(final Bundle bundle, ServiceRegistration registration) {
                return new BundleServiceImpl() {

                    public File openConfigFile(String name) {
                        return FrameworkImpl.INSTANCE.getConfigFile(bundle, name);
                    }

                    public void addClasspathURL(URL url) {
                        BundleImpl bundleImpl = (BundleImpl) bundle;
                        BundleRegistryImpl reg = (BundleRegistryImpl) FrameworkImpl.INSTANCE.getBundleRegistry();
                        reg.addUrl(bundleImpl.getBundleClassLoader(), url);
                    }

                    public Config createConfig(ResourceBundle resBundle) {
                        return new Config(config, resBundle);
                    }

                    public void addIdleEventListener(IdleEventListener listener) throws FrameworkException {
                        BundleImpl bundleImpl = (BundleImpl) bundle;
                        FrameworkImpl.INSTANCE.addIdleEventListener(listener, bundleImpl.getBundleClassLoader());
                    }
                };
            }

            public void ungetService(Bundle bundle, ServiceRegistration registration, Object service) {
            }
        };
        context.registerService(BundleService.class.getName(), factory, null);
        Dictionary properties;
        properties = new Hashtable();
        properties.put(JIMOConstants.SERVICE_COMMANDNAME, CoreConstants.HELP_COMMANDNAME);
        context.registerService(CommandHandler.class.getName(), new CommandHandler() {

            public void onCommand(String command, CommandContext context) {
                String helpText = getResourceString(CoreConstants.KEY_COMMANDHELP);
                context.print(helpText);
                context.newLine();
            }
        }, properties);
        properties = new Hashtable();
        properties.put(JIMOConstants.SERVICE_COMMANDNAME, CoreConstants.CORE_COMMANDNAME);
        context.registerService(CommandHandler.class.getName(), new CoreCommandHandler(), properties);
        properties = new Hashtable();
        properties.put(JIMOConstants.SERVICE_COMMANDNAME, CoreConstants.DISCONNECT_COMMANDNAME);
        context.registerService(CommandHandler.class.getName(), new CommandHandler() {

            public void onCommand(String command, CommandContext context) {
                context.close();
            }
        }, properties);
        properties = new Hashtable();
        properties.put(JIMOConstants.SERVICE_COMMANDNAME, CoreConstants.LIST_COMMANDNAME);
        context.registerService(CommandHandler.class.getName(), new CommandHandler() {

            public void onCommand(String command, CommandContext context) {
                ServiceReference[] commands = CommandProcessor.getCommands(getBundleContext());
                for (int i = 0; i < commands.length; i++) {
                    String name = (String) commands[i].getProperty(JIMOConstants.SERVICE_COMMANDNAME);
                    context.println(name);
                }
            }
        }, properties);
        Event event = FrameworkImpl.newEvent(true);
        Listener[] listeners = new Listener[] { new Listener() {

            public void onEvent(Event ev) throws FrameworkException {
                logTracker = new ServiceTracker(context, LogService.class.getName(), null);
                logTracker.open();
            }
        }, new Listener() {

            public void onEvent(Event ev) throws FrameworkException {
                daemonTracker = new ServiceTracker(bundleContext, Daemon.class.getName(), new ServiceTrackerCustomizer() {

                    public Object addingService(ServiceReference reference) {
                        return startDaemon(reference);
                    }

                    public void modifiedService(ServiceReference reference, Object service) {
                    }

                    public void removedService(ServiceReference reference, Object service) {
                        Thread thread = (Thread) mapDaemons.get(service);
                        if (thread != null && thread.isAlive()) thread.interrupt();
                        mapDaemons.remove(service);
                    }
                });
                daemonTracker.open();
                ServiceReference[] serviceReferences = daemonTracker.getServiceReferences();
                if (serviceReferences != null) {
                    for (int i = 0; i < serviceReferences.length; i++) {
                        ServiceReference reference = serviceReferences[i];
                        startDaemon(reference);
                    }
                }
            }
        }, new Listener() {

            public void onEvent(Event ev) throws FrameworkException {
                final String appId = bundleContext.getProperty(JIMOConstants.KEY_APPLICATION);
                if (appId != null && !"".equals(appId)) {
                    Filter filter = null;
                    try {
                        filter = context.createFilter("(& (" + JIMOConstants.SERVICE_APPID + "=" + appId + ") (" + Constants.OBJECTCLASS + "=" + Application.class.getName() + ") )");
                    } catch (InvalidSyntaxException e1) {
                        log.error(e1);
                    }
                    applicationTracker = new ServiceTracker(context, filter, new ServiceTrackerCustomizer() {

                        public Object addingService(ServiceReference reference) {
                            log.info(config.format(config.getResourceString(CoreConstants.KEY_STARTINGAPPLICATION), new Object[] { appId }));
                            Application app = (Application) context.getService(reference);
                            launchApp(reference, app);
                            return app;
                        }

                        public void modifiedService(ServiceReference reference, Object service) {
                        }

                        public void removedService(ServiceReference reference, Object service) {
                            String serviceAppId = (String) reference.getProperty(JIMOConstants.SERVICE_APPID);
                            if (!appId.equals(serviceAppId)) return;
                            if (appMain != null && appMain.isAlive()) {
                                appMain.interrupt();
                                try {
                                    appMain.join();
                                } catch (InterruptedException e) {
                                    Thread.currentThread().interrupt();
                                }
                                appMain = null;
                            }
                        }
                    });
                    applicationTracker.open();
                }
            }
        }, new Listener() {

            public void onEvent(Event ev) throws FrameworkException {
                boolean consoleMode = new Boolean(context.getProperty(JIMOConstants.KEY_CONSOLE)).booleanValue();
                if (consoleMode) {
                    context.registerService(new String[] { Daemon.class.getName(), CommandProcessor.class.getName() }, new CommandProcessor(System.in, System.err, bundleContext, COMMANDPROCESSOR, true), null);
                }
                String host = context.getProperty(JIMOConstants.KEY_SHELLHOST);
                if (host != null && !"".equals(host)) {
                    try {
                        int port = Integer.parseInt(context.getProperty(JIMOConstants.KEY_SHELLPORT));
                        ServerSocket socket = new ServerSocket();
                        SocketAddress endpoint = new InetSocketAddress(host, port);
                        socket.bind(endpoint);
                        ServerSocketShell shell = new ServerSocketShell(socket, context);
                        context.registerService(Daemon.class.getName(), shell, null);
                    } catch (Throwable e) {
                        log.error(e);
                    }
                }
            }
        }, new Listener() {

            public void onEvent(Event ev) throws FrameworkException {
                File file = bundleContext.getDataFile(CoreConstants.BUNDLE_INSTALLFILE);
                if (file.exists()) installBundles(file); else persistBundleState();
                if (!BundleClassLoaderImpl.resolve()) {
                    log.warn(CoreConstants.KEY_UNRESOLVEDIMPORTS);
                }
                FrameworkImpl.INSTANCE.getBundleRegistry().startPendingBundles();
                try {
                    FrameworkImpl.INSTANCE.sendFrameworkEvent(FrameworkEvent.STARTED, bundleContext.getBundle(), null);
                } catch (FrameworkException e) {
                    log.error(e);
                }
            }
        } };
        event.addListeners(listeners);
        FrameworkImpl.fireEvent(event);
        EventHandler handler = new EventHandler() {

            CommandContext commandContext = new CommandContext(null, System.err, false);

            CommandProcessor commandProcessor = new CommandProcessor(commandContext, bundleContext, "EVENT");

            public void handleEvent(org.osgi.service.event.Event event) {
                String cmd = (String) event.getProperty(CoreConstants.EVENT_COMMAND);
                String args = (String) event.getProperty(CoreConstants.EVENT_COMMAND);
                commandProcessor.process(cmd, args);
            }
        };
        properties = new Hashtable();
        properties.put(EventConstants.EVENT_TOPIC, new String[] { CoreConstants.TOPIC_COMMANDEXEC });
        context.registerService(EventHandler.class.getName(), handler, properties);
    }
