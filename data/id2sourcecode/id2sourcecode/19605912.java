    public static Artist artistAux(String id, String q) throws Exception {
        String url = "http://musicbrainz.org/ws/1/artist/" + id + "?type=xml&inc=" + q;
        HttpURLConnection conn = (HttpURLConnection) (new URL(url)).openConnection();
        XmlNode node = XmlLoader.load(conn.getInputStream());
        node = node.getFirstChild("artist");
        Artist artist = new Artist();
        artist.id = id;
        artist.name = node.getFirstChild("name").getText();
        artist.album = new ArrayList();
        HashSet set = new HashSet();
        try {
            XmlNode releases[] = node.getFirstChild("release-list").getChild("release");
            for (int i = 0; i < releases.length; i++) {
                String rid = releases[i].getAttribute("id");
                String title = releases[i].getFirstChild("title").getText();
                Release release = new Release();
                release.id = rid;
                release.name = title;
                artist.album.add(release);
                set.add(title);
            }
        } catch (NullPointerException e) {
        }
        return artist;
    }
