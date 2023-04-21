    public void connect() {
        me.init(0, -2, -10.0f, 90.0f, 0.0f);
        if (m_MyTNetWrapper == null) {
            System.out.println("m_MyTNetWrapper == null");
            return;
        }
        if (!m_MyTNetWrapper.connect("65.23.155.44", 5050)) {
            System.out.println("TNetTCPWrapperTester - connect failed.");
            return;
        }
        m_MyTNetWrapper.login("Robert", "robertpwd");
    }
