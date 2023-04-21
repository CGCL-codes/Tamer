    public void initialize(final PluginInterface _plugin_interface) {
        plugin_interface = _plugin_interface;
        logger = plugin_interface.getLogger().getChannel("NetStatus");
        logger.setDiagnostic();
        BasicPluginConfigModel config = plugin_interface.getUIManager().createBasicPluginConfigModel("Views.plugins." + VIEW_ID + ".title");
        ping_target = config.addStringParameter2("plugin.aznetstatus.pingtarget", "plugin.aznetstatus.pingtarget", "www.google.com");
        if (Constants.isCVSVersion()) {
            test_address = config.addStringParameter2("plugin.aznetstatus.test_address", "plugin.aznetstatus.test_address", "");
            test_button = config.addActionParameter2("test", "test ");
            test_button.setEnabled(false);
            test_button.addListener(new ParameterListener() {

                public void parameterChanged(Parameter param) {
                    protocol_tester.runTest(test_address.getValue().trim(), new NetStatusProtocolTesterListener() {

                        public void sessionAdded(NetStatusProtocolTesterBT.Session session) {
                        }

                        public void complete(NetStatusProtocolTesterBT tester) {
                        }

                        public void log(String str) {
                            logger.log(str);
                        }

                        public void logError(String str) {
                            logger.log(str);
                        }

                        public void logError(String str, Throwable e) {
                            logger.log(str, e);
                        }
                    });
                }
            });
        }
        plugin_interface.getUIManager().addUIListener(new UIManagerListener() {

            public void UIAttached(UIInstance instance) {
                if (instance instanceof UISWTInstance) {
                    UISWTInstance swt_ui = (UISWTInstance) instance;
                    NetStatusPluginView view = new NetStatusPluginView(NetStatusPlugin.this);
                    swt_ui.addView(UISWTInstance.VIEW_MAIN, VIEW_ID, view);
                }
            }

            public void UIDetached(UIInstance instance) {
            }
        });
        plugin_interface.addListener(new PluginListener() {

            public void initializationComplete() {
                new AEThread2("NetstatusPlugin:init", true) {

                    public void run() {
                        try {
                            protocol_tester = new NetStatusProtocolTester(NetStatusPlugin.this, plugin_interface);
                            if (test_button != null) {
                                test_button.setEnabled(true);
                            }
                        } finally {
                            protocol_tester_sem.releaseForever();
                        }
                    }
                }.start();
            }

            public void closedownInitiated() {
            }

            public void closedownComplete() {
            }
        });
    }
