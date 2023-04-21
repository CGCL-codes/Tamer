    public void setTitle(String title) {
        getChannel().put(Keys.TITLE, new JSONString(title));
    }
