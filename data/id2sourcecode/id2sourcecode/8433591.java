    public static String getExternalIP() {
        String externalIP = null;
        try {
            XmlParser xmlParser;
            URL url;
            HttpURLConnection conn;
            String currentIP;
            url = new URL("http://checkip.dyndns.org/");
            conn = (HttpURLConnection) url.openConnection();
            conn.connect();
            xmlParser = new XmlParser(new InputStreamReader(conn.getInputStream(), "8859_1"));
            xmlParser.openNode("html");
            xmlParser.openNode("head");
            xmlParser.openNode("title");
            xmlParser.match("Current IP Check");
            xmlParser.closeNode("title");
            xmlParser.closeNode("head");
            xmlParser.openNode("body");
            currentIP = xmlParser.readString();
            xmlParser.closeNode("body");
            xmlParser.closeNode("html");
            xmlParser.close();
            externalIP = (currentIP.split(": "))[1];
        } catch (Exception e) {
            StringBuffer error = new StringBuffer("Error obtaining external ip:");
            error.append(e.getMessage());
            Utilities.popUp(error.toString());
            e.printStackTrace();
        }
        return externalIP;
    }
