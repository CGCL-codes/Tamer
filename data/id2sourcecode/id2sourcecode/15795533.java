    private void dump(ScanCollectionState scs) throws MusicCollectionException {
        Connection conn = null;
        Statement stmt = null;
        PreparedStatement pstmtAuthors = null;
        PreparedStatement pstmtAlbums = null;
        PreparedStatement pstmtSongs = null;
        int forEachCount;
        scs.ccb.processStarted("Guardando la colección");
        try {
            conn = getConnection();
            stmt = conn.createStatement();
            pstmtAuthors = conn.prepareStatement("INSERT INTO AUTHORS(id,name) " + "VALUES(?,?)");
            pstmtAlbums = conn.prepareStatement("INSERT INTO ALBUMS(id,name,coverFile,authorId) " + "VALUES(?,?,?,?)");
            pstmtSongs = conn.prepareStatement("INSERT INTO SONGS(id,title,file,track,duration,bitrate,channels,bitrateVariable,albumId,authorId,size,lastModified) " + "VALUES(?,?,?,?,?,?,?,?,?,?,?,?)");
            int nauthors = scs.authorsMap.size();
            int nalbums = scs.albumsMap.size();
            int nsongs = scs.songs.size();
            int ntotal = nauthors + nalbums + nsongs;
            stmt.execute("DELETE FROM AUTHORS");
            forEachCount = 0;
            for (Author author : scs.authorsMap.values()) {
                pstmtAuthors.setInt(1, author.getId());
                pstmtAuthors.setString(2, author.getName());
                pstmtAuthors.executeUpdate();
                forEachCount++;
                scs.ccb.processRunning(100 * forEachCount / ntotal);
            }
            stmt.execute("DELETE FROM ALBUMS");
            for (Album album : scs.albumsMap.values()) {
                pstmtAlbums.setInt(1, album.getId());
                pstmtAlbums.setString(2, album.getName());
                if (album.getCover() != null) {
                    pstmtAlbums.setString(3, album.getCover().getAbsolutePath());
                } else {
                    pstmtAlbums.setString(3, "");
                }
                pstmtAlbums.setInt(4, album.getAuthor().getId());
                pstmtAlbums.executeUpdate();
                forEachCount++;
                scs.ccb.processRunning(100 * forEachCount / ntotal);
            }
            stmt.execute("DELETE FROM SONGS");
            for (Song song : scs.songs) {
                pstmtSongs.setInt(1, song.getId());
                pstmtSongs.setString(2, song.getTitle());
                pstmtSongs.setString(3, song.getFile().getAbsolutePath());
                pstmtSongs.setInt(4, song.getTrack());
                pstmtSongs.setLong(5, song.getDuration());
                pstmtSongs.setInt(6, song.getBitrate());
                pstmtSongs.setInt(7, song.getChannels());
                pstmtSongs.setBoolean(8, song.isBitrateVariable());
                pstmtSongs.setInt(9, song.getAlbum().getId());
                pstmtSongs.setInt(10, song.getAuthor().getId());
                pstmtSongs.setLong(11, song.getSize());
                pstmtSongs.setLong(12, song.getLastModified());
                pstmtSongs.executeUpdate();
                forEachCount++;
                scs.ccb.processRunning(100 * forEachCount / ntotal);
            }
            conn.commit();
            stmt.execute("SHUTDOWN");
        } catch (Exception e) {
            throw new MusicCollectionException("Cannot save collection database", e);
        } finally {
            if (pstmtAuthors != null) {
                try {
                    pstmtAuthors.close();
                } catch (Exception e2) {
                }
            }
            if (pstmtAlbums != null) {
                try {
                    pstmtAlbums.close();
                } catch (Exception e2) {
                }
            }
            if (pstmtSongs != null) {
                try {
                    pstmtSongs.close();
                } catch (Exception e2) {
                }
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (Exception e2) {
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (Exception e2) {
                }
            }
        }
        scs.ccb.processRunning(100);
    }
