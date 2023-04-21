    public void uploadImages(MP_USER mpUser, MP_POST_CHANNEL mpChannel) throws java.sql.SQLException, java.io.IOException, FileNotFoundException {
        Connection con = db.getConnection();
        log.log(Level.INFO, "uploading images for channel " + mpUser.getUserId() + ":" + mpChannel.getChannelName());
        String query = "select " + "user_id, " + "channel_name, " + "post_email, " + "post_date, " + "title, " + "description, " + "image_mime_type " + "from blog_post " + "where " + " user_id = ? " + "AND " + " channel_name = ? " + " ORDER BY post_date DESC";
        PreparedStatement stmt = con.prepareStatement(query);
        stmt.setString(1, mpUser.getUserId());
        stmt.setString(2, mpChannel.getChannelName());
        ResultSet rs = stmt.executeQuery();
        int row = 0;
        MP_POST mpPost = new MP_POST();
        while (rs.next()) {
            if (row >= 15) break;
            mpPost.setValues(rs);
            uploadImage(mpChannel, mpPost);
            row++;
        }
        stmt.close();
    }
