    public void onMessage(FourLinesAddedMessage m, List<Message> out) {
        int from = m.getFromSlot() - 1;
        tetrisCount[from]++;
        if (addToAll) {
            out.add(m);
        }
        if (tetrisCount[from] >= tetrisLimit) {
            getChannel().send(new EndGameMessage());
            User winner = getChannel().getPlayer(m.getFromSlot());
            PlineMessage announce = new PlineMessage();
            announce.setKey("channel.player_won", winner.getName());
            getChannel().send(announce);
        } else {
            int max = 0;
            for (int i = 0; i < 6; i++) {
                if (tetrisCount[i] > max) {
                    max = tetrisCount[i];
                }
            }
            if (tetrisCount[from] == max) {
                List<String> leaders = new ArrayList<String>();
                for (int i = 0; i < 6; i++) {
                    if (tetrisCount[i] == max) {
                        Client client = getChannel().getClient(i + 1);
                        if (client != null) {
                            leaders.add(client.getUser().getName());
                        }
                    }
                }
                GmsgMessage announce = new GmsgMessage();
                if (leaders.size() == 1) {
                    announce.setKey("filter.tetris.lead", leaders.get(0), max);
                } else {
                    String leadersList = StringUtils.join(leaders.iterator(), ", ");
                    announce.setKey("filter.tetris.tied", leadersList, max);
                }
                out.add(announce);
            }
        }
    }
