    private String getEventBasics(Event e) {
        return "<(Channel:" + e.getChannel().getChannelID() + "),(Direction:" + (e.getDir() == Direction.DOWN ? "DOWN" : "UP") + "), (Source:" + (e.getSourceSession() != null ? e.getSourceSession().getClass().getName() : "null") + ")>";
    }
