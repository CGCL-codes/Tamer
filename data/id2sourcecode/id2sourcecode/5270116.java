    public void setDescription(String description) {
        getChannel().put(Keys.DESCRIPTION, new JSONString(description));
    }
