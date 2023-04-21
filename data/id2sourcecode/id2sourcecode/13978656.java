        protected downloadData(Download download) {
            Torrent t = download.getTorrent();
            if (t != null) {
                byte[] hash = t.getHash();
                SHA1 sha1 = new SHA1();
                sha1.update(ByteBuffer.wrap(IV));
                sha1.update(ByteBuffer.wrap(hash));
                id = new HashWrapper(sha1.digest());
            }
        }
