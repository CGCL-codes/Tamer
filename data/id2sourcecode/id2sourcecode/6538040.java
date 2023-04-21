        @Override
        public void start(Gamio gamio, Properties properties) throws Exception {
            log.info("Starting Gamio...");
            log.info("Version: ", properties.getProperty(PRODUCT_VERSION), " (", properties.getProperty(VENDOR_NAME), ")");
            gamio.initEnv(properties);
            Configuration configuration = gamio.loadConfiguration();
            InstProps instProps = configuration.getInstProps();
            log.info("Instance System Name: ", instProps.getName());
            log.info("Instance System ID: ", instProps.getId());
            gamio.initContext(configuration);
            Context context = Context.getInstance();
            context.setTimer(new Timer(true));
            context.getWorkshop().start();
            context.getProcessorManager().startAllProcessors();
            context.getChannelManager().start();
            context.getClientManager().startAllClients();
            context.getServerManager().start();
            context.getServerManager().startAllServers();
            context.getMessageQueue().start();
            gamio.changeState(GamioStarted.getInstance());
            log.info("Gamio[name<", instProps.getName(), ">, id<", instProps.getId(), ">] started");
        }
