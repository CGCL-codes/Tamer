    public MapleGuild(MapleGuildCharacter initiator) {
        int guildid = initiator.getGuildId();
        members = new ArrayList<MapleGuildCharacter>();
        Logger log = LoggerFactory.getLogger(this.getClass());
        Connection con;
        try {
            con = DatabaseConnection.getConnection();
        } catch (Exception e) {
            log.error("unable to connect to database to load guild information.", e);
            return;
        }
        try {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM guilds WHERE guildid=" + guildid);
            ResultSet rs = ps.executeQuery();
            if (!rs.first()) {
                id = -1;
                return;
            }
            id = guildid;
            name = rs.getString("name");
            gp = rs.getInt("GP");
            logo = rs.getInt("logo");
            logoColor = rs.getInt("logoColor");
            logoBG = rs.getInt("logoBG");
            logoBGColor = rs.getInt("logoBGColor");
            capacity = rs.getInt("capacity");
            for (int i = 1; i <= 5; i++) {
                rankTitles[i - 1] = rs.getString("rank" + i + "title");
            }
            leader = rs.getInt("leader");
            notice = rs.getString("notice");
            signature = rs.getInt("signature");
            ps.close();
            rs.close();
            ps = con.prepareStatement("SELECT id, name, level, job, guildrank FROM characters WHERE guildid = ? ORDER BY guildrank ASC, name ASC");
            ps.setInt(1, guildid);
            rs = ps.executeQuery();
            if (!rs.first()) {
                log.error("No members in guild.  Impossible...");
                return;
            }
            do {
                members.add(new MapleGuildCharacter(rs.getInt("id"), rs.getInt("level"), rs.getString("name"), -1, rs.getInt("job"), rs.getInt("guildrank"), guildid, false));
            } while (rs.next());
            setOnline(initiator.getId(), true, initiator.getChannel());
            ps.close();
            rs.close();
        } catch (SQLException se) {
            log.error("unable to read guild information from sql", se);
            return;
        }
    }
