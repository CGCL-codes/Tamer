    public int removePlayer(Player pl) {
        if (pl == null || num_players == 0 || num_filled == 0) {
            System.out.println("ERROR: null player or no players in BattleGame.removePlayer");
            return BG_STATUS_NOADD;
        }
        int idx = getPlayerIndex(pl.getPlayerID());
        if (idx >= 0 && idx < num_filled) {
            for (int i = idx; i < (num_filled - 1); i++) {
                players[i] = players[i + 1];
            }
            players[num_filled - 1] = null;
            num_filled--;
        }
        status = BG_STATUS_AWAIT;
        return status;
    }
