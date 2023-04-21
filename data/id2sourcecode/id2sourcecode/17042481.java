    @Override
    protected void setUp() {
        try {
            telnet.connect(host.toInetAddress(), port);
            telnet.login(username, passwd);
            System.err.println("Telnet connection established.");
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
    }
