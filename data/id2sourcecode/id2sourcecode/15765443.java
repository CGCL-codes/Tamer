    public synchronized void startup() throws Exception {
        if (log.isDebugEnabled()) log.debug("starting up SNMP service");
        ConfigurationService config = (ConfigurationService) ServiceRegistry.getInstance().getService(ConfigurationService.class);
        roCommunities = config.getStringArray("config/snmp/community[@type='readonly']");
        if (log.isDebugEnabled()) log.debug(roCommunities.length + " read only communities configured");
        rwCommunities = config.getStringArray("config/snmp/community[@type='readwrite']");
        if (log.isDebugEnabled()) log.debug(rwCommunities.length + " read write communities configured");
        snmp = new Snmp(new DefaultUdpTransportMapping());
        snmp.listen();
        if (log.isDebugEnabled()) log.debug("SNMP service started");
    }
