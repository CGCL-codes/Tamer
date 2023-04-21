    public static void main(String[] args) {
        TNetTCPWrapperTester tntcpwt = new TNetTCPWrapperTester();
        if (tntcpwt.m_MyTNetWrapper == null) {
            System.out.println("TNetTCPWrapperTester - m_MyTNetWrapper == null");
            return;
        }
        if (!tntcpwt.m_MyTNetWrapper.connect("65.23.155.44", 5050)) {
            System.out.println("TNetTCPWrapperTester - connect failed.");
            return;
        }
        tntcpwt.m_MyTNetWrapper.login("Bill", "billpwd");
        tntcpwt.m_MyTNetWrapper.login("Robert", "robert");
        tntcpwt.m_MyTNetWrapper.login("Jenny", "jennypwd");
    }
