    public void start(BundleContext context) throws Exception {
        mainManagerServiceTracker = new ServiceTracker(context, XMPPMainManager.class.getName(), null);
        mainManagerServiceTracker.open();
        XMPPMainManager mainManager = (XMPPMainManager) mainManagerServiceTracker.getService();
        try {
            String serviceName = "gmail.com";
            mainManager.addConnectionListener(this);
            XMPPConnection connection = mainManager.createConnection(serviceName);
            Future future = connection.connect();
            future.complete();
            if ("gmail.com".equals(serviceName)) {
                connection.login("Noah.Shen87", "159357noah");
            } else if ("jabbercn.org".equals(serviceName)) {
                connection.login("Noah", "159357");
            } else if ("jabber.org".equals(serviceName)) {
                connection.login("NoahShen", "159357");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
