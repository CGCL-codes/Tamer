    public void disconnect() {
        MapleCharacter chr = this.getPlayer();
        if (chr != null && isLoggedIn()) {
            if (chr.hasFakeChar()) {
                for (FakeCharacter ch : chr.getFakeChars()) {
                    ch.getFakeChar().getMap().removePlayer(ch.getFakeChar());
                }
            }
            if (chr.getTrade() != null) {
                MapleTrade.cancelTrade(chr);
            }
            chr.cancelAllBuffs();
            if (chr.getEventInstance() != null) {
                chr.getEventInstance().playerDisconnected(chr);
            }
            getPlayer().savePet();
            getPlayer().unequipAllPets();
            try {
                WorldChannelInterface wci = getChannelServer().getWorldInterface();
                if (chr.getMessenger() != null) {
                    MapleMessengerCharacter messengerplayer = new MapleMessengerCharacter(chr);
                    wci.leaveMessenger(chr.getMessenger().getId(), messengerplayer);
                    chr.setMessenger(null);
                }
            } catch (RemoteException e) {
                getChannelServer().reconnectWorld();
            }
            getPlayer().unequipAllPets();
            if (!chr.isAlive()) {
                chr.setHp(50, true);
            }
            chr.setMessenger(null);
            chr.getCheatTracker().dispose();
            chr.saveToDB(true);
            chr.getMap().removePlayer(chr);
            try {
                WorldChannelInterface wci = getChannelServer().getWorldInterface();
                if (chr.getParty() != null) {
                    MaplePartyCharacter chrp = new MaplePartyCharacter(chr);
                    chrp.setOnline(false);
                    wci.updateParty(chr.getParty().getId(), PartyOperation.LOG_ONOFF, chrp);
                }
                if (!this.serverTransition && isLoggedIn()) {
                    wci.loggedOff(chr.getName(), chr.getId(), channel, chr.getBuddylist().getBuddyIds());
                } else {
                    wci.loggedOn(chr.getName(), chr.getId(), channel, chr.getBuddylist().getBuddyIds());
                }
                if (chr.getGuildId() > 0) {
                    wci.setGuildMemberOnline(chr.getMGC(), false, -1);
                }
            } catch (RemoteException e) {
                getChannelServer().reconnectWorld();
            } catch (Exception e) {
                log.error(getLogMessage(this, "ERROR"), e);
            } finally {
                if (getChannelServer() != null) {
                    getChannelServer().removePlayer(chr);
                } else {
                    log.error(getLogMessage(this, "No channelserver associated to char {}", chr.getName()));
                }
            }
        }
        if (!this.serverTransition && isLoggedIn()) {
            this.updateLoginState(MapleClient.LOGIN_NOTLOGGEDIN);
        }
        NPCScriptManager npcsm = NPCScriptManager.getInstance();
        if (npcsm != null) {
            npcsm.dispose(this);
        }
    }
