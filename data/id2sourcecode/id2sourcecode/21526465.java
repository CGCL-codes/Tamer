    static void welcome(Player player) {
        String msg = "This is release " + Debug.VERSION + " of Midhedava. Please report problems, suggestions and bugs at www.midhedava.org. Note: remember to keep your password completely secret, never tell it to another friend, player, or even admin.";
        try {
            Configuration config = Configuration.getConfiguration();
            if (config.has("server_welcome)")) {
                msg = config.get("server_welcome");
                if (msg.startsWith("http://")) {
                    URL url = new URL(msg);
                    HttpURLConnection.setFollowRedirects(false);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    msg = br.readLine();
                    br.close();
                    connection.disconnect();
                }
            }
        } catch (Exception e) {
            if (PlayerRPClass.firstWelcomeException) {
                logger.warn("Can't read server_welcome from marauroa.ini", e);
                PlayerRPClass.firstWelcomeException = false;
            }
        }
        if (msg != null) {
            player.sendPrivateText(msg);
        }
    }
