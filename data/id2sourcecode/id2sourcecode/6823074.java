    public Tracker(Torrent torrent, Client client) {
        this.torrent = torrent;
        this.client = client;
        parameters = new HashMap<String, Object>();
        parameters_order = new LinkedList<String>();
        this.running = false;
        BEncSHA1 bencsha1 = new BEncSHA1();
        addParameter("info_hash", bencsha1.digest(torrent.getInfo()).getSHA1());
        addParameter("peer_id", client.getPeerID());
        addParameter("port", 8000);
        addParameter("uploaded", 0);
        addParameter("downloaded", 0);
        addParameter("left", 0);
        addParameter("compact", "disabled");
        addParameter("no_peer_id", false);
        addParameter("event", "");
    }
