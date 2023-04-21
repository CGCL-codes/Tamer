    protected String channelsEditJson(Integer userId, Integer siteId, HttpServletRequest request, HttpServletResponse response, ModelMap model) {
        List<Channel> channelList = channelMng.getTopList(siteId, false);
        CmsUser user = manager.findById(userId);
        model.addAttribute("channelList", channelList);
        model.addAttribute("channelIds", user.getChannelIds(siteId));
        response.setHeader("Cache-Control", "no-cache");
        response.setContentType("text/json;charset=UTF-8");
        return "admin/channels_edit";
    }
