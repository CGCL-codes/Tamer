    private static void doConnect() throws Exception {
        System.out.println("[BNET] Beginning connection...");
        bncs.connect(new InetSocketAddress("localhost", 6112)).get();
        System.out.println("[BNET] Connection complete.");
        System.out.println("[BNET] Beginning login...");
        bncs.login("WTSBot", "password", true).get();
        System.out.println("[BNET] Login complete.");
        System.out.println("[BNET] Joining channel...");
        bncs.join("W3").get();
        System.out.println("[BNET] Join channel complete.");
    }
