    public String getDescription() {
        return JSONStringValueOrNull((JSONString) getChannel().get(Keys.DESCRIPTION));
    }
