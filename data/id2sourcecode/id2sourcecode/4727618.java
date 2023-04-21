    public void onMessage(StartGameMessage m, List<Message> out) {
        if (getChannel().getGameState() == STOPPED) {
            stopWatch.reset();
            stopWatch.start();
            for (int i = 0; i < 6; i++) {
                if (getChannel().getClient(i + 1) != null) {
                    stats.set(i, new PlayerStats());
                } else {
                    stats.set(i, null);
                }
            }
        }
        out.add(m);
    }
