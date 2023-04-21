    @RequestMapping("/topic/v_edit.do")
    public String edit(Integer id, HttpServletRequest request, ModelMap model) {
        WebErrors errors = validateEdit(id, request);
        if (errors.hasErrors()) {
            return errors.showErrorPage(model);
        }
        CmsSite site = CmsUtils.getSite(request);
        CmsTopic topic = manager.findById(id);
        List<String> tplList = getTplList(request, site, topic.getTplContent());
        Integer siteId;
        Channel channel = topic.getChannel();
        if (channel != null) {
            siteId = channel.getSite().getId();
        } else {
            siteId = site.getId();
        }
        List<Channel> topList = channelMng.getTopList(siteId, true);
        List<Channel> channelList = Channel.getListForSelect(topList, null, true);
        model.addAttribute("tplList", tplList);
        model.addAttribute("channelList", channelList);
        model.addAttribute("cmsTopic", topic);
        return "topic/edit";
    }
