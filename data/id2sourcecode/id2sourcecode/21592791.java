    @BeforeClass
    public static void messageSenderInitiation() throws Exception {
        connection = new XMPPConnectionService();
        try {
            connection.connect(TestAccount.server, TestAccount.port);
            connection.login(TestAccount.username, TestAccount.password);
        } catch (Exception e) {
            e.printStackTrace();
        }
        service = connection.chat("gustavo.cpii.ufrj@gmail.com");
        context = new JUnit4Mockery();
    }
