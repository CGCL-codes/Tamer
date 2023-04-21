    public void start(BundleContext context) throws Exception {
        mainManagerServiceTracker = new ServiceTracker(context, XmppMainManager.class.getName(), null);
        mainManagerServiceTracker.open();
        XmppMainManager mainManager = (XmppMainManager) mainManagerServiceTracker.getService();
        String serviceName = "gmail.com";
        XmppConnection connection = mainManager.createConnection(serviceName);
        Future future = connection.connect();
        future.complete();
        String username = null;
        String password = null;
        if ("gmail.com".equals(serviceName)) {
            username = "Noah.Shen87";
            password = "159357noah";
        } else if ("jabbercn.org".equals(serviceName)) {
            username = "Noah";
            password = "159357";
        } else if ("jabber.org".equals(serviceName)) {
            username = "NoahShen";
            password = "159357";
        }
        Future futureLogin = connection.login(username, password);
        futureLogin.complete();
        testFileTransfer(connection, context);
    }
