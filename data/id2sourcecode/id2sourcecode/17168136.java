    public String getHash() throws NoSuchAlgorithmException, IOException {
        if (hash == null) {
            Map info = (Map) torrentMap.get("info");
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            hash = Torrent.bin2hex(md.digest(BEncoder.encode(info))).toUpperCase();
        }
        return hash;
    }
