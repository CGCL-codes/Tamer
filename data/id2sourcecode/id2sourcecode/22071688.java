    @RequestMapping(value = "/comment.jspx", method = RequestMethod.POST)
    public void submit(Integer contentId, String text, String captcha, HttpServletRequest request, HttpServletResponse response, ModelMap model) throws JSONException {
        CmsSite site = CmsUtils.getSite(request);
        CmsUser user = CmsUtils.getUser(request);
        JSONObject json = new JSONObject();
        if (contentId == null) {
            json.put("success", false);
            json.put("status", 100);
            ResponseUtils.renderJson(response, json.toString());
            return;
        }
        if (StringUtils.isBlank(text)) {
            json.put("success", false);
            json.put("status", 101);
            ResponseUtils.renderJson(response, json.toString());
            return;
        }
        if (user == null || user.getGroup().getNeedCaptcha()) {
            try {
                if (!imageCaptchaService.validateResponseForID(session.getSessionId(request, response), captcha)) {
                    json.put("success", false);
                    json.put("status", 1);
                    ResponseUtils.renderJson(response, json.toString());
                    return;
                }
            } catch (CaptchaServiceException e) {
                json.put("success", false);
                json.put("status", 1);
                log.warn("", e);
                ResponseUtils.renderJson(response, json.toString());
                return;
            }
        }
        Content content = contentMng.findById(contentId);
        if (content == null) {
            json.put("success", false);
            json.put("status", 2);
        } else if (content.getChannel().getCommentControl() == ChannelExt.COMMENT_OFF) {
            json.put("success", false);
            json.put("status", 3);
        } else if (content.getChannel().getCommentControl() == ChannelExt.COMMENT_LOGIN && user == null) {
            json.put("success", false);
            json.put("status", 4);
        } else {
            boolean checked = false;
            Integer userId = null;
            if (user != null) {
                checked = !user.getGroup().getNeedCheck();
                userId = user.getId();
            }
            cmsCommentMng.comment(text, RequestUtils.getIpAddr(request), contentId, site.getId(), userId, checked, false);
            json.put("success", true);
            json.put("status", 0);
        }
        ResponseUtils.renderJson(response, json.toString());
    }
