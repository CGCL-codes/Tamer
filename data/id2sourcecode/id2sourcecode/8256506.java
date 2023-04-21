    public void initServer() throws IOException {
        conf = new ConfigManager();
        try {
            conf.initConf();
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
            System.exit(-1);
        }
        POLICY_PATH = conf.getFolderConf().getRoot() + "/" + conf.getFolderConf().getPolicies();
        LOG_PATH = conf.getFolderConf().getRoot() + "/" + conf.getFolderConf().getLogs() + "/" + (new SimpleDateFormat("yyyy-MM-dd-")).format(new Date()) + "log.txt";
        System.setProperty("java.security.krb5.kdc", conf.getKerberosServer());
        System.setProperty("java.security.auth.login.config", conf.getJaasConf().getJaasConfFilename());
        String princName = conf.getKerberosPrinc();
        System.setProperty("java.security.krb5.realm", princName.split("@")[1]);
        String princPass = conf.getPrincipalPasswd();
        try {
            lc = new LoginContext(conf.getJaasConf().getJaasRule(), new AuthHandler(princName, princPass));
            lc.login();
        } catch (LoginException e) {
            System.err.println("Service authentication failed!");
            System.exit(-1);
        }
        System.out.println("Service authentication succedeed");
        FilePolicyModule policyModule = new FilePolicyModule();
        Set<FilePolicyModule> policyModules = new HashSet<FilePolicyModule>();
        PolicyFinder policyFinder = new PolicyFinder();
        policyModule.addPolicy(POLICY_PATH + "/" + POLICY_FILE);
        policyModules.add(policyModule);
        policyFinder.setModules(policyModules);
        CurrentEnvModule envMod = new CurrentEnvModule();
        AttributeFinder attrFinder = new AttributeFinder();
        List<CurrentEnvModule> attrModules = new ArrayList<CurrentEnvModule>();
        attrModules.add(envMod);
        attrFinder.setModules(attrModules);
        pdp = new PDP(new PDPConfig(attrFinder, policyFinder, null));
        USER_ROOT_DIR = conf.getFolderConf().getRoot() + "/" + conf.getFolderConf().getUser() + "/";
        String folders[] = new File(USER_ROOT_DIR).list();
        managers = new ResourceManager[folders.length];
        for (int i = 0; i < managers.length; i++) {
            if (!folders[i].startsWith(".")) managers[i] = new ResourceManager(conf.getFolderConf().getRoot() + "/" + conf.getFolderConf().getConfig() + "/", folders[i], pdp);
        }
        System.out.println("PDP and managers initialized");
        try {
            FileHandler fh = new FileHandler(LOG_PATH, true);
            fh.setFormatter(new SimpleFormatter());
            logger = Logger.getLogger("StrongboxServer");
            logger.addHandler(fh);
            logLevel = conf.getLogger().getSecureLevel();
            if (logLevel != 0) {
                LoggerConf lc = conf.getLogger();
                mailOnLog = lc.isAdminMail();
                rl = new RemoteLogger(this.lc.getSubject());
                try {
                    rl.connect(lc.getLoggerServer(), lc.getLoggerPort(), lc.getLoggerService());
                } catch (IOException e1) {
                    logger.warning(e1.getMessage());
                    System.exit(-1);
                }
            }
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (GSSException e) {
            e.printStackTrace();
        }
        ipManager = new IPManager(conf.getIPConf().getNTriesBan(), conf.getIPConf().getBanTimeM(), conf.getIPConf().isAdminMail());
        mailOnBan = ipManager.mailEnabled();
        portManager = new PortManager(conf.getPortConf().getBase(), conf.getPortConf().getRange());
        if (ipManager.mailEnabled() || mailOnLog) emergencyMail = new StrongMail(conf.getMailAddress(), LOG_PATH, conf.getMailPasswd(), conf.getMailServer());
    }
