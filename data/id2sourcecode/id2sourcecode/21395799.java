    private void parseGroupFixture() {
        if (group == null) {
            error("Group not found");
        } else {
            int id = getInt("id");
            if (id <= 0 || id > show.getChannels().size()) {
                error("Unknown fixture with id \"" + id + "\"");
            } else {
                Channel channel = show.getChannels().get(id - 1);
                group.add(channel);
            }
        }
    }
