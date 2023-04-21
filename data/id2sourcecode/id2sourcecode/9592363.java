    private void insertTrackInformation(String filename, AudioMetadata metadata) throws SQLException {
        if (!existsTrackInformation(filename)) {
            String title = metadata.getTitle();
            String artistName = metadata.getAuthor();
            String albumName = metadata.getAlbum();
            long duration = metadata.getDuration();
            String genre = metadata.getGenre();
            String year = metadata.getYear();
            BufferedImage artwork = metadata.getArtwork();
            int trackId = getTrackId(filename);
            String update = "INSERT INTO information_tracks_table(id,track_title," + "artist_name,album_name,duration,genre,year,filename) " + "VALUES(?,?,?,?,?,?,?,?)";
            PreparedStatement st = conn.prepareStatement(update);
            st.setInt(1, trackId);
            st.setString(2, title);
            st.setString(3, artistName);
            st.setString(4, albumName);
            st.setLong(5, duration);
            st.setString(6, genre);
            st.setString(7, year);
            st.setString(8, filename);
            st.executeUpdate();
            st.close();
            try {
                byte[] imageBytes = ImageUtils.toByteArray(artwork);
                ByteArrayInputStream inStream = new ByteArrayInputStream(imageBytes);
                MessageDigest md;
                String imageHash = "";
                try {
                    md = MessageDigest.getInstance("MD5");
                    md.update(imageBytes);
                    byte[] hash = md.digest();
                    imageHash = Util.returnHex(hash);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    imageHash = artistName + ":" + albumName + ":" + title;
                }
                if (!artworkExists(imageHash)) {
                    update = "INSERT INTO tracks_cover_table(id,cover) " + "VALUES(?,?)";
                    st = conn.prepareStatement(update);
                    st.setString(1, imageHash);
                    st.setBinaryStream(2, inStream, inStream.available());
                    st.executeUpdate();
                    st.close();
                }
                update = "UPDATE information_tracks_table " + "SET cover_id = ? " + "WHERE id = ? ";
                st = conn.prepareStatement(update);
                st.setString(1, imageHash);
                st.setInt(2, trackId);
                st.executeUpdate();
                st.close();
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("This should not be happening.");
            }
        }
    }
