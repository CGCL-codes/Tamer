    protected Integer[] getChannelIds(Map<String, TemplateModel> params) throws TemplateException {
        Integer[] ids = DirectiveUtils.getIntArray(PARAM_CHANNEL_ID, params);
        if (ids != null && ids.length > 0) {
            return ids;
        } else {
            return null;
        }
    }
