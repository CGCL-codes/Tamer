    public static void listBBSThreads(MapleClient c, int start) {
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT * FROM bbs_threads WHERE guildid = ? ORDER BY localthreadid DESC");
            ps.setInt(1, c.getPlayer().getGuildId());
            ResultSet rs = ps.executeQuery();
            c.getSession().write(MaplePacketCreator.BBSThreadList(rs, start));
            ps.close();
        } catch (SQLException se) {
            log.error("SQLException: " + se.getLocalizedMessage(), se);
        }
    }
