    public void onMessage(FieldMessage m, List<Message> out) {
        Field field = getChannel().getField(0);
        if (field.getHighest() <= 2) {
            getChannel().send(new EndGameMessage());
            PlineMessage grats = new PlineMessage();
            grats.setKey("filter.puzzle.cleared");
            getChannel().send(grats);
        }
        out.add(m);
    }
