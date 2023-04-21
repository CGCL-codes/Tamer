    public void onMessage(EndGameMessage m, List<Message> out) {
        out.add(m);
        if (getChannel().getGameState() != STOPPED) {
            stopWatch.stop();
            displayStats(out);
        }
    }
