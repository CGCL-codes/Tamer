    @SuppressWarnings("unchecked")
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        request.setAttribute("starttime", System.currentTimeMillis());
        int timestamp = Common.time();
        request.setAttribute("timestamp", timestamp);
        String accessPath = request.getRequestURI();
        int index = accessPath.lastIndexOf("/");
        if (index != -1) {
            accessPath = accessPath.substring(index + 1);
        }
        if (accessPath.startsWith("forum-")) {
            accessPath = "forumdisplay.jsp";
        } else if (accessPath.startsWith("thread-")) {
            accessPath = "viewthread.jsp";
        }
        request.setAttribute("CURSCRIPT", accessPath);
        if ("install.jsp".equals(accessPath)) {
            chain.doFilter(request, response);
            return;
        }
        if (HibernateUtil.getSessionFactory() == null) {
            request.setAttribute("errorinfo", HibernateUtil.getMessage());
            request.getRequestDispatcher("/errors/error_sql.jsp").forward(request, response);
            return;
        }
        Map<String, String> settings = ForumInit.settings;
        if (settings == null) {
            initForum(request.getSession().getServletContext());
            settings = ForumInit.settings;
        }
        int attackevasive = Common.toDigit(settings.get("attackevasive"));
        if (attackevasive > 0 && this.security(request, response, timestamp, attackevasive)) {
            return;
        }
        if ("1".equals(settings.get("nocacheheaders"))) {
            response.setHeader("Pragma", "no-cache");
            response.setHeader("Cache-Control", "private, post-check=0, pre-check=0, max-age=0");
            response.setDateHeader("Expires", 0);
        }
        HttpSession httpSession = request.getSession();
        if (httpSession.getAttribute("timeoffset") == null) {
            Common.setDateformat(httpSession, settings);
        }
        if (httpSession.getAttribute("refreshtime") == null) {
            Map forwardmap = ((DataParse) BeanFactory.getBean("dataParse")).characterParse(settings.get("msgforward"), false);
            httpSession.setAttribute("refreshtime", forwardmap == null ? "3" : forwardmap.get("refreshtime"));
            httpSession.setAttribute("quick", forwardmap == null ? (byte) 0 : Byte.valueOf(forwardmap.get("quick").toString()));
            httpSession.setAttribute("successmessages", forwardmap == null ? null : forwardmap.get("messages"));
            forwardmap = null;
        }
        if (request.getParameter("styleid") != null && !"admincp.jsp".equals(accessPath)) {
            httpSession.setAttribute("styleid", request.getParameter("styleid"));
        }
        if (httpSession.getAttribute("boardurl") == null) {
            httpSession.setAttribute("boardurl", (request.getScheme().concat("://").concat(request.getServerName()).concat(":") + request.getServerPort()).concat(request.getContextPath()).concat("/"));
        }
        String jsprun_sid = (String) httpSession.getAttribute("jsprun_sid");
        Integer jsprun_uid = (Integer) httpSession.getAttribute("jsprun_uid");
        String sid = CookieUtil.getCookie(request, "sid", true, settings);
        if (sid == null && jsprun_sid == null || sid != null && sid.equals("")) {
            sid = Common.getRandStr(6, false);
            CookieUtil.setCookie(request, response, "sid", sid, 604800, true, settings);
            httpSession.setAttribute("jsprun_sid", sid);
        } else if ((sid == null && jsprun_sid != null) || sid != null && jsprun_sid != null && !jsprun_sid.equals(sid)) {
            sid = jsprun_sid;
            CookieUtil.setCookie(request, response, "sid", jsprun_sid, 604800, true, settings);
        } else if (sid != null && jsprun_sid == null) {
            httpSession.setAttribute("jsprun_sid", sid);
        }
        if (jsprun_uid == null) {
            jsprun_uid = 0;
            String jsprun_userss = null;
            short groupid = 7;
            byte adminid = 0;
            String uid = CookieUtil.getCookie(request, "uid", true, settings);
            if (uid != null) {
                MemberService memberService = (MemberService) BeanFactory.getBean("memberService");
                Members member = memberService.findMemberById(Common.toDigit(uid, 1000000000L, 0L).intValue());
                if (member != null) {
                    String validateAuth = Md5Token.getInstance().getLongToken(member.getPassword() + "\t" + member.getSecques() + "\t" + member.getUid());
                    if (validateAuth.equals(CookieUtil.getCookie(request, "auth", true, settings))) {
                        jsprun_uid = member.getUid();
                        jsprun_userss = member.getUsername();
                        groupid = member.getGroupid();
                        adminid = member.getAdminid();
                        httpSession.setAttribute("user", member);
                        Common.setDateformat(httpSession, settings);
                        httpSession.setAttribute("jsprun_pw", member.getPassword());
                        if (member.getStyleid() > 0) {
                            httpSession.setAttribute("styleid", member.getStyleid().toString());
                        }
                    }
                }
            } else {
                CookieUtil.setCookie(request, response, "uid", String.valueOf(jsprun_uid), 604800, true, settings);
            }
            httpSession.setAttribute("jsprun_uid", jsprun_uid);
            httpSession.setAttribute("jsprun_userss", jsprun_userss != null ? jsprun_userss : "");
            httpSession.setAttribute("jsprun_groupid", groupid);
            httpSession.setAttribute("jsprun_adminid", adminid);
            httpSession.setAttribute("formhash", Common.getRandStr(8, false));
        }
        Common.sessionExists(request, response, sid, jsprun_uid, settings);
        Common.calcGroup(httpSession, request, response, settings);
        if (Common.allowAccessBbs(request, response, httpSession, settings, accessPath)) {
            return;
        }
        Common.setFtpValue(settings.get("ftp"), settings.get("authkey"));
        int rewritestatus = Integer.parseInt(settings.get("rewritestatus"));
        boolean forumdisplayurl = false;
        boolean viewthreadurl = false;
        boolean spaceurlurl = false;
        boolean tagsurl = false;
        forumdisplayurl = (rewritestatus & 1) > 0;
        viewthreadurl = (rewritestatus & 2) > 0;
        spaceurlurl = (rewritestatus & 4) > 0;
        tagsurl = (rewritestatus & 8) > 0;
        request.setAttribute("forumdisplayurl", forumdisplayurl);
        request.setAttribute("viewthreadurl", viewthreadurl);
        request.setAttribute("spaceurlurl", spaceurlurl);
        request.setAttribute("tagsurl", tagsurl);
        String statstatus = settings.get("statstatus");
        if (statstatus != null && statstatus.equals("1") && request.getParameter("inajax") == null) {
            Common.stats(request);
        }
        settings = null;
        chain.doFilter(request, response);
    }
