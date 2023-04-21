    public static Release releaseNonCache(String id) throws Exception {
        String url = "http://musicbrainz.org/ws/1/release/" + id + "?type=xml&inc=tracks";
        HttpURLConnection conn = (HttpURLConnection) (new URL(url)).openConnection();
        XmlNode node = XmlLoader.load(conn.getInputStream());
        node = node.getFirstChild("release");
        String title = node.getFirstChild("title").getText();
        Release release = new Release();
        XmlNode asin = node.getFirstChild("asin");
        if (asin != null) release.asin = asin.getText();
        release.id = id;
        release.name = title;
        release.tracks = new ArrayList();
        XmlNode releases[] = node.getFirstChild("track-list").getChild("track");
        System.out.println(releases.length);
        for (int i = 0; i < releases.length; i++) {
            String rid = releases[i].getAttribute("id");
            String ttitle = releases[i].getFirstChild("title").getText();
            String tduration = releases[i].getFirstChild("duration").getText();
            Track track = new Track();
            track.id = rid;
            track.name = ttitle;
            if (tduration != null && tduration.length() > 1) track.duration = Integer.parseInt(tduration) / 1000;
            release.tracks.add(track);
        }
        return release;
    }
