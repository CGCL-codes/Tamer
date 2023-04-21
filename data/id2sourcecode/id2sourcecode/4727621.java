    public void onMessage(ResumeMessage m, List<Message> out) {
        if (getChannel().getGameState() == PAUSED) {
            stopWatch.resume();
        }
        out.add(m);
    }
