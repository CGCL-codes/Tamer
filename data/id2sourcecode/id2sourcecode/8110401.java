    public WorldLoginInterface registerLoginServer(String authKey, LoginWorldInterface cb) throws RemoteException {
        WorldLoginInterface ret = null;
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT * FROM loginserver WHERE `key` = SHA1(?) AND world = ?");
            ps.setString(1, authKey);
            ps.setInt(2, WorldServer.getInstance().getWorldId());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                loginServer.add(cb);
                for (ChannelWorldInterface cwi : channelServer.values()) {
                    cb.channelOnline(cwi.getChannelId(), authKey);
                }
            }
            rs.close();
            ps.close();
            ret = new WorldLoginInterfaceImpl();
        } catch (Exception e) {
            log.error("Encountered database error while authenticating loginserver", e);
        }
        return ret;
    }
