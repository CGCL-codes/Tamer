    @SuppressWarnings("unchecked")
    public void execute(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body) throws TemplateException, IOException {
        CmsSite site = FrontUtils.getSite(env);
        List<CmsTopic> list = cmsTopicMng.getListForTag(getChannelId(params), getRecommend(params), FrontUtils.getCount(params));
        Map<String, TemplateModel> paramWrap = new HashMap<String, TemplateModel>(params);
        paramWrap.put(OUT_LIST, DEFAULT_WRAPPER.wrap(list));
        Map<String, TemplateModel> origMap = DirectiveUtils.addParamsToVariable(env, paramWrap);
        InvokeType type = DirectiveUtils.getInvokeType(params);
        String listStyle = DirectiveUtils.getString(PARAM_STYLE_LIST, params);
        if (InvokeType.sysDefined == type) {
            if (StringUtils.isBlank(listStyle)) {
                throw new ParamsRequiredException(PARAM_STYLE_LIST);
            }
            env.include(TPL_STYLE_LIST + listStyle + TPL_SUFFIX, UTF8, true);
        } else if (InvokeType.userDefined == type) {
            if (StringUtils.isBlank(listStyle)) {
                throw new ParamsRequiredException(PARAM_STYLE_LIST);
            }
            FrontUtils.includeTpl(TPL_STYLE_LIST, site, env);
        } else if (InvokeType.custom == type) {
            FrontUtils.includeTpl(TPL_NAME, site, params, env);
        } else if (InvokeType.body == type) {
            body.render(env.getOut());
        } else {
            throw new RuntimeException("invoke type not handled: " + type);
        }
        DirectiveUtils.removeParamsFromVariable(env, paramWrap, origMap);
    }
