    public Page getSiteChannelList(Map map) {
        PageRequest pageRequest = new PageRequest();
        Map map_param = new HashMap();
        String sitePartId = (String) map.get("sitePartId");
        if (javacommon.util.StringTool.isNull(sitePartId)) {
            map_param.put("sitePartId", sitePartId);
            pageRequest.setFilters(map_param);
        }
        String count = (String) map.get("count");
        int limit = 10;
        if (javacommon.util.StringTool.isNull(count)) {
            limit = Integer.parseInt(count);
        }
        String start = (String) map.get("start");
        if (javacommon.util.StringTool.isNull(start)) {
            if (limit == 0) {
                pageRequest.setPageNumber(1);
            } else {
                int pageIndex = Integer.parseInt(start);
                pageRequest.setPageNumber(pageIndex / limit + 1);
            }
        } else {
            pageRequest.setPageNumber(1);
        }
        pageRequest.setPageSize(limit);
        String sort = (String) map.get("sort");
        if (javacommon.util.StringTool.isNull(sort)) {
            pageRequest.setSortColumns(sort);
        }
        Page page = siteChannelsDao.getChannelsByPartId(pageRequest);
        System.out.println("page.getPageSize()=" + page.getPageSize());
        List listChannels = (List) page.getResult();
        System.out.println("listChannels.size()=" + listChannels.size());
        return page;
    }
