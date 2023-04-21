    public void onMessage(NewGameMessage m, List<Message> out) {
        Puzzle puzzle = generator.getNextPuzzle();
        PlineMessage description = new PlineMessage();
        description.setKey("filter.puzzle.announce", puzzle.getKey(), puzzle.getName(), puzzle.getAuthor());
        out.add(description);
        if (puzzle.getSettings() != null) {
            m.setSettings(puzzle.getSettings());
            getChannel().getConfig().setSettings(puzzle.getSettings());
        }
        out.add(m);
        FieldMessage fieldMessage = new FieldMessage();
        fieldMessage.setSlot(1);
        fieldMessage.setField(puzzle.getField().getFieldString());
        out.add(fieldMessage);
    }
