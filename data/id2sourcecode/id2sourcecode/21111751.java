    private void writeObject(final ObjectOutputStream out) throws IOException {
        out.writeObject(baseUri_);
        out.writeObject(cssRules_);
        out.writeBoolean(disabled_);
        out.writeObject(href_);
        out.writeObject(media_);
        out.writeBoolean(readOnly_);
        out.writeObject(title_);
    }
