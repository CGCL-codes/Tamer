    public void setNetwork(int spieler, String command) {
        InputStream is = null;
        try {
            URL server = new URL("http://" + GetServer.serverName + ":" + String.valueOf(GetServer.serverPort));
            String path;
            if (spieler == DiceField.RED) path = GetNetworkGame.gameName + "/red"; else path = GetNetworkGame.gameName + "/green";
            URL url = new URL(server, path + "?" + command);
            System.out.println("Anfrage: http://" + GetServer.serverName + ":" + String.valueOf(GetServer.serverPort) + "/" + path + "?" + command);
            is = url.openStream();
            System.out.println("Antwort Netzwerk: " + new Scanner(is).useDelimiter("\n").next());
        } catch (MalformedURLException e) {
            new PopUpWindow("Serverfehler", "Server nicht erreichbar.");
        } catch (IOException e) {
            new PopUpWindow("Clientfehler", "Kein Mitspieler gefunden.");
        } finally {
            if (is != null) try {
                is.close();
            } catch (IOException e) {
            }
        }
    }
