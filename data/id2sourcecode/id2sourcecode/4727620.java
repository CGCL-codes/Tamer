    public void onMessage(PauseMessage m, List<Message> out) {
        if (getChannel().getGameState() == STARTED) {
            stopWatch.suspend();
        }
        out.add(m);
    }
