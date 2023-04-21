    private void parseFixture() {
        if ("group".equals(get(-2))) {
            parseGroupFixture();
        } else {
            int id = getInt("id") - 1;
            String name = getString("name");
            Channel channel = new Channel(dirty, id, name);
            show.getChannels().add(channel);
        }
    }
