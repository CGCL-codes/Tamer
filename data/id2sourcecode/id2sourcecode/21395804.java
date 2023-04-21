    private void parseSubmasterChannelLevel() {
        if (submaster == null) {
            error("Submaster not found");
        } else {
            int fixtureId = getInt("fixture-id");
            if (fixtureId <= 0 || fixtureId > show.getChannels().size()) {
                error("Unknown fixture with id \"" + fixtureId + "\"");
            } else {
                int value = getInt("value");
                Level level = submaster.getLevel(fixtureId - 1);
                level.setActive(true);
                level.setIntValue(value);
            }
        }
    }
