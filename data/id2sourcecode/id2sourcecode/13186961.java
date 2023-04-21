    private void process(AddPlayerMessage m) {
        Client client = m.getClient();
        Channel previousChannel = client.getChannel();
        client.setChannel(this);
        if (previousChannel != null && !client.supportsMultipleChannels()) {
            for (int j = 1; j <= MAX_PLAYERS; j++) {
                if (previousChannel.getPlayer(j) != null) {
                    LeaveMessage clear = new LeaveMessage();
                    clear.setSlot(j);
                    client.send(clear);
                }
            }
            previousChannel.removeClient(client);
            PlineMessage announce = new PlineMessage();
            announce.setKey("channel.join_notice", client.getUser().getName(), channelConfig.getName());
            previousChannel.send(announce);
            if (client.getUser().isPlaying()) {
                client.getUser().setPlaying(false);
                client.send(new EndGameMessage());
            }
        }
        clients.add(client);
        if (client.getUser().isSpectator()) {
            JoinMessage mjoin = new JoinMessage();
            mjoin.setName(client.getUser().getName());
            sendAll(mjoin);
            PlayerNumMessage mnum = new PlayerNumMessage(1);
            client.send(mnum);
        } else {
            int slot = 0;
            for (slot = 0; slot < slots.size() && slots.get(slot) != null; slot++) ;
            if (slot >= MAX_PLAYERS) {
                log.warning("[" + getConfig().getName() + "] Panic, no slot available for " + client);
                client.getUser().setSpectator();
            } else {
                slots.set(slot, client);
                JoinMessage mjoin = new JoinMessage();
                mjoin.setSlot(slot + 1);
                mjoin.setName(client.getUser().getName());
                sendAll(mjoin, client);
                PlayerNumMessage mnum = new PlayerNumMessage(slot + 1);
                client.send(mnum);
            }
            updateChannelOperator();
        }
        if (client.getUser().isSpectator()) {
            sendSpectatorList(client);
        }
        for (int i = 0; i < slots.size(); i++) {
            Client resident = slots.get(i);
            if (resident != null && resident != client) {
                JoinMessage mjoin2 = new JoinMessage();
                mjoin2.setChannelName(getConfig().getName());
                mjoin2.setSlot(i + 1);
                mjoin2.setName(resident.getUser().getName());
                client.send(mjoin2);
                TeamMessage mteam = new TeamMessage();
                mteam.setChannelName(getConfig().getName());
                mteam.setSource(resident);
                mteam.setSlot(i + 1);
                mteam.setName(resident.getUser().getTeam());
                client.send(mteam);
            }
        }
        for (int i = 0; i < MAX_PLAYERS; i++) {
            if (!fields[i].isEmpty() || previousChannel != null) {
                FieldMessage message = new FieldMessage(i + 1, fields[i].getFieldString());
                client.send(message);
            }
        }
        Winlist winlist = WinlistManager.getInstance().getWinlist(channelConfig.getWinlistId());
        if (winlist != null) {
            List<Score> topScores = winlist.getScores(0, 10);
            WinlistMessage winlistMessage = new WinlistMessage();
            winlistMessage.setScores(topScores);
            client.send(winlistMessage);
        }
        PlineMessage mwelcome = new PlineMessage();
        mwelcome.setKey("channel.welcome", client.getUser().getName(), channelConfig.getName());
        client.send(mwelcome);
        if (channelConfig.getTopic() != null) {
            BufferedReader topic = new BufferedReader(new StringReader(channelConfig.getTopic()));
            try {
                String line;
                while ((line = topic.readLine()) != null) {
                    PlineMessage message = new PlineMessage();
                    message.setText("<kaki>" + line);
                    client.send(message);
                }
                topic.close();
            } catch (Exception e) {
                log.log(Level.WARNING, e.getMessage(), e);
            }
        }
        if (client.getUser().isPlayer()) {
            sendSpectatorList(client);
        }
        if (gameState != STOPPED) {
            IngameMessage ingame = new IngameMessage();
            ingame.setChannelName(getConfig().getName());
            client.send(ingame);
            if (gameState == PAUSED) {
                client.send(new PauseMessage());
            }
        }
        if (client instanceof TetrinetClient) {
            int timeout = getConfig().isIdleAllowed() ? 0 : serverConfig.getTimeout() * 1000;
            try {
                ((TetrinetClient) client).getSocket().setSoTimeout(timeout);
            } catch (SocketException e) {
                log.log(Level.WARNING, "Unable to change the timeout", e);
            }
        }
    }
