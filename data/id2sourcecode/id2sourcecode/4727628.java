    private void removeBlock(SpecialMessage message) {
        int slot = message.getFromSlot();
        String team = null;
        if (message.getSource() != null && message.getSource() instanceof Client) {
            Client client = (Client) message.getSource();
            team = client.getUser().getTeam();
        }
        for (int i = 1; i <= 6; i++) {
            Client client = getChannel().getClient(i);
            if (i != slot && client != null) {
                User user = client.getUser();
                if (user.isPlaying() && (user.getTeam() == null || !user.getTeam().equals(team))) {
                    PlayerStats playerStats = stats.get(i - 1);
                    playerStats.blockCount--;
                }
            }
        }
    }
