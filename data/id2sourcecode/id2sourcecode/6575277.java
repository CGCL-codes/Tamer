    public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String contentType = "text/html";
        try {
            String setCache = request.getParameter("stylesheet");
            if (setCache != null) {
                if ("cleanup".equals(setCache)) {
                    if (store != null) store.freeAll();
                } else if ("on".equals(setCache)) {
                    store = new MemoryStore();
                } else if ("off".equals(setCache)) {
                    if (store != null) store.freeAll();
                    store = null;
                }
            }
            String basename = request.getPathTranslated();
            if (basename == null) {
                response.setContentType(contentType);
                PrintWriter out = new PrintWriter(response.getWriter());
                showStatus(out, request);
                return;
            }
            long time = System.currentTimeMillis();
            long diff;
            String requestType = request.getContentType();
            if (requestType != null && requestType.toLowerCase().startsWith("multipart/form-data")) {
                request = new HttpMultipartRequest(request, "/usr/tmp", "pl-upload-");
            }
            LogRequestSink trace = null;
            ResponseProxy proxy = null;
            HttpServletResponse rep = response;
            String suppress = request.getParameter("suppresstrace");
            if (Trace.getInstance().getTrace() && !"yes".equals(suppress)) {
                trace = new LogRequestSink();
                Trace.getInstance().setCurrent(trace);
            }
            try {
                if (trace != null) {
                    proxy = new ResponseProxy(response);
                    rep = proxy;
                    trace.setRequest(request);
                }
                if (basename.endsWith(".jar") || basename.endsWith(".js") || basename.endsWith(".gif") || basename.endsWith(".jpg")) {
                    contentType = "unknown/unknown";
                    Logger.getInstance().log(this, "Request: get" + basename, Logger.INFO);
                    response.setContentType(contentType);
                    copyService(request, rep);
                } else {
                    Reader xml = new FileReader(basename);
                    Hashtable pi;
                    try {
                        pi = getStylesheet(xml, basename);
                    } finally {
                        xml.close();
                    }
                    if (pi == null) {
                        throw new Exception("Can't find xml-stylesheet in " + basename);
                    }
                    if ("application/rmdms-raw".equals(pi.get("type"))) {
                        if (pi.get("content-type") != null) {
                            contentType = (String) pi.get("content-type");
                        } else {
                            contentType = "text/xml";
                        }
                        response.setContentType(contentType);
                        if ("readwrite".equals(pi.get("access"))) {
                            Logger.getInstance().log(this, "Request: raw readwrite " + basename, Logger.INFO);
                            rawService(request, rep, trace, true);
                        } else {
                            Logger.getInstance().log(this, "Request: raw readonly" + basename, Logger.INFO);
                            rawService(request, rep, trace, false);
                        }
                    } else {
                        if (pi.get("content-type") != null) {
                            contentType = (String) pi.get("content-type");
                        }
                        String stylesheet = (String) pi.get("href");
                        if (stylesheet == null) {
                            throw new Exception("Can't find a href for the stylesheet in " + basename);
                        }
                        int indexOfColon = stylesheet.indexOf(':');
                        int indexOfSlash = stylesheet.indexOf('/');
                        if ((indexOfColon != -1) && (indexOfSlash != -1) && (indexOfColon < indexOfSlash)) {
                        } else {
                            String base = basename;
                            int last = base.lastIndexOf("/");
                            if (last > 0) base = base.substring(0, last + 1);
                            stylesheet = base + stylesheet;
                        }
                        response.setContentType(contentType);
                        Logger.getInstance().log(this, "Request: xslt" + basename, Logger.INFO);
                        xslService(request, rep, trace, basename, stylesheet);
                    }
                }
            } catch (Exception e) {
                if (trace == null) {
                    trace = new LogRequestSink();
                    Trace.getInstance().setCurrent(trace);
                    trace.setRequest(request);
                    proxy = new ResponseProxy(response);
                }
                trace.setException(e);
                Logger.getInstance().log(this, e, "Error", Logger.ERROR);
                if ("text/html".equals(contentType)) {
                    htmlError(proxy, e);
                } else if ("text/vnd.wap.wml".equals(contentType)) {
                    wmlError(proxy, e);
                } else if ("text/xml".equals(contentType)) {
                    xmlError(proxy, e);
                } else {
                    response.setContentType("text/xml");
                    xmlError(proxy, e);
                }
            } finally {
                if (trace != null) {
                    trace.setResponse(proxy);
                    response.getWriter().print(proxy.getContent());
                }
                servedPages++;
                sumRuntime += System.currentTimeMillis() - time;
            }
        } catch (Exception e) {
            Logger.getInstance().log(this, e, "Error", Logger.ERROR);
            PrintWriter out = new PrintWriter(response.getWriter());
            out.println("Fatal error, see server log for details");
        }
    }
