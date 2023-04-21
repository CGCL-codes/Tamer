    private static void sendMessages(Game game, String command) {
        String msg = "{ \"command\": \"" + command + "\", \"gameId\": " + game.getKey().getId() + ", \"black\": " + id(game.getBlack()) + ", \"black_hama\": " + game.getBlackHama() + ", \"black_before\": " + array(game.getBlackBefore()) + ", \"white\": " + id(game.getWhite()) + ", \"white_hama\": " + game.getWhiteHama() + ", \"white_before\": " + array(game.getWhiteBefore()) + ", \"turn\": " + game.getTurn() + ", \"player\": " + id(game.getPlayer()) + ", \"grid\": " + toString(game.getGrid()) + ", \"winner\": " + id(game.getWinner()) + "}";
        System.out.println(msg);
        getChannelService().sendMessage(new ChannelMessage("" + game.getBlack().getId(), msg));
        getChannelService().sendMessage(new ChannelMessage("" + game.getWhite().getId(), msg));
    }
