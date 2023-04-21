    private static void informWebServer(String path) throws IOException {
        if (!Utils.parseBoolean(System.getProperty("Server.SaveInfo.InformWebServer", "no"))) return;
        URL url = new URL(System.getProperty("Server.SaveInfo.InformWebServer.URL") + URLEncoder.encode(path, "UTF-8"));
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.getInputStream().read();
        conn.disconnect();
    }
