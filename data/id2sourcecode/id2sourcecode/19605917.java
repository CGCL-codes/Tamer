    public static String puidToMbid(String id) throws Exception {
        String url = "http://musicbrainz.org/ws/1/track/?type=xml&puid=" + id;
        HttpURLConnection conn = (HttpURLConnection) (new URL(url)).openConnection();
        XmlNode node = XmlLoader.load(conn.getInputStream());
        node = node.getFirstChild("track-list");
        if (node != null) {
            node = node.getFirstChild("track");
            if (node != null) {
                return node.getAttribute("id");
            }
        }
        return "00000000-0000-0000-0000-000000000000";
    }
