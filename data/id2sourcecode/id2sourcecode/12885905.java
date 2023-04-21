    @RequestMapping("/content/v_add.do")
    public String add(Integer cid, HttpServletRequest request, ModelMap model) {
        WebErrors errors = validateAdd(cid, request);
        if (errors.hasErrors()) {
            return errors.showErrorPage(model);
        }
        CmsSite site = CmsUtils.getSite(request);
        Integer siteId = site.getId();
        CmsUser user = CmsUtils.getUser(request);
        Integer userId = user.getId();
        Channel c;
        if (cid != null) {
            c = channelMng.findById(cid);
        } else {
            c = null;
        }
        CmsModel m;
        if (c != null) {
            m = c.getModel();
        } else {
            m = cmsModelMng.getDefModel();
            if (m == null) {
                throw new RuntimeException("default model not found!");
            }
        }
        List<CmsModelItem> itemList = cmsModelItemMng.getList(m.getId(), false, false);
        List<Channel> channelList;
        Set<Channel> rights;
        if (user.getUserSite(siteId).getAllChannel()) {
            rights = null;
        } else {
            rights = user.getChannels(siteId);
        }
        if (c != null) {
            channelList = c.getListForSelect(rights, true);
        } else {
            List<Channel> topList = channelMng.getTopListByRigth(userId, siteId, true);
            channelList = Channel.getListForSelect(topList, rights, true);
        }
        List<CmsTopic> topicList;
        if (c != null) {
            topicList = cmsTopicMng.getListByChannel(c.getId());
        } else {
            topicList = new ArrayList<CmsTopic>();
        }
        List<String> tplList = getTplContent(site, m, null);
        List<CmsGroup> groupList = cmsGroupMng.getList();
        List<ContentType> typeList = contentTypeMng.getList(false);
        model.addAttribute("model", m);
        model.addAttribute("itemList", itemList);
        model.addAttribute("channelList", channelList);
        model.addAttribute("topicList", topicList);
        model.addAttribute("tplList", tplList);
        model.addAttribute("groupList", groupList);
        model.addAttribute("typeList", typeList);
        if (cid != null) {
            model.addAttribute("cid", cid);
        }
        if (c != null) {
            model.addAttribute("channel", c);
        }
        return "content/add";
    }
