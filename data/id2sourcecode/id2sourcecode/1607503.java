    @Override
    public String execute() throws Exception {
        ismoder = 0;
        disablepostctrl = 0;
        quickpost = 1;
        forumnav = "";
        topictitle = "";
        ignorelink = "";
        forumid = 0;
        firstpagesmilies = cachesManager.getSmiliesFirstPageCache();
        if (useradminid != 1 && usergroupinfo.getDisableperiodctrl() != 1) {
            String visittime = scoresetManager.betweenTime(config.getPostbanperiods());
            if (!visittime.equals("")) {
                quickpost = 0;
            }
        }
        headerad = "";
        footerad = "";
        pagewordad = new String[0];
        doublead = "";
        floatad = "";
        inpostad = "";
        quickeditorad = "";
        String[] quickbgad = new String[0];
        if (quickbgad.length > 1) {
            quickbgadimg = quickbgad[0];
            quickbgadlink = quickbgad[1];
        }
        topicid = LForumRequest.getParamIntValue("topicid", -1);
        postid = LForumRequest.getParamIntValue("postid", -1);
        if (topicid != -1) {
            postid = LForumRequest.getParamIntValue("postid", -1);
            if (postid == -1) {
                postid = postManager.getFirstPostId(topicid);
            }
        }
        Posts postinfo = postManager.getPostInfo(postid);
        if (postinfo == null) {
            reqcfg.addErrLine("错误的主题");
            return SUCCESS;
        } else {
            if (topicid == -1) {
                topicid = postinfo.getTopics().getTid();
            }
        }
        if (topicid != postinfo.getTopics().getTid()) {
            reqcfg.addErrLine("主题ID无效");
            return SUCCESS;
        }
        smilies = cachesManager.getSmiliesCache();
        smilietypes = cachesManager.getSmilieTypesCache();
        parseurloff = 0;
        usesig = config.getShowsignatures();
        topic = topicManager.getTopicInfo(topicid);
        if (topic == null) {
            reqcfg.addErrLine("不存在的主题ID");
            return SUCCESS;
        }
        if (topic.getDisplayorder() == -1) {
            reqcfg.addErrLine("此主题已被删除！");
            return SUCCESS;
        }
        if (topic.getDisplayorder() == -2) {
            reqcfg.addErrLine("此主题未经审核！");
            return SUCCESS;
        }
        if (topic.getIdentify() > 0) {
            topicidentify = cachesManager.getTopicIdentify(topic.getIdentify());
            if (topic.getIdentify() != topicidentify.getIdentifyid()) {
                topicAdminManager.identifyTopic(topicid + "", 0);
            }
        }
        forumid = topic.getForums().getFid();
        ismoder = moderatorManager.isModer(useradminid, userid, forumid) ? 1 : 0;
        admininfo = adminGroupManager.getAdminGroup(useradminid);
        if (admininfo != null) {
            disablepostctrl = admininfo.getDisablepostctrl();
        }
        if (topic.getReadperm() > usergroupinfo.getReadaccess() && topic.getUsersByPosterid().getUid() != userid && useradminid != 1 && ismoder == 0) {
            reqcfg.addErrLine("本主题阅读权限为: " + topic.getReadperm() + ", 您当前的身份 \"" + usergroupinfo.getGrouptitle() + "\" 阅读权限不够");
            if (userid == -1) {
                needlogin = true;
            }
            return SUCCESS;
        }
        topictitle = topic.getTitle().trim();
        replytitle = topictitle;
        if (replytitle.length() >= 50) {
            replytitle = Utils.format(replytitle, 50, true);
        }
        topicviews = topic.getViews() + 1;
        forum = forumManager.getForumInfo(forumid);
        enabletag = (config.getEnabletag() & forum.getAllowtag()) == 1;
        forumname = forum.getName();
        pagetitle = topic.getTitle();
        forumnav = forum.getPathlist().trim();
        navhomemenu = cachesManager.getForumListMenuDiv(usergroupid, userid, "");
        smileyoff = 1 - forum.getAllowsmilies();
        bbcodeoff = 1;
        if (forum.getAllowbbcode() == 1) {
            if (usergroupinfo.getAllowcusbbcode() == 1) {
                bbcodeoff = 0;
            }
        }
        if (!forum.getForumfields().getPassword().equals("") && !MD5.encode(forum.getForumfields().getPassword()).equals(ForumUtils.getCookie("forum" + forumid + "password"))) {
            reqcfg.addErrLine("本版块被管理员设置了密码");
            response.sendRedirect("showforum.action?forumid=" + forumid);
            return null;
        }
        if (!forumManager.allowViewByUserID(forum.getForumfields().getPermuserlist(), userid)) {
            if (Utils.null2String(forum.getForumfields().getViewperm()).equals("")) {
                if (usergroupinfo.getAllowvisit() != 1) {
                    reqcfg.addErrLine("您当前的身份 \"" + usergroupinfo.getGrouptitle() + "\" 没有浏览该版块的权限");
                    if (userid == -1) {
                        needlogin = true;
                    }
                    return SUCCESS;
                }
            } else {
                if (!forumManager.allowView(forum.getForumfields().getViewperm(), usergroupid)) {
                    reqcfg.addErrLine("您没有浏览该版块的权限");
                    if (userid == -1) {
                        needlogin = true;
                    }
                    return SUCCESS;
                }
            }
        }
        if (forumManager.allowReplyByUserID(forum.getForumfields().getPermuserlist().trim(), userid)) {
            isshowreplaylink = true;
        } else {
            if (forum.getForumfields().getReplyperm().trim().equals("")) {
                if (usergroupinfo.getAllowreply() == 1) {
                    isshowreplaylink = true;
                }
            } else if (forumManager.allowReply(forum.getForumfields().getReplyperm().trim(), usergroupid)) {
                isshowreplaylink = true;
            }
        }
        if (forumManager.allowGetAttachByUserID(forum.getForumfields().getPermuserlist(), userid)) {
            allowdownloadattach = true;
        } else {
            if (forum.getForumfields().getGetattachperm().trim().equals("")) {
                if (usergroupinfo.getAllowgetattach() == 1) {
                    allowdownloadattach = true;
                }
            } else if (forumManager.allowGetAttach(forum.getForumfields().getGetattachperm().trim(), usergroupid)) {
                allowdownloadattach = true;
            }
        }
        topictypes = Utils.null2String(cachesManager.getTopicTypeArray().get(topic.getTopictypes().getTypeid()));
        topictypes = topictypes != "" ? "[" + topictypes + "]" : "";
        userextcreditsinfo = scoresetManager.getScoreSet(scoresetManager.getCreditsTrans());
        if (quickpost == 1) {
            if (userid > -1 && forumManager.allowPostByUserID(forum.getForumfields().getPermuserlist(), userid)) {
                canposttopic = true;
            }
            if (forum.getForumfields().getPostperm().trim().equals("")) {
                if (usergroupinfo.getAllowpost() == 1) {
                    canposttopic = true;
                }
            } else if (forumManager.allowPost(forum.getForumfields().getPostperm().trim(), usergroupid)) {
                canposttopic = true;
            }
        }
        if (topic.getClosed() == 0 || ismoder == 1) {
            if (config.getFastpost() == 2 || config.getFastpost() == 3) {
                if (forumManager.allowReplyByUserID(forum.getForumfields().getPermuserlist(), userid)) {
                    canreply = true;
                } else {
                    if (forum.getForumfields().getReplyperm().trim().equals("")) {
                        if (usergroupinfo.getAllowreply() == 1) {
                            canreply = true;
                        }
                    } else if (forumManager.allowReply(forum.getForumfields().getReplyperm(), usergroupid)) {
                        canreply = true;
                    }
                }
            }
        }
        showratelog = config.getDisplayratecount() > 0 ? 1 : 0;
        int price = 0;
        if (topic.getSpecial() == 0) {
            if (topic.getPrice() > 0 && userid != topic.getUsersByPosterid().getUid() && ismoder != 1) {
                price = topic.getPrice();
                if (paymentLogManager.isBuyer(topicid, userid) || (Utils.strDateDiffHours(topic.getPostdatetime(), scoresetManager.getMaxChargeSpan()) > 0 && scoresetManager.getMaxChargeSpan() != 0)) {
                    price = -1;
                }
            }
            if (price > 0) {
                response.sendRedirect("buytopic.action?topicid=" + topic.getTid());
                return null;
            }
        }
        if (newpmcount > 0) {
            pmlist = messageManager.getPrivateMessageListForIndex(userid, 5, 1, 1);
            showpmhint = userManager.getUserInfo(userid).getNewsletter() > 4;
        }
        ispoll = false;
        allowvote = false;
        Map<Integer, Boolean> tmp = new HashMap<Integer, Boolean>();
        if (topic.getSpecial() == 1) {
            ispoll = true;
            polloptionlist = pollManager.getPollOptionList(topicid);
            pollinfo = pollManager.getPollInfo(topicid);
            voters = pollManager.getVoters(topicid, userid, username, tmp);
            allowvote = tmp.get(0);
            if (pollinfo.getUsers().getUid() != userid && useradminid != 1) {
                if (pollinfo.getVisible() == 1 && (allowvote || (userid == -1 && !Utils.inArray(topicid + "", ForumUtils.getCookie("polled"))))) {
                    showpollresult = false;
                }
            }
        }
        if (topic.getSpecial() == 3) {
            bonuslogs = bonuManager.getLogs(topic.getTid());
        }
        SimpleDateFormat sdf = new SimpleDateFormat(Utils.FULL_DATEFORMAT);
        if (ispoll && sdf.parse(pollinfo.getExpiration()).before(Utils.getNowDate())) {
            allowvote = false;
        }
        postcount = topic.getReplies() + 1;
        int hide = 1;
        if (topic.getHide() == 1 && (postManager.isReplier(topicid, userid) || ismoder == 1)) {
            hide = -1;
        }
        PostpramsInfo postpramsInfo = new PostpramsInfo();
        postpramsInfo.setPid(postid);
        postpramsInfo.setFid(forum.getFid());
        postpramsInfo.setTid(topicid);
        postpramsInfo.setJammer(forum.getJammer());
        postpramsInfo.setPagesize(1);
        postpramsInfo.setPageindex(1);
        postpramsInfo.setGetattachperm(forum.getForumfields().getGetattachperm());
        postpramsInfo.setUsergroupid(usergroupid);
        postpramsInfo.setAttachimgpost(config.getAttachimgpost());
        postpramsInfo.setShowattachmentpath(config.getShowattachmentpath());
        postpramsInfo.setHide(hide);
        postpramsInfo.setPrice(price);
        postpramsInfo.setUbbmode(false);
        postpramsInfo.setUsergroupreadaccess(usergroupinfo.getReadaccess());
        if (ismoder == 1) {
            postpramsInfo.setUsergroupreadaccess(Integer.MAX_VALUE);
        }
        postpramsInfo.setCurrentuserid(userid);
        postpramsInfo.setShowimages(forum.getAllowimgcode());
        postpramsInfo.setSmiliesinfo(smilieManager.getSmiliesListWithInfo());
        postpramsInfo.setCustomeditorbuttoninfo(editorManager.getCustomEditButtonListWithInfo());
        postpramsInfo.setSmiliesmax(config.getSmiliesmax());
        postpramsInfo.setBbcodemode(0);
        postpramsInfo.setCurrentusergroup(usergroupinfo);
        postpramsInfo.setCondition("");
        Map<Integer, List<ShowtopicPageAttachmentInfo>> map = new HashMap<Integer, List<ShowtopicPageAttachmentInfo>>();
        map.put(0, attachmentlist);
        post = postManager.getSinglePost(postpramsInfo, map, ismoder == 1);
        attachmentlist = map.get(0);
        if (post == null) {
            reqcfg.addErrLine("读取信息失败");
            return SUCCESS;
        }
        posttree = postManager.getPostTree(topicid);
        topicStatManager.track(topicid, 1);
        onlineUserManager.updateAction(olid, ForumAction.ShowTopic.ACTION_ID, forumid, forumname, topicid, topictitle, config.getOnlinetimeout());
        score = scoresetManager.getValidScoreName();
        scoreunit = scoresetManager.getValidScoreUnit();
        if (userid != -1) {
            Users userinfo = userManager.getUserInfo(userid);
            if (userinfo != null) {
                if (userinfo.getNewpm() == 0) {
                    newpmcount = 0;
                }
            }
        }
        return SUCCESS;
    }
