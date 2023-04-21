    public static int[] createRing(MapleClient c, int itemid, int chrId, String chrName, int partnerId, String partnername) {
        try {
            MapleCharacter chr = c.getChannelServer().getPlayerStorage().getCharacterById(partnerId);
            if (chr == null) {
                int[] ret_ = new int[2];
                ret_[0] = -1;
                ret_[1] = -1;
                return ret_;
            }
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("INSERT INTO rings (itemid, partnerChrId, partnername) VALUES (?, ?, ?)");
            ps.setInt(1, itemid);
            ps.setInt(2, partnerId);
            ps.setString(3, partnername);
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            rs.next();
            int[] ret = new int[2];
            ret[0] = rs.getInt(1);
            rs.close();
            ps.close();
            ps = con.prepareStatement("INSERT INTO rings (itemid, partnerRingId, partnerChrId, partnername) VALUES (?, ?, ?, ?)");
            ps.setInt(1, itemid);
            ps.setInt(2, ret[0]);
            ps.setInt(3, chrId);
            ps.setString(4, chrName);
            ps.executeUpdate();
            rs = ps.getGeneratedKeys();
            rs.next();
            ret[1] = rs.getInt(1);
            rs.close();
            ps.close();
            ps = con.prepareStatement("UPDATE rings SET partnerRingId = ? WHERE id = ?");
            ps.setInt(1, ret[1]);
            ps.setInt(2, ret[0]);
            ps.executeUpdate();
            ps.close();
            MapleCharacter player = c.getPlayer();
            MapleInventoryManipulator.addRing(player, itemid, ret[0]);
            MapleInventoryManipulator.addRing(chr, itemid, ret[1]);
            c.getSession().write(MaplePacketCreator.getCharInfo(player));
            player.getMap().removePlayer(player);
            player.getMap().addPlayer(player);
            chr.getClient().getSession().write(MaplePacketCreator.getCharInfo(chr));
            chr.getMap().removePlayer(chr);
            chr.getMap().addPlayer(chr);
            chr.getClient().getSession().write(MaplePacketCreator.serverNotice(5, "You have received a ring from " + player.getName() + ". Please log out and log back in again if it does not work correctly."));
            return ret;
        } catch (SQLException ex) {
            Logger.getLogger(MaplePet.class.getName()).log(Level.SEVERE, null, ex);
            int[] ret = new int[2];
            ret[0] = -1;
            ret[1] = -1;
            return ret;
        }
    }
