    public static ArrayList searchArtist(String query) throws Exception {
        ArrayList ret = new ArrayList();
        String url = "http://musicbrainz.org/ws/1/artist/?type=xml&name=" + query;
        HttpURLConnection conn = (HttpURLConnection) (new URL(url)).openConnection();
        XmlNode node = XmlLoader.load(conn.getInputStream());
        node = node.getFirstChild("artist-list");
        if (node != null) {
            XmlNode tracks[] = node.getChild("artist");
            for (int i = 0; i < tracks.length; i++) {
                String id = tracks[i].getAttribute("id");
                String name = tracks[i].getFirstChild("name").getText();
                Artist artist = new Artist();
                artist.id = id;
                artist.name = name;
                ret.add(artist);
            }
        }
        return ret;
    }
