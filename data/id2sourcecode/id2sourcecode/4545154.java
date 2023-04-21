    public void start() {
        LOG.info("Deploying SageAlert v2.x into Jetty plugin...");
        try {
            FileUtils.copyFileToDirectory(new File(RES_DIR, "SageAlert.war"), new File("jetty/webapps"), true);
            FileUtils.copyFileToDirectory(new File(RES_DIR, "SageAlert.context.xml"), new File("jetty/contexts"), false);
            LOG.info("Deployment successful!");
        } catch (IOException e) {
            LOG.fatal("Deployment failed!", e);
            throw new RuntimeException(e);
        }
        LOG.info("Registering all connected UI contexts...");
        DataStore ds = DataStore.getInstance();
        for (String clntIp : (String[]) ArrayUtils.addAll(API.apiNullUI.global.GetConnectedClients(), API.apiNullUI.global.GetUIContextNames())) {
            Client c = ds.getClient(clntIp);
            ds.registerClient(c.getId());
            LOG.info("Client id '" + c.getId() + "' registered!");
        }
    }
