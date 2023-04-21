    @SuppressWarnings("unchecked")
    public void execute(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body) throws TemplateException, IOException {
        CmsSite site = FrontUtils.getSite(env);
        int pageNo = FrontUtils.getPageNo(env);
        int count = FrontUtils.getCount(params);
        String query = getQuery(params);
        Integer siteId = getSiteId(params);
        Integer channelId = getChannelId(params);
        Date startDate = getStartDate(params);
        Date endDate = getEndDate(params);
        Pagination page;
        try {
            String path = realPathResolver.get(Constants.LUCENE_PATH);
            page = luceneContentSvc.searchPage(path, query, siteId, channelId, startDate, endDate, pageNo, count);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        Map<String, TemplateModel> paramWrap = new HashMap<String, TemplateModel>(params);
        paramWrap.put(OUT_PAGINATION, DEFAULT_WRAPPER.wrap(page));
        paramWrap.put(OUT_LIST, DEFAULT_WRAPPER.wrap(page.getList()));
        Map<String, TemplateModel> origMap = DirectiveUtils.addParamsToVariable(env, paramWrap);
        InvokeType type = DirectiveUtils.getInvokeType(params);
        String listStyle = DirectiveUtils.getString(PARAM_STYLE_LIST, params);
        if (InvokeType.sysDefined == type) {
            if (StringUtils.isBlank(listStyle)) {
                throw new ParamsRequiredException(PARAM_STYLE_LIST);
            }
            env.include(TPL_STYLE_LIST + listStyle + TPL_SUFFIX, UTF8, true);
            FrontUtils.includePagination(site, params, env);
        } else if (InvokeType.userDefined == type) {
            if (StringUtils.isBlank(listStyle)) {
                throw new ParamsRequiredException(PARAM_STYLE_LIST);
            }
            FrontUtils.includeTpl(TPL_STYLE_LIST, site, env);
            FrontUtils.includePagination(site, params, env);
        } else if (InvokeType.custom == type) {
            FrontUtils.includeTpl(TPL_NAME, site, params, env);
            FrontUtils.includePagination(site, params, env);
        } else if (InvokeType.body == type) {
            body.render(env.getOut());
            FrontUtils.includePagination(site, params, env);
        } else {
            throw new RuntimeException("invoke type not handled: " + type);
        }
        DirectiveUtils.removeParamsFromVariable(env, paramWrap, origMap);
    }
