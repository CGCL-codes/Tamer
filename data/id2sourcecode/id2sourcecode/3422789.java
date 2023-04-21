    protected Object getData(Map<String, TemplateModel> params, Environment env) throws TemplateException {
        int orderBy = getOrderBy(params);
        Boolean titleImg = getHasTitleImg(params);
        Boolean recommend = getRecommend(params);
        Integer[] typeIds = getTypeIds(params);
        Integer[] siteIds = getSiteIds(params);
        String title = getTitle(params);
        int count = FrontUtils.getCount(params);
        Integer[] tagIds = getTagIds(params);
        if (tagIds != null) {
            Integer[] channelIds = getChannelIdsOrPaths(params, siteIds);
            Integer excludeId = DirectiveUtils.getInt(PARAM_EXCLUDE_ID, params);
            if (isPage()) {
                int pageNo = FrontUtils.getPageNo(env);
                return contentMng.getPageByTagIdsForTag(tagIds, siteIds, channelIds, typeIds, excludeId, titleImg, recommend, title, orderBy, pageNo, count);
            } else {
                int first = FrontUtils.getFirst(params);
                return contentMng.getListByTagIdsForTag(tagIds, siteIds, channelIds, typeIds, excludeId, titleImg, recommend, title, orderBy, first, count);
            }
        }
        Integer topicId = getTopicId(params);
        if (topicId != null) {
            Integer[] channelIds = getChannelIdsOrPaths(params, siteIds);
            if (isPage()) {
                int pageNo = FrontUtils.getPageNo(env);
                return contentMng.getPageByTopicIdForTag(topicId, siteIds, channelIds, typeIds, titleImg, recommend, title, orderBy, pageNo, count);
            } else {
                int first = FrontUtils.getFirst(params);
                return contentMng.getListByTopicIdForTag(topicId, siteIds, channelIds, typeIds, titleImg, recommend, title, orderBy, first, count);
            }
        }
        Integer[] channelIds = getChannelIds(params);
        if (channelIds != null) {
            int option = getChannelOption(params);
            if (isPage()) {
                int pageNo = FrontUtils.getPageNo(env);
                return contentMng.getPageByChannelIdsForTag(channelIds, typeIds, titleImg, recommend, title, orderBy, option, pageNo, count);
            } else {
                int first = FrontUtils.getFirst(params);
                return contentMng.getListByChannelIdsForTag(channelIds, typeIds, titleImg, recommend, title, orderBy, option, first, count);
            }
        }
        String[] channelPaths = getChannelPaths(params);
        if (channelPaths != null) {
            int option = getChannelOption(params);
            boolean pathsToIds = false;
            Integer siteId = null;
            if (siteIds == null || siteIds.length == 0) {
                List<CmsSite> siteList = cmsSiteMng.getListFromCache();
                if (siteList.size() == 1) {
                    pathsToIds = true;
                    siteId = siteList.get(0).getId();
                }
            } else if (siteIds != null && siteIds.length == 1) {
                pathsToIds = true;
                siteId = siteIds[0];
            }
            if (pathsToIds) {
                channelIds = getChannelIdsByPaths(channelPaths, siteId);
                if (channelIds != null) {
                    if (isPage()) {
                        int pageNo = FrontUtils.getPageNo(env);
                        return contentMng.getPageByChannelIdsForTag(channelIds, typeIds, titleImg, recommend, title, orderBy, option, pageNo, count);
                    } else {
                        int first = FrontUtils.getFirst(params);
                        return contentMng.getListByChannelIdsForTag(channelIds, typeIds, titleImg, recommend, title, orderBy, option, first, count);
                    }
                } else {
                }
            } else {
                if (isPage()) {
                    int pageNo = FrontUtils.getPageNo(env);
                    return contentMng.getPageByChannelPathsForTag(channelPaths, siteIds, typeIds, titleImg, recommend, title, orderBy, pageNo, count);
                } else {
                    int first = FrontUtils.getFirst(params);
                    return contentMng.getListByChannelPathsForTag(channelPaths, siteIds, typeIds, titleImg, recommend, title, orderBy, first, count);
                }
            }
        }
        if (isPage()) {
            int pageNo = FrontUtils.getPageNo(env);
            return contentMng.getPageBySiteIdsForTag(siteIds, typeIds, titleImg, recommend, title, orderBy, pageNo, count);
        } else {
            int first = FrontUtils.getFirst(params);
            return contentMng.getListBySiteIdsForTag(siteIds, typeIds, titleImg, recommend, title, orderBy, first, count);
        }
    }
