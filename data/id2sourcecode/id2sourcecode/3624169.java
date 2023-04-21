    @SuppressWarnings("unchecked")
    public ActionForward forumdisplay(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute("jsprun_action", "2");
        short fid = (short) Common.toDigit(request.getParameter("fid"));
        List<Map<String, String>> forums = dataBaseService.executeQuery("SELECT f.*, ff.* FROM jrun_forums f LEFT JOIN jrun_forumfields ff ON ff.fid=f.fid WHERE f.fid='" + fid + "'");
        if (forums != null && forums.size() > 0) {
            Map<String, String> forum = forums.get(0);
            Map<String, String> settings = (Map<String, String>) request.getAttribute("settings");
            short fup = Short.valueOf(forum.get("fup"));
            String type = forum.get("type");
            String name = forum.get("name");
            String redirect = null;
            if (forum.get("redirect").length() > 0) {
                redirect = forum.get("redirect");
            } else if ("group".equals(type)) {
                redirect = settings.get("indexname") + "?gid" + fid;
            }
            if (redirect != null) {
                try {
                    response.sendRedirect(redirect);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
            request.setAttribute("fid", fid);
            short styleid = Short.valueOf(forum.get("styleid"));
            if (styleid > 0) {
                request.setAttribute("styleid", styleid);
            }
            int rewritestatus = Integer.parseInt(settings.get("rewritestatus"));
            boolean forumdisplayurl = (rewritestatus & 1) > 0;
            String navtitle = "";
            if (type.equals("forum")) {
                request.setAttribute("navigation", "&raquo; " + name);
                navtitle = Common.strip_tags(name);
            } else {
                Map<String, String> sub = dataBaseService.executeQuery("SELECT name FROM jrun_forums WHERE fid=" + fup + " limit 1").get(0);
                String subName = Common.strip_tags(sub.get("name"));
                if (forumdisplayurl) {
                    request.setAttribute("navigation", "&raquo; <a href=\"forum-" + fup + "-1.html\">" + subName + "</a> &raquo; " + name);
                } else {
                    request.setAttribute("navigation", "&raquo; <a href=\"forumdisplay.jsp?fid=" + fup + "\">" + subName + "</a> &raquo; " + name);
                }
                navtitle = Common.strip_tags(name) + " - " + subName;
            }
            request.setAttribute("navtitle", navtitle + " - ");
            request.setAttribute("metakeywords", forum.get("keywords").length() > 0 ? forum.get("keywords") : Common.strip_tags(name));
            request.setAttribute("metadescription", forum.get("description").length() > 0 ? Common.strip_tags(forum.get("description")) : Common.strip_tags(name));
            request.setAttribute("google_searchbox", Integer.valueOf(settings.get("google_searchbox")) & 1);
            request.setAttribute("baidu_searchbox", Integer.valueOf(settings.get("baidu_searchbox")) & 1);
            HttpSession session = request.getSession();
            Members member = (Members) session.getAttribute("user");
            String extgroupid = member != null ? member.getExtgroupids() : null;
            short groupid = (Short) session.getAttribute("jsprun_groupid");
            String viewperm = forum.get("viewperm");
            int creditstrans = Integer.valueOf(settings.get("creditstrans"));
            if (!viewperm.equals("") && !Common.forumperm(viewperm, groupid, extgroupid)) {
                request.setAttribute("show_message", "�����ֻ���ض��û�����Է��ʡ�");
                return mapping.findForward("nopermission");
            }
            boolean ismoderator = Common.ismoderator(fid, member);
            request.setAttribute("ismoderator", ismoderator);
            Map<Integer, Map<String, String>> extcredits = dataParse.characterParse(settings.get("extcredits"), false);
            String formulaperm = forum.get("formulaperm");
            if (formulaperm.length() > 0) {
                Map<String, String> messages = Common.forumformulaperm(formulaperm, member, ismoderator, extcredits);
                if (messages != null) {
                    request.setAttribute("show_message", "��������������������������</b><br />&nbsp;&nbsp;&nbsp;��������: " + messages.get("formulamessage") + "<br />&nbsp;&nbsp;&nbsp;�����Ϣ: " + messages.get("usermsg") + "<b>");
                    return mapping.findForward("nopermission");
                }
            }
            String password = forum.get("password");
            if (!"".equals(password)) {
                String pwverify = request.getParameter("pwverify");
                if ("true".equals(pwverify)) {
                    String pw = request.getParameter("pw");
                    if (password.equals(pw)) {
                        CookieUtil.setCookie(request, response, "fidpw" + fid, pw, 31536000, true, settings);
                        session.setAttribute("fidpw" + fid, password);
                        request.setAttribute("successInfo", "������֤�ɹ������ڽ�ת�������б?");
                        request.setAttribute("requestPath", "forumdisplay.jsp?fid=" + fid);
                        return mapping.findForward("showMessage");
                    } else {
                        request.setAttribute("resultInfo", "����������벻��ȷ�����ܷ��������顣");
                        return mapping.findForward("showMessage");
                    }
                }
                String pw = CookieUtil.getCookie(request, "fidpw" + fid, true, settings);
                if (!password.equals(pw)) {
                    return mapping.findForward("toForumdisplay_passwd");
                }
            }
            boolean spaceurlurl = (rewritestatus & 4) > 0;
            request.setAttribute("moderatedby", Common.moddisplay(forum.get("moderators"), "forumdisplay", false, spaceurlurl));
            int timestamp = (Integer) request.getAttribute("timestamp");
            String jsprun_collapse = CookieUtil.getCookie(request, "jsprun_collapse", false, settings);
            Map<String, String> collapse = new HashMap<String, String>();
            if (forum.get("rules").length() > 0) {
                if (jsprun_collapse != null && jsprun_collapse.indexOf("rules") > 0) {
                    collapse.put("rules", "display: none");
                    collapse.put("rules_link", "");
                } else {
                    collapse.put("rules", "");
                    collapse.put("rules_link", "display: none");
                }
            }
            Map modrecommend = dataParse.characterParse(forum.get("modrecommend"), false);
            if (modrecommend != null && "1".equals(modrecommend.get("open"))) {
                request.setAttribute("modrecommend", modrecommend);
                List<Map<String, String>> recommendlist = recommendupdate(fid, modrecommend, false, timestamp);
                if (recommendlist != null && recommendlist.size() > 0) {
                    if (jsprun_collapse != null && jsprun_collapse.indexOf("recommendlist") > 0) {
                        collapse.put("recommendlist", "display: none");
                        collapse.put("recommendlist_link", "");
                    } else {
                        collapse.put("recommendlist", "");
                        collapse.put("recommendlist_link", "display: none");
                    }
                    request.setAttribute("recommendlist", recommendlist);
                }
            }
            if (member != null && member.getNewpm() > 0) {
                Map<String, String> announcementsMap = (Map<String, String>) request.getAttribute("announcements");
                Map<Integer, Map> announcements = dataParse.characterParse(announcementsMap != null ? announcementsMap.get("announcements") : null, true);
                List<Map<String, String>> pmlists = new ArrayList<Map<String, String>>();
                ;
                if (announcements != null && announcements.size() > 0) {
                    int announcepm = 0;
                    Set<Integer> keys = announcements.keySet();
                    for (Integer key : keys) {
                        Map<String, String> announcement = announcements.get(key);
                        if (announcement.get("type") != null && announcement.get("type").equals("2")) {
                            announcement.put("announce", "true");
                            pmlists.add(announcement);
                            announcepm++;
                        }
                    }
                    request.setAttribute("announcepm", announcepm);
                }
                List<Map<String, String>> maps = dataBaseService.executeQuery("SELECT pmid, msgfrom, msgfromid, subject, message FROM jrun_pms WHERE msgtoid=" + member.getUid() + " AND folder='inbox' AND delstatus!='2' AND new='1'");
                int newpmnum = maps != null ? maps.size() : 0;
                if (newpmnum > 0 && newpmnum <= 10) {
                    pmlists.addAll(maps);
                }
                request.setAttribute("newpmnum", newpmnum);
                request.setAttribute("pmlists", pmlists.size() > 0 ? pmlists : null);
            }
            int timeoffset = (int) ((Float) session.getAttribute("timeoffset") * 3600);
            String timeformat = (String) session.getAttribute("timeformat");
            String dateformat = (String) session.getAttribute("dateformat");
            SimpleDateFormat sdf_all = new SimpleDateFormat(dateformat + " " + timeformat);
            byte accessmasks = member != null ? member.getAccessmasks() : 0;
            int jsprun_uid = (Integer) session.getAttribute("jsprun_uid");
            String sql = accessmasks > 0 ? "SELECT f.fid, f.fup, f.type, f.name, f.threads, f.posts, f.todayposts, f.lastpost, f.inheritedmod, f.forumcolumns, f.simple, ff.description, ff.moderators, ff.icon, ff.viewperm, ff.redirect, a.allowview FROM jrun_forums f LEFT JOIN jrun_forumfields ff ON ff.fid=f.fid LEFT JOIN jrun_access a ON a.uid=" + jsprun_uid + " AND a.fid=f.fid WHERE fup=" + fid + " AND f.status>0 AND type='sub' ORDER BY f.displayorder" : "SELECT f.fid, f.fup, f.type, f.name, f.threads, f.posts, f.todayposts, f.lastpost, f.inheritedmod, f.forumcolumns, f.simple, ff.description, ff.moderators, ff.icon, ff.viewperm, ff.redirect FROM jrun_forums f LEFT JOIN jrun_forumfields ff USING(fid) WHERE fup=" + fid + " AND f.status>0 AND type='sub' ORDER BY f.displayorder";
            List<Map<String, String>> maps = dataBaseService.executeQuery(sql);
            if (maps != null && maps.size() > 0) {
                Map<String, Map<String, String>> lastposts = new TreeMap<String, Map<String, String>>();
                List<Map<String, String>> subforums = new ArrayList<Map<String, String>>();
                String hideprivate = settings.get("hideprivate");
                Integer lastvisit = member != null ? member.getLastvisit() : null;
                for (Map<String, String> forumMap : maps) {
                    if (Common.forum(forumMap, hideprivate, groupid, lastvisit, extgroupid, lastposts, sdf_all, timeoffset, rewritestatus)) {
                        subforums.add(forumMap);
                    }
                }
                short forumcolumns = Short.valueOf(forum.get("forumcolumns"));
                if (forumcolumns > 0) {
                    int colspan = subforums.size() % forumcolumns;
                    if (colspan > 0) {
                        StringBuffer endrows = new StringBuffer();
                        while (forumcolumns - colspan > 0) {
                            endrows.append("<td>&nbsp;</td>");
                            colspan++;
                        }
                        request.setAttribute("endrows", endrows);
                    }
                    request.setAttribute("forumcolwidth", Math.floor(100 / forumcolumns) + "%");
                }
                Map<String, String> collapseimg = new HashMap<String, String>();
                if (jsprun_collapse != null && jsprun_collapse.indexOf("subforum_" + fid) > 0) {
                    collapse.put("subforum", "display: none");
                    collapseimg.put("subforum", "collapsed_yes.gif");
                } else {
                    collapse.put("subforum", "");
                    collapseimg.put("subforum", "collapsed_no.gif");
                }
                request.setAttribute("collapseimg", collapseimg);
                request.setAttribute("lastposts", lastposts);
                request.setAttribute("subforums", subforums.size() > 0 ? subforums : null);
            }
            maps = null;
            if (settings.get("forumjump").equals("1")) {
                List<Map<String, String>> forumlist = dataParse.characterParse(settings.get("forums"));
                if ("1".equals(settings.get("jsmenu_1"))) {
                    request.setAttribute("forummenu", Common.forumselect(forumlist, groupid, extgroupid, String.valueOf(fid), forumdisplayurl));
                } else {
                    request.setAttribute("forumselect", Common.forumselect(forumlist, false, false, groupid, extgroupid, null, forumdisplayurl));
                }
            }
            request.setAttribute("forum", forum);
            request.setAttribute("threadsticky", settings.get("threadsticky").split(","));
            String viewthreadurl = settings.get("rewritestatus");
            boolean viewthread = (Integer.valueOf(viewthreadurl) & 2) > 0;
            short simple = Short.valueOf(forum.get("simple"));
            if ((simple & 1) > 0) {
                return mapping.findForward("toForumdisplay_simple");
            }
            int subforumsindex = -1;
            int defaultorderfield = 0;
            int defaultorder = 0;
            defaultorderfield = simple / 64;
            simple %= 64;
            defaultorder = simple / 32;
            simple %= 32;
            if (simple >= 16) {
                subforumsindex = 1;
                simple -= 16;
            }
            if (simple >= 8) {
                subforumsindex = 0;
                simple -= 8;
            }
            request.setAttribute("simple", simple);
            request.setAttribute("subforumsindex", subforumsindex);
            request.setAttribute("defaultorderfield", defaultorderfield);
            request.setAttribute("defaultorder", defaultorder);
            String filteradd = null;
            StringBuffer forumdisplayadd = new StringBuffer();
            int threadcount = 0;
            String filter = request.getParameter("filter");
            int typeid = Common.toDigit(request.getParameter("typeid"), 255L, 0L).intValue();
            if (filter == null) {
                threadcount = Integer.valueOf(forum.get("threads"));
                filteradd = " ";
            } else {
                if ("digest".equals(filter)) {
                    forumdisplayadd.append("&amp;filter=digest");
                    filteradd = " AND digest>'0'";
                } else if ("type".equals(filter) && typeid > 0) {
                    forumdisplayadd.append("&amp;filter=type&amp;typeid=" + typeid);
                    filteradd = " AND typeid=" + typeid;
                } else if (filter.matches("^\\d+$")) {
                    forumdisplayadd.append("&amp;filter=" + filter);
                    filteradd = Integer.valueOf(filter) > 0 ? " AND lastpost>='" + (timestamp - Integer.valueOf(filter)) + "'" : "";
                } else if (specialtype.get(filter) != null) {
                    forumdisplayadd.append("&amp;filter=" + filter);
                    filteradd = " AND special='" + specialtype.get(filter) + "'";
                } else {
                    filteradd = " ";
                    filter = "";
                }
                List<Map<String, String>> count = dataBaseService.executeQuery("SELECT COUNT(*) count FROM jrun_threads WHERE fid=" + fid + filteradd + " AND displayorder>='0'");
                threadcount = Integer.valueOf(count.get(0).get("count"));
            }
            String orderby = request.getParameter("orderby");
            String ascdesc = request.getParameter("ascdesc");
            if (orderby != null && orderfields.contains(orderby)) {
                forumdisplayadd.append("&amp;orderby=" + orderby);
            } else {
                orderby = orderfields.get(defaultorderfield);
            }
            if (ascdesc != null && order.contains(ascdesc)) {
                forumdisplayadd.append("&amp;ascdesc=" + ascdesc);
            } else {
                ascdesc = order.get(defaultorder);
            }
            Map<String, String> checked = new HashMap<String, String>();
            checked.put(filter, "selected='selected'");
            checked.put(orderby, "selected='selected'");
            checked.put(ascdesc, "selected='selected'");
            request.setAttribute("checked", checked);
            Map threadtypes = dataParse.characterParse(forum.get("threadtypes"), true);
            request.setAttribute("threadtypes", threadtypes.size() > 0 ? threadtypes : null);
            int globalstick = Common.toDigit(settings.get("globalstick"), 255L, 0L).intValue();
            String thisgid = String.valueOf(type.equals("forum") ? fup : forumService.findById(fup).getFup());
            request.setAttribute("thisgid", thisgid);
            int stickycount = 0;
            StringBuffer stickytids = new StringBuffer();
            if (globalstick > 0) {
                Map<String, String> globalstickMap = (Map<String, String>) request.getAttribute("globalstick");
                if (globalstickMap != null) {
                    Map<String, Map<String, String>> globalsticks = dataParse.characterParse(globalstickMap.get("globalstick"), false);
                    if (globalsticks != null) {
                        Map<String, String> global = globalsticks.get("global");
                        Map<String, String> categories = globalsticks.get(thisgid);
                        if (global != null) {
                            stickytids.append(global.get("tids"));
                            stickycount = Integer.valueOf(global.get("count"));
                        }
                        if (categories != null) {
                            stickytids.append(stickytids.length() == 0 ? categories.get("tids") : "," + categories.get("tids"));
                            stickycount += Integer.valueOf(categories.get("count"));
                        }
                    }
                }
            }
            boolean filterbool = filter != null && "|digest|type|activity|poll|trade|reward|debate|video|".contains("|" + filter + "|");
            threadcount += filterbool ? 0 : stickycount;
            Long threadmaxpages = Long.valueOf(settings.get("threadmaxpages"));
            int page = Common.toDigit(request.getParameter("page"), threadmaxpages, 1L).intValue();
            int tpp = member != null && member.getTpp() > 0 ? member.getTpp() : Integer.valueOf(settings.get("topicperpage"));
            Map<String, Integer> multiInfo = Common.getMultiInfo(threadcount, tpp, page);
            page = multiInfo.get("curpage");
            int start_limit = multiInfo.get("start_limit");
            String extra = Common.encode("page=" + page + forumdisplayadd);
            boolean isstaticurl = false;
            String url = null;
            if (viewthread && forumdisplayadd.length() == 0) {
                isstaticurl = true;
                url = "forum-" + fid;
                request.setAttribute("extra", page);
            } else {
                url = "forumdisplay.jsp?fid=" + fid + forumdisplayadd;
                request.setAttribute("extra", extra);
            }
            Map<String, Object> multi = Common.multi(threadcount, tpp, page, url, threadmaxpages.intValue(), 10, true, false, null, isstaticurl);
            request.setAttribute("page", page);
            request.setAttribute("url", url);
            request.setAttribute("isstaticurl", isstaticurl);
            request.setAttribute("multi", multi);
            String displayorderadd = !filterbool && stickycount > 0 ? "t.displayorder IN (0, 1)" : "t.displayorder IN (0, 1, 2, 3)";
            String querysticky = null;
            String query = null;
            if ((start_limit > 0 && start_limit > stickycount) || stickycount == 0) {
                query = "SELECT t.* FROM jrun_threads t WHERE t.fid='" + fid + "' " + filteradd + " AND " + displayorderadd + " ORDER BY t.displayorder DESC, t." + orderby + " " + ascdesc + " LIMIT " + (filterbool ? start_limit : start_limit - stickycount) + ", " + tpp;
            } else {
                querysticky = "SELECT t.* FROM jrun_threads t WHERE t.tid IN (" + stickytids + ") AND t.displayorder IN (2, 3) ORDER BY displayorder DESC, " + orderby + " " + ascdesc + " LIMIT " + start_limit + ", " + (stickycount - start_limit < tpp ? stickycount - start_limit : tpp);
                if (tpp - stickycount + start_limit > 0) {
                    query = "SELECT t.* FROM jrun_threads t WHERE t.fid=" + fid + " " + filteradd + " AND " + displayorderadd + " ORDER BY displayorder DESC, " + orderby + " " + ascdesc + " LIMIT " + (tpp - stickycount + start_limit);
                }
            }
            Map<String, String> announcement = (Map<String, String>) request.getAttribute("announcement");
            if (announcement != null && announcement.size() > 0) {
                announcement.put("starttime", Common.gmdate(dateformat, Integer.valueOf(announcement.get("starttime")) + timeoffset));
                request.setAttribute("announcement", announcement);
            }
            List<Map<String, String>> threadlists = new ArrayList<Map<String, String>>();
            if (querysticky != null) {
                threadlists = dataBaseService.executeQuery(querysticky);
            }
            if (query != null) {
                threadlists.addAll(dataBaseService.executeQuery(query));
            }
            if (threadlists != null && threadlists.size() > 0) {
                Map types = null;
                byte prefix = 0;
                if (threadtypes.size() > 0) {
                    types = (Map) threadtypes.get("types");
                    prefix = (Byte) threadtypes.get("prefix");
                }
                int postperpage = Integer.valueOf(settings.get("postperpage"));
                int ppp = Byte.valueOf(forum.get("threadcaches")) > 0 && jsprun_uid == 0 ? postperpage : (member != null && member.getPpp() > 0 ? member.getPpp() : postperpage);
                SimpleDateFormat sdf_dateformat = new SimpleDateFormat(dateformat);
                String closedby = null;
                short autoclose = Short.valueOf(forum.get("autoclose"));
                if (autoclose != 0) {
                    closedby = autoclose > 0 ? "dateline" : "lastpost";
                    autoclose = (short) (Math.abs(autoclose) * 86400);
                }
                int separatepos = 0;
                for (Map<String, String> thread : threadlists) {
                    int thread_typeid = Integer.valueOf(thread.get("typeid"));
                    if (thread_typeid > 0 && prefix == 1 && types.get(thread_typeid) != null) {
                        thread.put("type", "<em>[<a href=\"forumdisplay.jsp?fid=" + fid + "&amp;filter=type&amp;typeid=" + thread_typeid + "\">" + types.get(thread_typeid) + "</a>]</em>");
                    }
                    int replies = Integer.valueOf(thread.get("replies"));
                    int special = Integer.valueOf(thread.get("special"));
                    int topicpages = special > 0 ? replies : replies + 1;
                    if (topicpages > ppp) {
                        StringBuffer pagelinks = new StringBuffer();
                        topicpages = (int) (Math.ceil((double) topicpages / (double) ppp));
                        for (int i = 1; i <= 6 && i <= topicpages; i++) {
                            if (viewthread) {
                                pagelinks.append("<a href=\"thread-" + thread.get("tid") + "-" + page + "-" + i + ".html\">" + i + "</a> ");
                            } else {
                                pagelinks.append("<a href=\"viewthread.jsp?tid=" + thread.get("tid") + "&amp;extra=" + extra + "&amp;page=" + i + "\">" + i + "</a> ");
                            }
                        }
                        if (topicpages > 6) {
                            if (viewthread) {
                                pagelinks.append(" .. <a href=\"thread-" + thread.get("tid") + "-" + page + "-" + topicpages + ".html\">" + topicpages + "</a> ");
                            } else {
                                pagelinks.append(" .. <a href=\"viewthread.jsp?tid=" + thread.get("tid") + "&amp;extra=" + extra + "&amp;page=" + topicpages + "\">" + topicpages + "</a> ");
                            }
                        }
                        thread.put("multipage", " &nbsp; " + pagelinks);
                    }
                    int highlight = Integer.valueOf(thread.get("highlight"));
                    if (highlight > 0) {
                        StringBuffer style = new StringBuffer();
                        style.append(" style=\"");
                        style.append(highlight >= 40 ? "font-weight: bold;" : "");
                        highlight = highlight % 40;
                        style.append(highlight >= 20 ? "font-style: italic;" : "");
                        highlight = highlight % 20;
                        style.append(highlight >= 10 ? "text-decoration: underline;" : "");
                        highlight = highlight % 10;
                        style.append("color: " + colorarray[highlight]);
                        style.append("\"");
                        thread.put("highlight", style.toString());
                    } else {
                        thread.put("highlight", "");
                    }
                    int closed = Integer.valueOf(thread.get("closed"));
                    if (closed != 0 || (autoclose != 0 && (timestamp - Integer.valueOf(thread.get(closedby)) > autoclose))) {
                        thread.put("new", "0");
                        if (closed > 1) {
                            thread.put("moved", thread.get("tid"));
                            thread.put("tid", String.valueOf(closed));
                            thread.put("replies", "-");
                            thread.put("views", "-");
                        }
                        thread.put("folder", "lock");
                    } else {
                        thread.put("folder", "common");
                        if (member != null && member.getLastvisit() < Integer.valueOf(thread.get("lastpost"))) {
                            thread.put("new", "1");
                            thread.put("folder", "new");
                        } else {
                            thread.put("new", "0");
                        }
                        if (Integer.valueOf(thread.get("replies")) > Integer.valueOf(thread.get("views"))) {
                            thread.put("views", thread.get("replies"));
                        }
                        if (Integer.valueOf(thread.get("replies")) > Integer.valueOf(settings.get("hottopic"))) {
                            thread.put("folder", "hot");
                        }
                    }
                    thread.put("dateline", Common.gmdate(sdf_dateformat, Integer.valueOf(thread.get("dateline")) + timeoffset));
                    thread.put("lastpost", Common.gmdate(sdf_all, Integer.valueOf(thread.get("lastpost")) + timeoffset));
                    int displayorder = Integer.valueOf(thread.get("displayorder"));
                    if (displayorder == 1 || displayorder == 2 || displayorder == 3) {
                        thread.put("id", "stickthread_" + thread.get("tid"));
                        separatepos++;
                    } else if (displayorder == 4 || displayorder == 5) {
                        thread.put("id", "floatthread_" + thread.get("tid"));
                        thread.put("displaystyle", "style='display:none'");
                    } else {
                        thread.put("id", "normalthread_" + thread.get("tid"));
                    }
                }
                separatepos = separatepos > 0 ? separatepos + 1 : (announcement != null && announcement.size() > 0 ? 1 : 0);
                request.setAttribute("threadlists", threadlists != null && threadlists.size() > 0 ? threadlists : null);
                request.setAttribute("creditstrans", creditstrans);
                request.setAttribute("extcredits", extcredits);
                request.setAttribute("separatepos", separatepos);
            }
            String postperm = forum.get("postperm");
            Map<String, String> usergroups = (Map<String, String>) request.getAttribute("usergroups");
            boolean allowpost = (("".equals(postperm)) && Integer.valueOf(usergroups.get("allowpost")).intValue() > 0) || ((!"".equals(postperm)) && Common.forumperm(postperm, groupid, extgroupid));
            boolean showpoll = false;
            boolean showtrade = false;
            boolean showreward = false;
            boolean showactivity = false;
            boolean showdebate = false;
            boolean showvideo = false;
            short allowpostspecial = Short.valueOf(forum.get("allowpostspecial"));
            if (allowpostspecial > 0) {
                showpoll = (allowpostspecial & 1) > 0;
                showtrade = (allowpostspecial & 2) > 0;
                showreward = (allowpostspecial & 4) > 0;
                showactivity = (allowpostspecial & 8) > 0;
                showdebate = (allowpostspecial & 16) > 0;
                showvideo = (allowpostspecial & 32) > 0 && "1".equals(settings.get("videoopen"));
                request.setAttribute("showpoll", showpoll);
                request.setAttribute("showtrade", showtrade);
                request.setAttribute("showreward", showreward);
                request.setAttribute("showactivity", showactivity);
                request.setAttribute("showdebate", showdebate);
                request.setAttribute("showvideo", showvideo);
            }
            if (allowpost && usergroups != null) {
                request.setAttribute("allowpostpoll", usergroups.get("allowpostpoll").equals("1") && showpoll);
                request.setAttribute("allowposttrade", usergroups.get("allowposttrade").equals("1") && showtrade);
                request.setAttribute("allowpostreward", usergroups.get("allowpostreward").equals("1") && showreward && extcredits.get(creditstrans) != null);
                request.setAttribute("allowpostactivity", usergroups.get("allowpostactivity").equals("1") && showactivity);
                request.setAttribute("allowpostdebate", usergroups.get("allowpostdebate").equals("1") && showdebate);
                request.setAttribute("allowpostvideo", usergroups.get("allowpostvideo").equals("1") && showvideo);
            }
            request.setAttribute("allowpost", allowpost);
            request.setAttribute("typemodels", dataParse.characterParse(forum.get("typemodels"), true));
            request.setAttribute("forumdisplayadd", forumdisplayadd);
            request.setAttribute("filter", filter);
            request.setAttribute("typeid", typeid);
            int whosonlinestatus = Integer.valueOf(settings.get("whosonlinestatus"));
            if (whosonlinestatus == 2 || whosonlinestatus == 3) {
                whosonlinestatus = 1;
                String[] onlineinfo = settings.get("onlinerecord").split("\t");
                String showoldetails = request.getParameter("showoldetails");
                if (showoldetails != null) {
                    if ("no".equals(showoldetails)) {
                        CookieUtil.setCookie(request, response, "onlineforum", "0", 31536000, true, settings);
                    } else if ("yes".equals(showoldetails)) {
                        CookieUtil.setCookie(request, response, "onlineforum", "1", 31536000, true, settings);
                    }
                }
                String onlineforum = CookieUtil.getCookie(request, "onlineforum", true, settings);
                boolean detailstatus = showoldetails != null && showoldetails.equals("yes") || (onlineforum == null && settings.get("whosonline_contract").equals("0")) || "1".equals(onlineforum) && (!"yes".equals(showoldetails)) && Integer.valueOf(onlineinfo[0]) < 500;
                if (detailstatus) {
                    Common.updatesession(request, settings);
                    request.setAttribute("forumname", Common.strip_tags(name));
                    Map<String, String> onlinelist = (Map<String, String>) request.getAttribute("onlinelist");
                    List<Map<String, String>> onlines = dataBaseService.executeQuery("SELECT uid, username, groupid, invisible, action, lastactivity, fid FROM jrun_sessions WHERE fid=" + fid + " " + (onlinelist.get("7") != null ? "" : "AND uid > 0") + " AND invisible='0'");
                    if (onlines != null && onlines.size() > 0) {
                        SimpleDateFormat sdf_timeformat = new SimpleDateFormat((String) session.getAttribute("timeformat"));
                        MessageResources mr = getResources(request);
                        Locale locale = getLocale(request);
                        for (Map<String, String> online : onlines) {
                            if (!online.get("uid").equals("0")) {
                                online.put("icon", onlinelist.get(online.get("groupid")) != null ? onlinelist.get(online.get("groupid")) : onlinelist.get("0"));
                            } else {
                                online.put("icon", onlinelist.get("7"));
                                online.put("username", onlinelist.get("guest"));
                            }
                            online.put("action", mr.getMessage(locale, online.get("action")));
                            online.put("lastactivity", Common.gmdate(sdf_timeformat, Integer.valueOf(online.get("lastactivity")) + timeoffset));
                        }
                        request.setAttribute("whosonline", onlines);
                    }
                }
                request.setAttribute("detailstatus", detailstatus);
            } else {
                whosonlinestatus = 0;
            }
            request.setAttribute("whosonlinestatus", whosonlinestatus > 0 ? true : false);
            int visitedforumcount = Integer.valueOf(settings.get("visitedforums"));
            if (visitedforumcount > 0) {
                Map<Short, String> visitedforums = (Map<Short, String>) session.getAttribute("visitedforums");
                if (visitedforums == null) {
                    visitedforums = new HashMap<Short, String>();
                }
                visitedforums.put(fid, name);
                if (visitedforums.size() > visitedforumcount + 1) {
                    Set<Short> keys = visitedforums.keySet();
                    for (Short key : keys) {
                        visitedforums.remove(key);
                        break;
                    }
                }
                session.setAttribute("visitedforums", visitedforums);
                request.setAttribute("visitedforums", visitedforums != null && visitedforums.size() > 1 ? visitedforums : null);
            }
            String rsshead = "";
            String rssstatus = settings.get("rssstatus");
            String rssauth = "0";
            if (rssstatus != null && rssstatus.equals("1")) {
                if (member != null) {
                    Md5Token md5 = Md5Token.getInstance();
                    rssauth = Common.authcode(member.getUid() + "\t" + fid + "\t" + md5.getLongToken(member.getPassword() + member.getSecques()).substring(0, 8), "ENCODE", md5.getLongToken(settings.get("authkey")), null);
                    try {
                        rssauth = URLEncoder.encode(rssauth, JspRunConfig.charset);
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
                String boardurl = (String) session.getAttribute("boardurl");
                if (boardurl == null) {
                    boardurl = (request.getScheme().concat("://").concat(request.getServerName()).concat(":") + request.getServerPort()).concat(request.getContextPath()).concat("/");
                }
                rsshead = "<link rel=\"alternate\" type=\"application/rss+xml\" title=\"" + settings.get("bbname") + " - " + navtitle + "\" href=\"" + boardurl + "rss.jsp?fid=" + fid + "&amp;auth=" + rssauth + "\" />\n";
            }
            request.setAttribute("rssauth", rssauth);
            request.setAttribute("rsshead", rsshead);
            request.setAttribute("collapse", collapse);
            settings = null;
        } else {
            request.setAttribute("errorInfo", "ָ���İ�鲻���ڣ��뷵�ء�");
            return mapping.findForward("showMessage");
        }
        request.setAttribute("coloroptions", coloroptions);
        return mapping.findForward("toForumdisplay");
    }
