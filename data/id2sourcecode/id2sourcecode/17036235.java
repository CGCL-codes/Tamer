    public static ConnectionConfiguration getConnectionConfiguration() {
        if (connectionConfiguration == null) {
            connectionConfiguration = new ConnectionConfiguration();
            DriverDescriptorPluginReader r2 = new DriverDescriptorPluginReader();
            r2.readDriverDescriptors(connectionConfiguration);
            ConnectionProfileXMLReader r = new ConnectionProfileXMLReader();
            try {
                URL url = new URL(Platform.getPlugin("net.sourceforge.ecldbtool.connect").getDescriptor().getInstallURL(), "ConnectionConfiguration.xml");
                r.setInputStream(url.openConnection().getInputStream());
            } catch (Throwable e) {
                System.out.println("Using hardcoded path");
                try {
                    r.setInputStream(new java.io.FileInputStream("C:/Program Files/oti/eclipse/plugins/net.sourceforge.ecldbtool.connect/ConnectionConfiguration.xml"));
                } catch (FileNotFoundException e2) {
                    e2.printStackTrace();
                }
            }
            r.readConnectionProfiles(connectionConfiguration);
        }
        return connectionConfiguration;
    }
