    public void onMessage(PlineMessage m, List<Message> out) {
        int slot = m.getSlot();
        String text = m.getText();
        out.add(m);
        if (slot == 0) {
            return;
        }
        if (getChannel().getGameState() != STOPPED) {
            return;
        }
        if (text != null && text.toLowerCase().trim().startsWith("go")) {
            long now = System.currentTimeMillis();
            timestamp[slot - 1] = now;
            boolean doStart = true;
            int i = 0;
            while (i < 6 && doStart) {
                Client player = getChannel().getClient(i + 1);
                doStart = (player == null) || (player != null && (now - timestamp[i]) <= delay);
                i = i + 1;
            }
            if (doStart) {
                Arrays.fill(timestamp, 0);
                if (countdown == 0) {
                    StartGameMessage startMessage = new StartGameMessage();
                    out.add(startMessage);
                } else {
                    (new StartCommand.CountDown(getChannel(), countdown)).start();
                }
            }
        }
    }
