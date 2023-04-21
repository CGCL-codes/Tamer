    @RequestMapping(value = "/comment*.jspx", method = RequestMethod.GET)
    public String page(Integer contentId, Integer pageNo, HttpServletRequest request, HttpServletResponse response, ModelMap model) {
        CmsSite site = CmsUtils.getSite(request);
        Content content = contentMng.findById(contentId);
        if (content == null) {
            return FrontUtils.showMessage(request, model, "comment.contentNotFound");
        }
        if (content.getChannel().getCommentControl() == ChannelExt.COMMENT_OFF) {
            return FrontUtils.showMessage(request, model, "comment.closed");
        }
        model.putAll(RequestUtils.getQueryParams(request));
        FrontUtils.frontData(request, model, site);
        FrontUtils.frontPageData(request, model);
        model.addAttribute("content", content);
        return FrontUtils.getTplPath(request, site.getSolutionPath(), TPLDIR_SPECIAL, COMMENT_PAGE);
    }
