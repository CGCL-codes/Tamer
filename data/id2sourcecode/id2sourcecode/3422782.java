    protected int getChannelOption(Map<String, TemplateModel> params) throws TemplateException {
        Integer option = DirectiveUtils.getInt(PARAM_CHANNEL_OPTION, params);
        if (option == null || option < 0 || option > 2) {
            return 0;
        } else {
            return option;
        }
    }
