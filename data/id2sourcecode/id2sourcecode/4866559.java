    public boolean handle(Message m) {
        String request = m.getMessage().trim();
        String nick = m.getFromNick();
        String channel = m.getChannel();
        String action;
        String target;
        int idx = request.indexOf(" ");
        if (idx > 0) {
            action = request.substring(0, idx).trim();
            target = request.substring(idx).trim();
        } else {
            action = request.trim();
            target = null;
        }
        if (action.equalsIgnoreCase("r") || action.equalsIgnoreCase("rock")) return handlePlay(m, nick, target, "rock"); else if (action.equalsIgnoreCase("p") || action.equalsIgnoreCase("paper")) return handlePlay(m, nick, target, "paper"); else if (action.equalsIgnoreCase("s") || action.equalsIgnoreCase("scissors")) return handlePlay(m, nick, target, "scissors"); else if (action.equalsIgnoreCase("score")) {
            return handleScore(m, target);
        } else {
            return false;
        }
    }
