    public ModelAndView submitArticle(HttpServletRequest req, HttpServletResponse res) {
        Map rsmap = new HashMap();
        this.manageProxy(req, res, rsmap);
        String title = req.getParameter("title");
        String channelId = req.getParameter("channelId");
        String content = req.getParameter("content");
        if (!StrUtil.empty(title) && !StrUtil.empty(channelId) && !StrUtil.empty(content)) {
            Long chid = (long) StrUtil.parseInt(channelId);
            Users user = (Users) rsmap.get("user");
            Text text = new Text(content);
            Article article = null;
            String articleid = req.getParameter("articleId");
            if (!StrUtil.empty(articleid)) {
                Long id = (long) StrUtil.parseInt(articleid, -1);
                article = articleService.getArticleById(id);
                if (article != null) {
                    article.setTitle(title);
                    article.setContent(text);
                    article.setChannelId(chid);
                    article.setUpdateTime(new Date());
                    articleService.updateArticle(article);
                }
            }
            if (article == null) {
                article = new Article(title, text, chid);
                article.setCreateTime(new Date());
                article.setCreateUserId(user.getId());
                article.setUpdateTime(new Date());
                article.setIfShow(true);
                article.setIsDeleted(false);
                article.setRank(100);
                articleService.saveArticle(article);
            }
            Channel channel = channelService.getChannelById(chid);
            rsmap.put("channel", channel);
            rsmap.put("result", "isPosted");
        } else {
            rsmap.put("result", "notPosted");
        }
        return new ModelAndView("manage/result", rsmap);
    }
