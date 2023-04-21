    public String content(Integer id, int pageNo, String[] params, PageInfo info, HttpServletRequest request, HttpServletResponse response, ModelMap model) {
        Content content = contentMng.findById(id);
        if (content == null) {
            log.debug("Content id not found: {}", id);
            return FrontUtils.pageNotFound(request, response, model);
        }
        CmsUser user = CmsUtils.getUser(request);
        CmsSite site = content.getSite();
        Set<CmsGroup> groups = content.getViewGroupsExt();
        int len = groups.size();
        if (len != 0) {
            if (user == null) {
                return FrontUtils.showLogin(request, model, site);
            }
            Integer gid = user.getGroup().getId();
            boolean right = false;
            for (CmsGroup group : groups) {
                if (group.getId().equals(gid)) {
                    right = true;
                    break;
                }
            }
            if (!right) {
                String gname = user.getGroup().getName();
                return FrontUtils.showMessage(request, model, GROUP_FORBIDDEN, gname);
            }
        }
        String txt = content.getTxtByNo(pageNo);
        txt = cmsKeywordMng.attachKeyword(site.getId(), txt);
        Paginable pagination = new SimplePage(pageNo, 1, content.getPageCount());
        model.addAttribute("pagination", pagination);
        FrontUtils.frontPageData(request, model);
        model.addAttribute("content", content);
        model.addAttribute("channel", content.getChannel());
        model.addAttribute("title", content.getTitleByNo(pageNo));
        model.addAttribute("txt", txt);
        model.addAttribute("pic", content.getPictureByNo(pageNo));
        FrontUtils.frontData(request, model, site);
        return content.getTplContentOrDef();
    }
