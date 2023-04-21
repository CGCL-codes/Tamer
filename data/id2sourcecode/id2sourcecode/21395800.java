    private void parsePatchLine() {
        int id = getInt("id") - 1;
        String name = getString("name");
        int fixtureId = getOptionalNumber("fixture-id") - 1;
        Dimmer dimmer = new Dimmer(dirty, id, name);
        if (fixtureId != -1) {
            Channel channel = show.getChannels().get(fixtureId);
            dimmer.setChannel(channel);
        }
        show.getDimmers().add(dimmer);
    }
