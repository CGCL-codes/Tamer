    private void win(Player player) {
        long difference = System.currentTimeMillis() - testStart;
        double seconds = difference / 1000;
        DecimalFormat decimalFormatter = new DecimalFormat("#0.00");
        StringBuilder winMessageBuilder = new StringBuilder();
        winMessageBuilder.append(ChatColor.YELLOW);
        winMessageBuilder.append(player.getDisplayName());
        winMessageBuilder.append(" has won the reactiontest in ");
        winMessageBuilder.append(decimalFormatter.format(seconds));
        winMessageBuilder.append(" seconds.");
        Utilities.sendMessageToAll(winMessageBuilder.toString());
        winMessageBuilder = new StringBuilder();
        winMessageBuilder.append(Colors.RED);
        winMessageBuilder.append("*** ");
        winMessageBuilder.append(Utilities.fixName(player));
        winMessageBuilder.append(" has won the reactiontest in ");
        winMessageBuilder.append(decimalFormatter.format(seconds));
        winMessageBuilder.append(" seconds.");
        Main.getInstance().getIRCHandler().sendEchoMessage(winMessageBuilder.toString());
        givePrice(player);
        isRunning = false;
        Main.getInstance().getPlayer(player).addReactiontest();
        callTimer();
    }
