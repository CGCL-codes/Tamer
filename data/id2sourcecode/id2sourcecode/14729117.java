    @Test
    public void connectDisconnectAvailableProjects() throws QcException {
        log.info("Testing connection/disconnection to all available projects.");
        final IQcConnection lConnection = QcConnectionFactory.createConnection(PROPERTIES.getProperty(IArgsConstants.SERVER));
        lConnection.login(PROPERTIES.getProperty(IArgsConstants.USER), PROPERTIES.getProperty(IArgsConstants.PASSWORD));
        final List<Domain> lDomainList = lConnection.getDomainList();
        lConnection.disconnect();
        for (final Domain lDomain : lDomainList) {
            log.info("Connection to domain " + lDomain.getName());
            for (final Project lPrj : lDomain.getProjects()) {
                log.info("Connection to project " + lPrj.getName());
                lConnection.connect(PROPERTIES.getProperty(IArgsConstants.USER), PROPERTIES.getProperty(IArgsConstants.PASSWORD), lDomain.getName(), lPrj.getName());
                sleep(1);
                lConnection.disconnect();
                log.info("==> OK");
            }
        }
        log.info("OK");
    }
