    public static void main(String[] args) {
        String host = "localhost";
        int port = 16007;
        ServerConnection sc = ServerConnection.getInstance();
        try {
            try {
                sc.connect(host, port);
            } catch (UnknownHostException uhe) {
                System.err.println("ERROR: Verbindung zum Server Fehlgeschlagen");
            }
            try {
                sc.login("root", "12345678");
            } catch (ServerException e) {
                System.err.println("Login fehlgeschlagen: " + e.getMessage());
            }
            try {
                for (int i = 0; i < 100; ++i) sc.createTable("Test Tisch Nr. " + i, 100, 20, 40);
            } catch (ServerException e) {
                System.err.println("Fehler beim Tisch anlegen: " + e.getMessage());
            }
            PokerTable[] pt = sc.getPokerTables();
            for (int i = 0; i < pt.length; ++i) System.out.println("[" + pt[i].getTableID() + "] Name: " + pt[i].getTableName() + "  Chips: " + pt[i].getStartChips() + " Blinds: " + pt[i].getSmallBlind() + " / " + pt[i].getBigBlind());
            try {
                sc.logout();
            } catch (ServerException e) {
                System.err.println("Logout fehlgeschlagen: " + e.getMessage());
            }
        } catch (IOException e) {
            System.err.println("ERROR: Ein-/Ausgabefehler");
        }
    }
