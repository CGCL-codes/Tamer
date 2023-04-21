    public EgaChannelSender() throws AxisFault {
        options = new Options();
        configContext = null;
        Properties channelProperties = EgaChannelManager.getChannelProperties();
        addUrl = channelProperties.getProperty(EgaChannelLabel.getProxyAddress());
        topicns = channelProperties.getProperty(EgaChannelLabel.getEventTopicNS());
        action = EgaChannelLabel.getEventUrn();
        String repoPath = channelProperties.getProperty(EgaChannelLabel.getRepositoryAxis2Path());
        String repoAxis2xml = repoPath + File.separator + "conf" + File.separator + "axis2.xml";
        AxisConfigurator configurator = null;
        try {
            configurator = new FileSystemConfigurator(repoPath, repoAxis2xml);
        } catch (AxisFault e1) {
            e1.printStackTrace();
        }
        ConfigurationContext configuratorContext = null;
        try {
            configuratorContext = ConfigurationContextFactory.createConfigurationContext(configurator);
        } catch (AxisFault e1) {
            e1.printStackTrace();
            throw e1;
        }
        try {
            if (serviceClient == null) {
                serviceClient = new ServiceClient(configuratorContext, null);
            }
            serviceClient.engageModule("addressing");
        } catch (AxisFault e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
