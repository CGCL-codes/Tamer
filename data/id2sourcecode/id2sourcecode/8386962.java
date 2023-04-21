    @Transactional(readOnly = true)
    public void contentRelated(Content content) throws IOException, TemplateException {
        content(content);
        Channel channel = content.getChannel();
        while (channel != null) {
            channel(channel, true);
            channel = channel.getParent();
        }
        if (content.getSite().getStaticIndex()) {
            index(content.getSite());
        }
    }
