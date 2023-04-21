    private String getMime(List<String> channels) {
        if (channels == null || channels.isEmpty()) {
            return null;
        }
        String mime = rbnb.getChannel(channels.get(0)).getMetadata("mime");
        for (int i = 1; i < channels.size(); i++) {
            String channel = channels.get(i);
            if (!mime.equals(rbnb.getChannel(channel).getMetadata("mime"))) {
                return null;
            }
        }
        return mime;
    }
