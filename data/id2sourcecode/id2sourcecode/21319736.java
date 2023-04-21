    @Override
    public LinkedList<ResponseMessage> inform(TextMessage message) {
        String giveOp;
        if (bot != null) {
            giveOp = "(" + bot.getName() + "|" + bot.getName().toLowerCase() + ")[:,]? ?([gG]i(b|ve|pp?) ?[oO]pp?!?!?|[oO]p!!?!?)";
        } else {
            giveOp = "(" + bot2.getName() + "|" + bot2.getName().toLowerCase() + ")[:,]? ?([gG]i(b|ve|pp?) ?[oO]pp?!?!?|[oO]p!!?!?)";
        }
        if (message.getText().matches(giveOp)) {
            loadOpers();
            if (bot != null) {
                if (opers.contains(message.getName())) {
                    bot.send(new RawResponseMessage("MODE " + bot.getChannel() + " +o " + message.getName(), 100, 1000));
                } else {
                    bot.send(new ResponseMessage("Noe.", 100, 1000));
                }
            } else {
                if (opers.contains(message.getName())) {
                    bot2.send(new RawResponseMessage("MODE " + bot2.getChannel() + " +o " + message.getName(), 100, 1000));
                } else {
                    bot2.send(new ResponseMessage("Noe.", 100, 1000));
                }
            }
        }
        return null;
    }
