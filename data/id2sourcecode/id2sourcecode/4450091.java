    public String getStaticFilenameByRule() {
        Channel channel = getChannel();
        CmsModel model = channel.getModel();
        String rule = channel.getContentRule();
        if (StringUtils.isBlank(rule)) {
            return null;
        }
        String url = StaticPageUtils.staticUrlRule(rule, model.getId(), model.getPath(), channel.getId(), channel.getPath(), getId(), getReleaseDate());
        return url;
    }
