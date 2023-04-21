    public static void main(String args[]) throws Exception {
        ProviderManager.getInstance().addIQProvider(SipAccountPacket.ELEMENT_NAME, SipAccountPacket.NAMESPACE, new SipAccountPacket.Provider());
        ProviderManager.getInstance().addIQProvider(LogPacket.ELEMENT_NAME, LogPacket.NAMESPACE, new LogPacket.Provider());
        XMPPConnection.DEBUG_ENABLED = true;
        final XMPPConnection con = new XMPPConnection("anteros");
        con.connect();
        con.login("demo", "demo");
        SipAccountPacket.getSipSettings(con);
        System.out.println("HELLO");
    }
