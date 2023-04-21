    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpSession session = request.getSession();
        Integer uid = (Integer) session.getAttribute("jsprun_uid");
        if (uid != null && uid == 0) {
            String accessPath = request.getRequestURI();
            int index = accessPath.lastIndexOf("/");
            if (index != -1 && accessPath.indexOf("archiver") == -1) {
                accessPath = accessPath.substring(index + 1);
            }
            HttpServletResponse response = (HttpServletResponse) res;
            Map<String, String> settings = ForumInit.settings;
            if (accessPath.equals("index.jsp") || accessPath.equals("") || accessPath.equals(settings.get("indexname"))) {
                int cacheindexlife = Common.toDigit(settings.get("cacheindexlife"));
                if (cacheindexlife > 0) {
                    String realPath = session.getServletContext().getRealPath("/");
                    String[] indexcache = getcacheinfo(0, realPath + settings.get("cachethreaddir"));
                    File file = new File(indexcache[0]);
                    if (file.exists() && file.length() == 0) {
                        file.delete();
                        int timestamp = (Integer) (request.getAttribute("timestamp"));
                        int timeoffset = (int) ((Float) session.getAttribute("timeoffset") * 3600);
                        SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
                        String debugtime = Common.gmdate(df, timestamp + timeoffset);
                        String filename = indexcache[0].substring(realPath.length());
                        FileCaptureResponseWrapper responseWrapper = new FileCaptureResponseWrapper(response);
                        chain.doFilter(request, responseWrapper);
                        String content = null;
                        if ("1".equals(settings.get("debug"))) {
                            content = "<script type=\"text/javascript\">document.getElementById(\"debuginfo\").innerHTML = \"Update at " + debugtime + ", Processed in 0.009121 second(s), 0 Queries\";</script>";
                        }
                        responseWrapper.writeFile(indexcache[0], content);
                        try {
                            request.getRequestDispatcher(filename).include(request, response);
                        } catch (Exception e) {
                        }
                        return;
                    }
                }
            } else if (accessPath.equals("viewthread.jsp") || accessPath.startsWith("thread-")) {
                int cacheindexlife = Common.toDigit(settings.get("cacheindexlife"));
                if (cacheindexlife > 0) {
                    String realPath = session.getServletContext().getRealPath("/");
                    String tid = request.getParameter("tid");
                    String page = request.getParameter("page");
                    if (accessPath.startsWith("thread-")) {
                        tid = accessPath.replaceAll("thread-([0-9]+)-([0-9]+)-([0-9]+)\\.html", "$1");
                        page = accessPath.replaceAll("thread-([0-9]+)-([0-9]+)-([0-9]+)\\.html", "$3");
                    }
                    page = page == null ? "1" : page;
                    String[] threadcache = getcacheinfo(Common.toDigit(tid), realPath + settings.get("cachethreaddir"));
                    File file = new File(threadcache[0]);
                    if (file.exists() && file.length() == 0 && page.equals("1")) {
                        file.delete();
                        int timestamp = (Integer) (request.getAttribute("timestamp"));
                        int timeoffset = (int) ((Float) session.getAttribute("timeoffset") * 3600);
                        SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
                        String debugtime = Common.gmdate(df, timestamp + timeoffset);
                        String filename = threadcache[0].substring(realPath.length());
                        FileCaptureResponseWrapper responseWrapper = new FileCaptureResponseWrapper(response);
                        chain.doFilter(request, responseWrapper);
                        String content = null;
                        if ("1".equals(settings.get("debug"))) {
                            content = "<script type=\"text/javascript\">document.getElementById(\"debuginfo\").innerHTML = \"Update at " + debugtime + ", Processed in 0.009121 second(s), 0 Queries\";</script>";
                        }
                        responseWrapper.writeFile(threadcache[0], content);
                        try {
                            request.getRequestDispatcher(filename).include(request, response);
                        } catch (Exception e) {
                        }
                        return;
                    }
                }
            }
        }
        chain.doFilter(req, res);
    }
