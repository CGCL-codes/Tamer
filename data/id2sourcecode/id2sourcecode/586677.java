    public String getNetwork(int spieler) {
        InputStream is = null;
        String command = "";
        try {
            URL server = new URL("http://" + GetServer.serverName + ":" + String.valueOf(GetServer.serverPort));
            String path;
            if (spieler == DiceField.RED) path = GetNetworkGame.gameName + "/red"; else path = GetNetworkGame.gameName + "/green";
            URL url = new URL(server, path);
            System.out.println("Anfrage: http://" + GetServer.serverName + ":" + String.valueOf(GetServer.serverPort) + "/" + path);
            is = url.openStream();
            command = new Scanner(is).useDelimiter(" ").next();
            System.out.println("Antwort Netzwerk: " + command);
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
        return command;
    }
