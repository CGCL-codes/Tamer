    public String getStaticFilenameByRule() {
        String rule = getChannelRule();
        if (StringUtils.isBlank(rule)) {
            return null;
        }
        CmsModel model = getModel();
        String url = StaticPageUtils.staticUrlRule(rule, model.getId(), model.getPath(), getId(), getPath(), null, null);
        return url;
    }
