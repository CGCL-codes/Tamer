    private void initData() {
        artists = new HashMap();
        InputStream is = this.getClass().getClassLoader().getResourceAsStream("org/richfaces/demo/tree/data.txt");
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        byte[] rb = new byte[1024];
        int read;
        try {
            do {
                read = is.read(rb);
                if (read > 0) {
                    os.write(rb, 0, read);
                }
            } while (read > 0);
            String buf = os.toString();
            StringTokenizer toc1 = new StringTokenizer(buf, "\n");
            while (toc1.hasMoreTokens()) {
                String str = toc1.nextToken();
                StringTokenizer toc2 = new StringTokenizer(str, "\t");
                String songTitle = toc2.nextToken();
                String artistName = toc2.nextToken();
                String albumTitle = toc2.nextToken();
                toc2.nextToken();
                toc2.nextToken();
                String albumYear = toc2.nextToken();
                Artist artist = getArtistByName(artistName, this);
                Album album = getAlbumByTitle(albumTitle, artist);
                album.setYear(new Integer(albumYear));
                Song song = new Song(getNextId());
                song.setTitle(songTitle);
                album.addSong(song);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
