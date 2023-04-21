    public boolean sendInfo(int LocalGameServerPort) {
        this.GameServerPort = LocalGameServerPort;
        URL url = null;
        System.out.println("Connecting to Game server Manager...");
        System.out.flush();
        try {
            url = new URL(CentralWebServer.concat("?hostname=" + this.LocalGameServer + "&port=9670&location=Kathmandu&gameservername=iSnake Game Server"));
            URLConnection connection = url.openConnection();
            connection.setConnectTimeout(3000);
            connection.connect();
            connection.getContent();
            System.out.println("Connected to Game Server Manager: [" + this.CentralWebServer + "]");
            System.out.println("Nourishing with following info:\n\t hostname=" + this.LocalGameServer + "\n\t port=9670&location=Dhulikhel\n\t gameservername=Sata");
            return true;
        } catch (IOException ex) {
            System.out.println("Cannot connect to Game Server Manager: [" + this.CentralWebServer + "]");
            return false;
        }
    }
