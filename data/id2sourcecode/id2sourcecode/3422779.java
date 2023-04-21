    protected String[] getChannelPaths(Map<String, TemplateModel> params) throws TemplateException {
        String nameStr = DirectiveUtils.getString(PARAM_CHANNEL_PATH, params);
        if (StringUtils.isBlank(nameStr)) {
            return null;
        }
        return StringUtils.split(nameStr, ',');
    }
