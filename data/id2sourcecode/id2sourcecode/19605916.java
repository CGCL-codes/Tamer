    public static Track track(String id) throws Exception {
        String url = "http://musicbrainz.org/ws/1/track/" + id + "?type=xml&inc=artist+puids";
        HttpURLConnection conn = (HttpURLConnection) (new URL(url)).openConnection();
        XmlNode node = XmlLoader.load(conn.getInputStream());
        node = node.getFirstChild("track");
        String title = node.getFirstChild("title").getText();
        Track track = new Track();
        track.id = id;
        track.name = title;
        XmlNode artist = node.getFirstChild("artist");
        track.artist = new Artist();
        track.artist.id = artist.getAttribute("id");
        track.artist.name = artist.getFirstChild("name").getText();
        track.puids = new ArrayList();
        try {
            XmlNode puids[] = node.getFirstChild("puid-list").getChild("puid");
            for (int i = 0; i < puids.length; i++) {
                track.puids.add(puids[i].getAttribute("id"));
            }
        } catch (NullPointerException e) {
        }
        return track;
    }
