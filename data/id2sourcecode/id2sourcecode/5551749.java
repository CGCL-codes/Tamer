    public static void displayThread(MapleClient client, int threadid, boolean bIsThreadIdLocal) {
        MapleCharacter mc = client.getPlayer();
        if (mc.getGuildId() <= 0) return;
        Connection con = DatabaseConnection.getConnection();
        try {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM bbs_threads WHERE guildid = ? AND " + (bIsThreadIdLocal ? "local" : "") + "threadid = ?");
            ps.setInt(1, mc.getGuildId());
            ps.setInt(2, threadid);
            ResultSet threadRS = ps.executeQuery();
            if (!threadRS.next()) return;
            ResultSet repliesRS = null;
            PreparedStatement ps2 = null;
            if (threadRS.getInt("replycount") > 0) {
                ps2 = con.prepareStatement("SELECT * FROM bbs_replies WHERE threadid = ?");
                ps2.setInt(1, !bIsThreadIdLocal ? threadid : threadRS.getInt("threadid"));
                repliesRS = ps2.executeQuery();
            }
            client.getSession().write(MaplePacketCreator.showThread(bIsThreadIdLocal ? threadid : threadRS.getInt("localthreadid"), threadRS, repliesRS));
            ps.close();
            if (ps2 != null) ps2.close();
        } catch (SQLException se) {
            log.error("SQLException: " + se.getLocalizedMessage(), se);
        } catch (RuntimeException re) {
            log.error("The number of reply rows does not match the replycount in thread. Thread Id = " + re.getMessage(), re);
            try {
                PreparedStatement ps = con.prepareStatement("DELETE FROM bbs_threads WHERE threadid = ?");
                ps.setInt(1, Integer.parseInt(re.getMessage()));
                ps.execute();
                ps.close();
                ps = con.prepareStatement("DELETE FROM bbs_replies WHERE threadid = ?");
                ps.setInt(1, Integer.parseInt(re.getMessage()));
                ps.execute();
                ps.close();
            } catch (Exception e) {
            }
        }
    }
