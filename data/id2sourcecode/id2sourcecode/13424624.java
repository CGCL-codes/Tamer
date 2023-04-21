    private Map<Object, Object> trackerRequest(byte[] hash, int port) throws MalformedURLException, IOException {
        String sUrl = tracker + "?info_hash=" + new String(new URLCodec().encode(hash)) + "&port=" + port + "&compact=1&peer_id=" + new String(new URLCodec().encode(peerId)) + "&uploaded=0&downloaded=0&left=100";
        URL url = new URL(sUrl);
        PushbackInputStream in = new PushbackInputStream(url.openStream());
        return (Map<Object, Object>) Bencode.parseBencode(in);
    }
