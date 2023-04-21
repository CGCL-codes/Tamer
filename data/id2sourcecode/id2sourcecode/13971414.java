    @SuppressWarnings("unchecked")
    private ActionForward network(HttpServletRequest request, HttpServletResponse response, Map<String, Object> sGlobal, Map<String, Object> sConfig, Map<String, Object> space) throws Exception {
        if (Common.empty(sConfig.get("networkpublic"))) {
            if (Common.empty(sGlobal.get("supe_uid"))) {
                CookieHelper.setCookie(request, response, "_refer", Common.urlEncode((String) request.getAttribute("requestURI")));
                return showMessage(request, response, "to_login", "do.jsp?ac=" + sConfig.get("login_action"));
            }
        }
        int timestamp = (Integer) sGlobal.get("timestamp");
        Map<String, Map<String, Object>> globalNetWork = Common.getCacheDate(request, response, "/data/cache/cache_network.jsp", "globalNetWork");
        String jchRoot = JavaCenterHome.jchRoot;
        Map<Integer, String> sNames = (Map<Integer, String>) request.getAttribute("sNames");
        List<Map<String, Object>> blogs = null;
        String cachePath = jchRoot + "data/cache/cache_network_blog.txt";
        Map<String, Object> netWork = getNetWork(globalNetWork, "blog");
        if (check_network_cache(netWork, cachePath, timestamp)) {
            blogs = Serializer.unserialize(FileHelper.readFile(cachePath));
        } else {
            Map<String, String> result = mk_network_sql(netWork, timestamp, new String[] { "blogid", "uid" }, new String[] { "hot", "viewnum", "replynum" }, new String[] { "dateline" }, new String[] { "dateline", "viewnum", "replynum", "hot" });
            String sql = "SELECT main.blogid, main.uid, main.username, main.subject, main.hot, main.dateline, field.message FROM " + JavaCenterHome.getTableName("blog") + " main LEFT JOIN " + JavaCenterHome.getTableName("blogfield") + " field ON field.blogid=main.blogid WHERE " + result.get("wheres") + " AND main.friend='0' ORDER BY main." + result.get("order") + " " + result.get("sc") + " LIMIT 0,6";
            blogs = dataBaseService.executeQuery(sql);
            for (Map<String, Object> blog : blogs) {
                blog.put("subject", Common.getStr((String) blog.get("subject"), 50, false, false, false, 0, -1, request, response));
                blog.put("message", Common.getStr((String) blog.get("message"), 86, false, false, false, 0, -1, request, response));
                blog.put("dateline", Common.sgmdate(request, "MM-dd HH:mm", (Integer) blog.get("dateline"), true));
            }
            if ((Integer) netWork.get("cache") > 0) {
                FileHelper.writeFile(cachePath, Serializer.serialize(blogs));
            }
        }
        if (!Common.empty(blogs)) {
            for (Map<String, Object> blog : blogs) {
                Common.realname_set(sGlobal, sConfig, sNames, (Integer) blog.get("uid"), (String) blog.get("username"), null, 0);
            }
            request.setAttribute("blogs", blogs);
        }
        List<Map<String, Object>> pics = null;
        cachePath = jchRoot + "data/cache/cache_network_pic.txt";
        netWork = getNetWork(globalNetWork, "pic");
        if (check_network_cache(netWork, cachePath, timestamp)) {
            pics = Serializer.unserialize(FileHelper.readFile(cachePath));
        } else {
            pics = new ArrayList<Map<String, Object>>();
            Map<String, String> result = mk_network_sql(netWork, timestamp, new String[] { "picid", "uid" }, new String[] { "hot" }, new String[] { "dateline" }, new String[] { "dateline", "hot" });
            String sql = "SELECT album.albumname, album.friend, space.username, space.name, space.namestatus, main.picid, main.uid, main.dateline, main.filepath, main.thumb, main.remote, main.hot FROM " + JavaCenterHome.getTableName("pic") + " main LEFT JOIN " + JavaCenterHome.getTableName("album") + " album ON album.albumid=main.albumid LEFT JOIN " + JavaCenterHome.getTableName("space") + " space ON space.uid=main.uid WHERE " + result.get("wheres") + " ORDER BY main." + result.get("order") + " " + result.get("sc") + " LIMIT 0,28";
            List<Map<String, Object>> picList = dataBaseService.executeQuery(sql);
            for (Map<String, Object> pic : picList) {
                if (Common.empty(pic.get("friend"))) {
                    pic.put("filepath", Common.pic_get(sConfig, (String) pic.get("filepath"), (Integer) pic.get("thumb"), (Integer) pic.get("remote"), true));
                    pic.put("dateline", Common.sgmdate(request, "MM-dd HH:mm", (Integer) pic.get("dateline"), true));
                    pic.remove("friend");
                    pic.remove("thumb");
                    pic.remove("remote");
                    pics.add(pic);
                }
            }
            if ((Integer) netWork.get("cache") > 0) {
                FileHelper.writeFile(cachePath, Serializer.serialize(pics));
            }
        }
        if (!Common.empty(pics)) {
            for (Map<String, Object> pic : pics) {
                Common.realname_set(sGlobal, sConfig, sNames, (Integer) pic.get("uid"), (String) pic.get("username"), (String) pic.get("name"), (Integer) pic.get("namestatus"));
            }
            request.setAttribute("pics", pics);
        }
        List<Map<String, Object>> threads = null;
        cachePath = jchRoot + "data/cache/cache_network_thread.txt";
        netWork = getNetWork(globalNetWork, "thread");
        if (check_network_cache(netWork, cachePath, timestamp)) {
            threads = Serializer.unserialize(FileHelper.readFile(cachePath));
        } else {
            Map<String, String> result = mk_network_sql(netWork, timestamp, new String[] { "tid", "uid" }, new String[] { "hot", "viewnum", "replynum" }, new String[] { "dateline", "lastpost" }, new String[] { "dateline", "viewnum", "replynum", "hot" });
            String sql = "SELECT main.tid, main.tagid, main.subject, main.uid, main.username, main.hot, m.tagname FROM " + JavaCenterHome.getTableName("thread") + " main LEFT JOIN " + JavaCenterHome.getTableName("mtag") + " m ON m.tagid=main.tagid WHERE " + result.get("wheres") + " ORDER BY main." + result.get("order") + " " + result.get("sc") + " LIMIT 0,10";
            threads = dataBaseService.executeQuery(sql);
            for (Map<String, Object> thread : threads) {
                thread.put("tagname", Common.getStr((String) thread.get("tagname"), 20, false, false, false, 0, 0, request, response));
                thread.put("subject", Common.getStr((String) thread.get("subject"), 50, false, false, false, 0, 0, request, response));
            }
            if ((Integer) netWork.get("cache") > 0) {
                FileHelper.writeFile(cachePath, Serializer.serialize(threads));
            }
        }
        if (!Common.empty(threads)) {
            for (Map<String, Object> thread : threads) {
                Common.realname_set(sGlobal, sConfig, sNames, (Integer) thread.get("uid"), (String) thread.get("username"), null, 0);
            }
            request.setAttribute("threads", threads);
        }
        String dateformat = "MM-dd HH:mm";
        Map<Object, Map<String, Object>> globalEventClass = Common.getCacheDate(request, response, "/data/cache/cache_eventclass.jsp", "globalEventClass");
        List<Map<String, Object>> events = null;
        cachePath = jchRoot + "data/cache/cache_network_event.txt";
        netWork = getNetWork(globalNetWork, "event");
        if (check_network_cache(netWork, cachePath, timestamp)) {
            events = Serializer.unserialize(FileHelper.readFile(cachePath));
        } else {
            Map<String, String> result = mk_network_sql(netWork, timestamp, new String[] { "eventid", "uid" }, new String[] { "hot", "membernum", "follownum" }, new String[] { "dateline" }, new String[] { "dateline", "membernum", "follownum", "hot" });
            String sql = "SELECT main.eventid, main.uid, main.username, main.title, main.classid, main.province, main.city, main.location, main.poster, main.thumb, main.remote, main.starttime, main.endtime, main.membernum, main.follownum FROM " + JavaCenterHome.getTableName("event") + " main WHERE " + result.get("wheres") + " ORDER BY main." + result.get("order") + " " + result.get("sc") + " LIMIT 0,6";
            events = dataBaseService.executeQuery(sql);
            for (Map<String, Object> event : events) {
                event.put("title", Common.getStr((String) event.get("title"), 45, false, false, false, 0, 0, request, response));
                String poster = (String) event.get("poster");
                if (Common.empty(poster)) {
                    event.put("poster", globalEventClass.get(event.get("classid")).get("poster"));
                } else {
                    event.put("poster", Common.pic_get(sConfig, poster, (Integer) event.get("thumb"), (Integer) event.get("remote"), true));
                }
                event.put("starttime", Common.sgmdate(request, dateformat, (Integer) event.get("starttime"), false));
                event.put("endtime", Common.sgmdate(request, dateformat, (Integer) event.get("endtime"), false));
                event.remove("classid");
                event.remove("thumb");
                event.remove("remote");
            }
            if ((Integer) netWork.get("cache") > 0) {
                FileHelper.writeFile(cachePath, Serializer.serialize(events));
            }
        }
        if (!Common.empty(events)) {
            for (Map<String, Object> event : events) {
                Common.realname_set(sGlobal, sConfig, sNames, (Integer) event.get("uid"), (String) event.get("username"), null, 0);
            }
            request.setAttribute("events", events);
        }
        List<Map<String, Object>> polls = null;
        cachePath = jchRoot + "data/cache/cache_network_poll.txt";
        netWork = getNetWork(globalNetWork, "poll");
        if (check_network_cache(netWork, cachePath, timestamp)) {
            polls = Serializer.unserialize(FileHelper.readFile(cachePath));
        } else {
            Map<String, String> result = mk_network_sql(netWork, timestamp, new String[] { "pid", "uid" }, new String[] { "hot", "voternum", "replynum" }, new String[] { "dateline" }, new String[] { "dateline", "voternum", "replynum", "hot" });
            String sql = "SELECT main.pid, main.uid, main.username, main.subject, main.voternum FROM " + JavaCenterHome.getTableName("poll") + " main WHERE " + result.get("wheres") + " ORDER BY main." + result.get("order") + " " + result.get("sc") + " LIMIT 0,9";
            polls = dataBaseService.executeQuery(sql);
            if ((Integer) netWork.get("cache") > 0) {
                FileHelper.writeFile(cachePath, Serializer.serialize(polls));
            }
        }
        if (!Common.empty(polls)) {
            for (Map<String, Object> poll : polls) {
                Common.realname_set(sGlobal, sConfig, sNames, (Integer) poll.get("uid"), (String) poll.get("username"), null, 0);
            }
            request.setAttribute("polls", polls);
        }
        List<Map<String, Object>> doings = dataBaseService.executeQuery("SELECT doid, uid, username, dateline, message  FROM " + JavaCenterHome.getTableName("doing") + " ORDER BY dateline DESC LIMIT 0,5");
        if (doings.size() > 0) {
            for (Map<String, Object> doing : doings) {
                Common.realname_set(sGlobal, sConfig, sNames, (Integer) doing.get("uid"), (String) doing.get("username"), null, 0);
                doing.put("title", Common.getStr((String) doing.get("message"), 0, false, false, false, 0, -1, request, response));
                doing.put("dateline", Common.sgmdate(request, "HH:mm", (Integer) (Integer) doing.get("dateline"), true));
            }
            request.setAttribute("doings", doings);
        }
        List<Map<String, Object>> stars = null;
        Object spaceBarUserName = sConfig.get("spacebarusername");
        if (!Common.empty(spaceBarUserName)) {
            stars = dataBaseService.executeQuery("SELECT * FROM " + JavaCenterHome.getTableName("space") + " WHERE username IN (" + Common.sImplode(spaceBarUserName.toString().split(",")) + ") ORDER BY rand() limit 1");
        }
        List<Map<String, Object>> shows = dataBaseService.executeQuery("SELECT sh.note, s.* FROM " + JavaCenterHome.getTableName("show") + " sh LEFT JOIN " + JavaCenterHome.getTableName("space") + " s ON s.uid=sh.uid ORDER BY sh.credit DESC LIMIT 0,23");
        if (shows.size() > 0) {
            for (Map<String, Object> show : shows) {
                Common.realname_set(sGlobal, sConfig, sNames, (Integer) show.get("uid"), (String) show.get("username"), (String) show.get("name"), (Integer) show.get("namestatus"));
                show.put("note", Common.addSlashes(Common.getStr((String) show.get("note"), 80, false, false, false, 0, -1, request, response)));
            }
            if (Common.empty(stars)) {
                stars = Common.getRandList(shows, 1);
            }
            request.setAttribute("shows", shows);
        }
        List<Map<String, Object>> onlines = dataBaseService.executeQuery("SELECT s.*, sf.note FROM " + JavaCenterHome.getTableName("session") + " s LEFT JOIN " + JavaCenterHome.getTableName("spacefield") + " sf ON sf.uid=s.uid WHERE s.magichidden = 0 ORDER BY s.lastactivity DESC LIMIT 0,12");
        if (onlines.size() > 0) {
            for (Map<String, Object> online : onlines) {
                Common.realname_set(sGlobal, sConfig, sNames, (Integer) online.get("uid"), (String) online.get("username"), null, 0);
                online.put("note", Common.sHtmlSpecialChars(Common.stripTags((String) online.get("note"))));
            }
            if (Common.empty(stars)) {
                stars = Common.getRandList(onlines, 1);
                for (Map<String, Object> value : stars) {
                    List<Map<String, Object>> query = dataBaseService.executeQuery("SELECT * FROM " + JavaCenterHome.getTableName("space") + " WHERE uid='" + value.get("uid") + "'");
                    if (query.size() > 0) {
                        value.putAll(query.get(0));
                    }
                }
            }
            request.setAttribute("onlines", onlines);
        }
        if (!Common.empty(stars)) {
            Map<String, Object> star = stars.get(0);
            Common.realname_set(sGlobal, sConfig, sNames, (Integer) star.get("uid"), (String) star.get("username"), (String) star.get("name"), star.get("namestatus") == null ? 0 : (Integer) star.get("namestatus"));
            star.put("updatetime", Common.sgmdate(request, "HH:mm", star.get("updatetime") == null ? 0 : (Integer) star.get("updatetime"), true));
            request.setAttribute("star", star);
        }
        request.setAttribute("onlineCount", dataBaseService.findRows("SELECT COUNT(*) FROM " + JavaCenterHome.getTableName("session")));
        int myAppCount = 0;
        if ((Integer) sConfig.get("my_status") == 1) {
            myAppCount = dataBaseService.findRows("SELECT COUNT(*) FROM " + JavaCenterHome.getTableName("myapp") + " WHERE flag>='0'");
            if (myAppCount > 0) {
                List<Map<String, Object>> myApps = dataBaseService.executeQuery("SELECT appid,appname FROM " + JavaCenterHome.getTableName("myapp") + " WHERE flag>=0 ORDER BY flag DESC, displayorder LIMIT 0,7");
                request.setAttribute("myApps", myApps);
            }
        }
        request.setAttribute("myAppCount", myAppCount);
        List<Map<String, Object>> shares = null;
        cachePath = jchRoot + "data/cache/cache_network_share.txt";
        netWork = getNetWork(globalNetWork, "share");
        if (check_network_cache(netWork, cachePath, timestamp)) {
            shares = Serializer.unserialize(FileHelper.readFile(cachePath));
        } else {
            Map<String, String> result = mk_network_sql(netWork, timestamp, new String[] { "sid", "uid" }, new String[] { "hot" }, new String[] { "dateline" }, new String[] { "dateline", "hot" });
            String sql = "SELECT * FROM " + JavaCenterHome.getTableName("share") + " main LEFT JOIN " + JavaCenterHome.getTableName("space") + " space ON space.uid=main.uid WHERE " + result.get("wheres") + " ORDER BY main." + result.get("order") + " " + result.get("sc") + " LIMIT 0,6";
            shares = dataBaseService.executeQuery(sql);
            for (Map<String, Object> share : shares) {
                Map<String, String> bodyData = Serializer.unserialize((String) share.get("body_data"), false);
                String type = (String) share.get("type");
                boolean iscut = false;
                if (!Common.empty(bodyData)) {
                    Set<String> keys = bodyData.keySet();
                    for (String key : keys) {
                        if (!iscut && ("blog".equals(type) || "thread".equals(type))) {
                            bodyData.put("message", Common.cutstr(bodyData.get("message"), 40));
                            iscut = true;
                        }
                        if (!iscut && "poll".equals(type)) {
                            String subject = bodyData.get("subject");
                            List<String> strList = Common.pregMatch(subject, "(?is)<a href=\"(.+?)\">(.+?)</a>");
                            if (strList.size() > 0) {
                                subject = "<a href=\"" + strList.get(1) + "\">" + Common.cutstr(strList.get(2), 40) + "</a>";
                            }
                            bodyData.put("subject", subject);
                            iscut = true;
                        }
                        String body_template = ((String) share.get("body_template")).replace("{" + key + "}", bodyData.get(key));
                        share.put("body_template", body_template);
                    }
                }
                share.put("body_data", bodyData);
                share.put("dateline", Common.sgmdate(request, dateformat, (Integer) share.get("dateline"), true));
                share.put("body_general", Common.cutstr((String) share.get("body_general"), 40));
            }
            if ((Integer) netWork.get("cache") > 0) {
                FileHelper.writeFile(cachePath, Serializer.serialize(shares));
            }
        }
        if (!Common.empty(shares)) {
            for (Map<String, Object> share : shares) {
                Common.realname_set(sGlobal, sConfig, sNames, (Integer) share.get("uid"), (String) share.get("username"), null, 0);
            }
            request.setAttribute("shares", shares);
        }
        Map<String, String> shareClasses = new LinkedHashMap<String, String>();
        shareClasses.put("link", "��ַ");
        shareClasses.put("video", "��Ƶ");
        shareClasses.put("music", "����");
        shareClasses.put("flash", "Flash");
        shareClasses.put("blog", "��־");
        shareClasses.put("album", "���");
        shareClasses.put("pic", "ͼƬ");
        shareClasses.put("mtag", "Ⱥ��");
        shareClasses.put("thread", "����");
        shareClasses.put("poll", "ͶƱ");
        shareClasses.put("event", "�");
        shareClasses.put("space", "�û�");
        shareClasses.put("tag", "TAG");
        request.setAttribute("shareClasses", shareClasses);
        Common.realname_get(sGlobal, sConfig, sNames, space);
        Map<String, String> sCookie = (Map<String, String>) request.getAttribute("sCookie");
        String loginUser = sCookie.get("loginuser");
        request.setAttribute("memberName", Common.empty(loginUser) ? "" : Common.stripSlashes(loginUser));
        request.setAttribute("tpl_css", "network");
        return include(request, response, sConfig, sGlobal, "network.jsp");
    }
