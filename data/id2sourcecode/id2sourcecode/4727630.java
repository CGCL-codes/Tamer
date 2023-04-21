    private void displayStats(List<Message> out) {
        for (int slot = 1; slot <= 6; slot++) {
            PlayerStats playerStats = stats.get(slot - 1);
            User user = getChannel().getPlayer(slot);
            if (playerStats != null && user != null) {
                if (playerStats.playing) {
                    playerStats.timePlayed = stopWatch.getTime();
                }
                String bpm = df.format(playerStats.getBlocksPerMinute());
                StringBuilder text = new StringBuilder();
                text.append("<purple>" + user.getName() + "</purple> : ");
                text.append(playerStats.blockCount + " <aqua>blocks @<red>" + bpm + "</red> bpm, ");
                text.append("<black>" + playerStats.linesAdded + "</black> added, ");
                text.append("<black>" + playerStats.tetrisCount + "</black> tetris");
                if (getChannel().getConfig().getSettings().getSpecialAdded() > 0) {
                    text.append(", <black>" + playerStats.specialsSent + " / " + playerStats.specialsReceived + "</black> specials");
                }
                out.add(new PlineMessage(text.toString()));
            }
        }
        PlineMessage time = new PlineMessage();
        time.setText("<brown>Total game time: <black>" + df.format(stopWatch.getTime() / 1000f) + "</black> seconds");
        out.add(time);
    }
