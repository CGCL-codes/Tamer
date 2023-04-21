    @SuppressWarnings("unchecked")
    public ActionForward querysitmap(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        Map<String, String> settings = (Map<String, String>) request.getAttribute("settings");
        Map<String, String> forumStr = (Map<String, String>) request.getAttribute("forums");
        Map<String, Map<String, String>> forumMap = ((DataParse) BeanFactory.getBean("dataParse")).characterParse(forumStr.get("forums"), false);
        int maxitemnum = 500;
        HttpSession session = request.getSession();
        String boardurl = (String) session.getAttribute("boardurl");
        int timestamp = (Integer) (request.getAttribute("timestamp"));
        String baidusitemap = settings.get("baidusitemap");
        if (baidusitemap.equals("0")) {
            try {
                response.getWriter().write("Baidu Sitemaps is closed!");
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
        String sitemapfile = servlet.getServletContext().getRealPath("/") + "forumdata/sitemap.xml";
        File sitemap = new File(sitemapfile);
        long xmlfiletime = 0;
        if (sitemap.exists()) {
            xmlfiletime = sitemap.lastModified();
        }
        int baidusitemap_life = convertInt(settings.get("baidusitemap_life"));
        response.setHeader("Content-type:", "application/xml");
        String xmlcontent = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" + "<document xmlns:bbs=\"http://www.baidu.com/search/bbs_sitemap.xsd\">\n";
        if (timestamp - xmlfiletime >= baidusitemap_life * 3600) {
            xmlfiletime = timestamp - baidusitemap_life * 3600;
            int timeoffset = (int) ((Float) session.getAttribute("timeoffset") * 3600);
            String fidarray = "0";
            String adminemail = settings.get("adminemail");
            List<Forums> forumlist = forumService.findAll();
            for (Forums f : forumlist) {
                if (sitemapforumperm(f, forumMap)) {
                    fidarray = fidarray + "," + f.getFid();
                }
            }
            String sql = "SELECT tid, fid, subject, dateline, lastpost, replies, views, digest FROM jrun_threads WHERE dateline > " + (xmlfiletime + "").substring(0, 10) + " AND fid IN (" + fidarray + ") AND displayorder >=0 LIMIT " + maxitemnum;
            List<Map<String, String>> threadlist = dataBaseService.executeQuery(sql, new String[] { "tid", "fid", "subject", "dateline", "lastpost", "replies", "views", "digest" });
            xmlcontent = xmlcontent + "<webSite>" + boardurl + "</webSite>\n" + "	<webMaster>" + adminemail + "</webMaster>\n" + "	<updatePeri>" + baidusitemap_life + "</updatePeri>\n" + "	<updatetime>" + Common.gmdate("yyyy-MM-dd HH:mm:ss", (int) (timestamp + timeoffset)) + "</updatetime>\n" + "	<version>JspRun! " + JspRunConfig.version + "</version>\n";
            String rewritestatus = settings.get("rewritestatus");
            for (Map<String, String> thread : threadlist) {
                int dateline = Common.toDigit(thread.get("dateline"));
                int lastpost = Common.toDigit(thread.get("lastpost"));
                xmlcontent += "	<item>\n" + "		<link>" + (rewritestatus.equals("0") ? boardurl + "viewthread.jsp?tid=" + thread.get("tid") : boardurl + "thread-" + thread.get("tid") + "-1-1.html") + "</link>\n" + "		<title>" + thread.get("subject") + "</title>\n" + "		<pubDate>" + Common.gmdate("yyyy-MM-dd HH:mm:ss", (int) (dateline + timeoffset)) + "</pubDate>\n" + "		<bbs:lastDate>" + Common.gmdate("yyyy-MM-dd HH:mm:ss", (int) (lastpost + timeoffset)) + "</bbs:lastDate>\n" + "		<bbs:reply>" + thread.get("replies") + "</bbs:reply>\n" + "		<bbs:hit>" + thread.get("views") + "</bbs:hit>\n" + "		<bbs:boardid>" + thread.get("fid") + "</bbs:boardid>\n" + "		<bbs:pick>" + (thread.get("digest").equals("") ? 0 : 1) + "</bbs:pick>\n" + "	</item>\n";
            }
            xmlcontent += "</document>";
            try {
                OutputStream out = new FileOutputStream(sitemapfile);
                OutputStreamWriter ot = new OutputStreamWriter(out, JspRunConfig.charset);
                ot.write(xmlcontent);
                ot.close();
                out.close();
                sitemapfile = sitemapfile.substring(servlet.getServletContext().getRealPath("/").length());
                request.getRequestDispatcher(sitemapfile).forward(request, response);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ServletException e) {
                e.printStackTrace();
            }
        } else {
            sitemapfile = sitemapfile.substring(servlet.getServletContext().getRealPath("/").length());
            try {
                request.getRequestDispatcher(sitemapfile).forward(request, response);
            } catch (ServletException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
