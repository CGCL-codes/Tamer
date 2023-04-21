    private static void informWebServer(String path) throws Exception {
        if (!Utils.parseBoolean(OGSserver.getProperty("Server.SaveInfo.InformWebServer", "no"))) return;
        URL url = new URL(OGSserver.getProperty("Server.SaveInfo.InformWebServer.URL") + URLEncoder.encode(path, "UTF-8"));
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.getInputStream().read();
        conn.disconnect();
    }
