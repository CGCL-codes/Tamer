    public void onMessage(EndGameMessage m, List out) {
        long now = new Date().getTime();
        for (int slot = 0; slot < 6; slot++) {
            slotTime[slot] = slotTime[slot] + (now - lastStart);
        }
        for (int slot = 0; slot < 6; slot++) {
            Client client = getChannel().getPlayer(slot);
            Player player = client.getPlayer();
            System.out.println(player.getName() + " : " + "time played=" + slotTime[slot] + " ");
        }
        out.add(m);
    }
