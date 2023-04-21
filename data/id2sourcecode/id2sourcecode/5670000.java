        @Override
        public Playlist construct() throws Exception {
            InputStream in = url.openStream();
            try {
                return Playlist.parse(in);
            } finally {
                in.close();
            }
        }
