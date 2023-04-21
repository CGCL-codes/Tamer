    private void processGameInfo(GameInfo gi) throws SQLException {
        String map_file_name = FilenameUtils.getName(gi.ssi.mapPath).toLowerCase();
        String map_sha1 = (gi.ssi.mapSHA1 == null) ? "(null)" : String.valueOf(Hex.encodeHex(gi.ssi.mapSHA1));
        log.trace(String.format("Found game %s : %s : %s\n", gi.gameName, map_file_name, map_sha1));
        if (gi.ssi.mapSHA1 == null) {
            log.debug(String.format("SHA1 not present in game info.  Aborting database update."));
            return;
        }
        game_count_stmt.setString(1, gi.gameName);
        ResultSet gamelist_rs = game_count_stmt.executeQuery();
        if (!gamelist_rs.next()) {
            log.error("Unable to get count of matching game names.  Aborting database update.");
            gamelist_rs.close();
            connection.rollback();
            return;
        }
        int count = gamelist_rs.getInt(1);
        gamelist_rs.close();
        if (count != 0) {
            connection.rollback();
            return;
        }
        map_lookup_stmt.setString(1, map_file_name);
        ResultSet maps_rs = map_lookup_stmt.executeQuery();
        int map_id;
        if (maps_rs.next()) {
            String stored_sha1 = maps_rs.getString("sha1");
            if (!map_sha1.equalsIgnoreCase(stored_sha1)) {
                log.debug(String.format("Map SHA1 mismatch - got %s, expected %s.  Aborting database update.", map_sha1, stored_sha1));
                maps_rs.close();
                connection.rollback();
                return;
            }
            map_id = maps_rs.getInt("id");
        } else {
            map_insert_stmt.setString(1, map_file_name);
            map_insert_stmt.setString(2, map_sha1);
            map_insert_stmt.executeUpdate();
            ResultSet last_id_rs = last_insert_id_stmt.executeQuery();
            if (!last_id_rs.next()) {
                log.error("Unable to retrieve last insert ID after map insertion.  Aborting database update.");
                last_id_rs.close();
                maps_rs.close();
                connection.rollback();
                return;
            }
            map_id = last_id_rs.getInt(1);
            last_id_rs.close();
        }
        maps_rs.close();
        game_insert_stmt.setString(1, gi.gameName);
        game_insert_stmt.setInt(2, map_id);
        game_insert_stmt.setString(3, gi.ssi.hostName);
        game_insert_stmt.setString(4, realm);
        game_insert_stmt.setString(5, gi.hostAddress.getAddress().getHostAddress());
        game_insert_stmt.setInt(6, gi.hostAddress.getPort());
        game_insert_stmt.executeUpdate();
        connection.commit();
        log.trace("Database update succeeded.");
    }
