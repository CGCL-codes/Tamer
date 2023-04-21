    public static void main(String[] args) throws Exception {
        if (args.length != 1) throw new Exception("Falta archivo");
        String path = args[0];
        InputStream stream = null;
        try {
            File torrentFile = new File(path);
            stream = new FileInputStream(torrentFile);
            BEDecoder decoder = new BEDecoder(stream);
            BEValue torrent = decoder.decode();
            Map<String, BEValue> metainfo = torrent.mapValue();
            BEValue info = metainfo.get("info");
            MessageDigest digest = MessageDigest.getInstance("SHA");
            byte[] infohash = digest.digest(new BEEncoder().encode(info));
            System.out.println("FILENAME: " + torrentFile.getName());
            System.out.println(metainfo);
            System.out.println("INFOHASH: " + hexencode(infohash));
        } finally {
            if (stream != null) stream.close();
        }
    }
