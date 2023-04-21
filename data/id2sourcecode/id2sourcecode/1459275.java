    public static void welcome(final Player player) {
        String msg = "This release is EXPERIMENTAL. Need help? #http://enyarok.sourceforge.net/wiki/index.php?title=AskForHelp - please report problems, suggestions and bugs. Remember to keep your password completely secret, never tell to another friend, player, or admin.";
        try {
            final Configuration config = Configuration.getConfiguration();
            if (config.has("server_welcome")) {
                msg = config.get("server_welcome");
                if (msg.startsWith("http://")) {
                    final URL url = new URL(msg);
                    HttpURLConnection.setFollowRedirects(false);
                    final HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    final BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    msg = br.readLine();
                    br.close();
                    connection.disconnect();
                }
            }
        } catch (final Exception e) {
            logger.warn("Can't read server_welcome from marauroa.ini", e);
        }
        if (msg != null) {
            player.sendPrivateText(msg);
        }
    }
