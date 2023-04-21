    @Test
    public void executeAction() {
        MAService service = new MAServiceImpl();
        Settings connectionSettings = settings;
        String notificationChannel = "test-DOE-Paolo";
        try {
            service.startServiceInstance(builder, connectionSettings, notificationChannel);
        } catch (ServiceStartupException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        IManageabilityAgentFacade facade = service.getManagibilityAgentFacade(builder);
        ConfigurationFactoryImpl factory = new ConfigurationFactoryImpl();
        MonitoringSystemConfiguration msc = factory.createMonitoringSystemConfiguration();
        msc.setUuid(UUID.randomUUID().toString());
        Component[] components = new Component[1];
        Component c = factory.createComponent();
        c.setType("Sensor");
        DOESensorConfiguration config = new DOESensorConfiguration();
        config.setConfigurationId(UUID.randomUUID().toString());
        String serviceID = "paymentService";
        config.setServiceID(serviceID);
        String operationID = "/process/flow/receive[@name=$$ReceivePaymentRequest$$]";
        config.setOperationID(operationID);
        String status = "input";
        config.setStatus(status);
        String correlationKey = "cardNumber";
        config.setCorrelationKey(correlationKey);
        String correlationValue = "7777";
        config.setCorrelationValue(correlationValue);
        OutputReceiver[] newOutputReceivers = new OutputReceiverImpl[1];
        OutputReceiver receiver = new ConfigurationFactoryImpl().createOutputReceiver();
        receiver.setEventType("event");
        receiver.setUuid("tcp:localhost:10000");
        newOutputReceivers[0] = receiver;
        config.setOutputReceivers(newOutputReceivers);
        ComponentConfiguration[] configs = new ComponentConfiguration[1];
        configs[0] = config;
        c.setConfigurations(configs);
        components[0] = c;
        msc.setComponents(components);
        facade.configureMonitoringSystem(msc);
        System.out.println("[DOE- facade] Added Monitoring System Configuration to DOE");
        assertTrue(true);
        List<SensorSubscriptionData> subDatas = facade.getSensorSubscriptionData();
        System.out.println("[DOE- facade] Got SensorSubscriptionData from DOE");
        assertTrue(true);
        for (SensorSubscriptionData subData : subDatas) {
            System.out.println("[DOE - facade] Sensor Subscription ID: " + subData.getSensorID().toString());
            for (String s : subData.getChannels()) {
                System.out.println("[DOE - facade] Sensor Subscription on channel: " + s);
            }
        }
        facade.deconfigureMonitoring();
        System.out.println("[DOE- facade] Removed Monitoring System Configuration from DOE");
        assertTrue(true);
    }
