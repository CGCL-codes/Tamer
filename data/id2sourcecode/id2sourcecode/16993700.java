    boolean internalProcessing(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String internal = request.getQueryString();
        if (internal != null) {
            if (internal.equals("js")) {
                if (script == null) {
                    HttpSession session = request.getSession();
                    script = getResource("script/process.js");
                    script = script.replaceAll("##sessiontimeout##", Integer.toString((session.getMaxInactiveInterval() * 1000) - (10 * 1000)));
                    if (actionfieldname != null) script = script.replaceAll("##actionname##", actionfieldname);
                    ETAG_VALUE = String.valueOf(script.hashCode());
                }
                if (ETAG_VALUE.equals(request.getHeader(IF_NONE_MATCH_HEADER))) {
                    response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
                    return true;
                } else {
                    response.setHeader(ETAG_HEADER, ETAG_VALUE);
                    streamzip(response, script);
                }
            } else if (internal.startsWith("js_")) {
                String file = internal.substring(3);
                String jspath = file + ".js";
                String etag = String.valueOf(new File(classloader.getResource(jspath).getFile()).lastModified());
                if (etag.equals(request.getHeader(IF_NONE_MATCH_HEADER))) {
                    response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
                    return true;
                } else {
                    response.setHeader(ETAG_HEADER, etag);
                    streamzip(response, getResource(jspath));
                }
            } else if (internal.startsWith("killer")) {
                HttpSession session = ((HttpServletRequest) request).getSession();
                try {
                    Thread thread = ((Thread) session.getAttribute("thread"));
                    System.out.println("kill: " + thread);
                    if (thread != null) thread.interrupt();
                    ((HttpServletResponse) response).sendRedirect("");
                } catch (Exception e) {
                }
            } else if (internal.startsWith("echo")) {
                PrintWriter out = response.getWriter();
                out.println(request.getParameter("struttercache"));
                out.flush();
            } else if (internal.startsWith("img")) {
                ServletOutputStream out = response.getOutputStream();
                BufferedInputStream in = getResourceAsStream(request.getParameter("name"));
                int data;
                while ((data = in.read()) != -1) out.write(data);
                response.setContentType("image/gif");
                out.flush();
                in.close();
            } else if (internal.startsWith("keepalive")) {
                System.out.println("Strutter: keep alive");
            }
        }
        return true;
    }
