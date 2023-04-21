    public static List getChannels(String orderBy) throws Exception {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT CHANNEL_ID, TITLE, DESCRIPTION, LOCSTRING, SITE, CREATOR, PUBLISHER, LANGUAGE, FORMAT, IMAGE_ID, TEXTINPUT_ID, COPYRIGHT, RATING, CLOUD_ID, GENERATOR, DOCS, TTL, LAST_UPDATED, LAST_BUILD_DATE, PUB_DATE, UPDATE_PERIOD, UPDATE_FREQUENCY, UPDATE_BASE ");
        sql.append("FROM CHANNELS");
        if (orderBy != null) {
            sql.append(" ORDER BY ");
            sql.append(orderBy);
        }
        List channels = new ArrayList();
        Connection con = Database.getInstance().getConnection();
        ResultSet rs = con.createStatement().executeQuery(sql.toString());
        while (rs.next()) {
            ChannelIF channel = new Channel();
            channel.setId(rs.getLong("CHANNEL_ID"));
            channel.setTitle(rs.getString("TITLE"));
            channel.setDescription(rs.getString("DESCRIPTION"));
            try {
                channel.setLocation(rs.getObject("LOCSTRING") == null ? null : new URL(rs.getString("LOCSTRING")));
            } catch (MalformedURLException e) {
            }
            channel.setSite(rs.getObject("SITE") == null ? null : (URL) Utils.deserialize(rs.getBytes("SITE")));
            channel.setCreator(rs.getString("CREATOR"));
            channel.setPublisher(rs.getString("PUBLISHER"));
            channel.setLanguage(rs.getString("LANGUAGE"));
            channel.setFormat(DAOChannel.getFormat(rs.getString("FORMAT")));
            channel.setImage(rs.getObject("IMAGE_ID") == null ? null : DAOImage.getImage(rs.getLong("IMAGE_ID")));
            channel.setTextInput(rs.getObject("TEXTINPUT_ID") == null ? null : DAOTextInput.getTextInput(rs.getLong("TEXTINPUT_ID")));
            channel.setCopyright(rs.getString("COPYRIGHT"));
            channel.setRating(rs.getString("RATING"));
            channel.setCloud(rs.getObject("CLOUD_ID") == null ? null : DAOCloud.getCloud(rs.getLong("CLOUD_ID")));
            channel.setGenerator(rs.getString("GENERATOR"));
            channel.setDocs(rs.getString("DOCS"));
            channel.setTtl(rs.getInt("TTL"));
            channel.setLastUpdated(rs.getDate("LAST_UPDATED"));
            channel.setLastBuildDate(rs.getDate("LAST_BUILD_DATE"));
            channel.setPubDate(rs.getDate("PUB_DATE"));
            channel.setUpdatePeriod(rs.getString("UPDATE_PERIOD"));
            channel.setUpdateFrequency(rs.getInt("UPDATE_FREQUENCY"));
            channel.setUpdateBase(rs.getDate("UPDATE_BASE"));
            List items = DAOItem.getItemsByTimestampDesc(channel);
            for (int i = 0; i < items.size(); i++) {
                channel.addItem((ItemIF) items.get(i));
            }
            channels.add(channel);
        }
        rs.close();
        return channels;
    }
