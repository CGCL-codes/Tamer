    public String getChannelIconURL() throws IOException {
        final File file = TVChannelIconHelper.getIconFile(programme.getChannel());
        return (file != null) ? file.toURI().toURL().toString() : null;
    }
