    @RequestMapping("/content/v_edit.do")
    public String edit(String queryStatus, Integer queryTypeId, Boolean queryTopLevel, Boolean queryRecommend, Integer queryOrderBy, Integer pageNo, Integer cid, Integer id, HttpServletRequest request, ModelMap model) {
        WebErrors errors = validateEdit(id, request);
        if (errors.hasErrors()) {
            return errors.showErrorPage(model);
        }
        CmsSite site = CmsUtils.getSite(request);
        Integer siteId = site.getId();
        CmsUser user = CmsUtils.getUser(request);
        Content content = manager.findById(id);
        Channel channel = content.getChannel();
        CmsModel m = channel.getModel();
        List<CmsModelItem> itemList = cmsModelItemMng.getList(m.getId(), false, false);
        Set<Channel> rights;
        if (user.getUserSite(siteId).getAllChannel()) {
            rights = null;
        } else {
            rights = user.getChannels(siteId);
        }
        List<Channel> topList = channelMng.getTopList(site.getId(), true);
        List<Channel> channelList = Channel.getListForSelect(topList, rights, true);
        List<CmsTopic> topicList = cmsTopicMng.getListByChannel(channel.getId());
        Set<CmsTopic> topics = content.getTopics();
        for (CmsTopic t : topics) {
            if (!topicList.contains(t)) {
                topicList.add(t);
            }
        }
        Integer[] topicIds = CmsTopic.fetchIds(content.getTopics());
        List<String> tplList = getTplContent(site, m, content.getTplContent());
        List<CmsGroup> groupList = cmsGroupMng.getList();
        Integer[] groupIds = CmsGroup.fetchIds(content.getViewGroups());
        List<ContentType> typeList = contentTypeMng.getList(false);
        int tplPathLength = site.getTplPath().length();
        String tplContent = content.getTplContent();
        if (!StringUtils.isBlank(tplContent)) {
            tplContent = tplContent.substring(tplPathLength);
        }
        model.addAttribute("content", content);
        model.addAttribute("channel", channel);
        model.addAttribute("model", m);
        model.addAttribute("itemList", itemList);
        model.addAttribute("channelList", channelList);
        model.addAttribute("topicList", topicList);
        model.addAttribute("topicIds", topicIds);
        model.addAttribute("tplList", tplList);
        model.addAttribute("groupList", groupList);
        model.addAttribute("groupIds", groupIds);
        model.addAttribute("typeList", typeList);
        model.addAttribute("tplContent", tplContent);
        if (cid != null) {
            model.addAttribute("cid", cid);
        }
        String queryTitle = RequestUtils.getQueryParam(request, "queryTitle");
        String queryInputUsername = RequestUtils.getQueryParam(request, "queryInputUsername");
        addAttibuteForQuery(model, queryTitle, queryInputUsername, queryStatus, queryTypeId, queryTopLevel, queryRecommend, queryOrderBy, pageNo);
        return "content/edit";
    }
