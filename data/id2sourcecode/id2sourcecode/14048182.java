    protected Integer[] getChannelIdsOrPaths(Map<String, TemplateModel> params, Integer[] siteIds) throws TemplateException {
        Integer[] ids = getChannelIds(params);
        if (ids != null) {
            return ids;
        }
        String[] paths = getChannelPaths(params);
        if (paths == null) {
            return null;
        }
        Set<Integer> set = new HashSet<Integer>();
        Channel channel;
        if (siteIds == null) {
            List<CmsSite> list = cmsSiteMng.getListFromCache();
            siteIds = new Integer[list.size()];
            int i = 0;
            for (CmsSite site : list) {
                siteIds[i++] = site.getId();
            }
        }
        for (Integer siteId : siteIds) {
            for (String path : paths) {
                channel = channelMng.findByPathForTag(path, siteId);
                if (channel != null) {
                    set.add(channel.getId());
                }
            }
        }
        if (set.size() > 0) {
            return set.toArray(new Integer[set.size()]);
        } else {
            return null;
        }
    }
