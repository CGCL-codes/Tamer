    public void handle(String target, HttpServletRequest request, HttpServletResponse response, int dispatch) throws IOException, ServletException {
        Request base_request = (request instanceof Request) ? (Request) request : HttpConnection.getCurrentConnection().getRequest();
        if (!isStarted()) return;
        if (request.getParameter("continue") != null) {
            Continuation continuation = ContinuationSupport.getContinuation(request, null);
            continuation.suspend(Long.parseLong(request.getParameter("continue")));
        }
        base_request.setHandled(true);
        response.setHeader(HttpHeaders.CONTENT_TYPE, MimeTypes.TEXT_HTML);
        OutputStream out = response.getOutputStream();
        ByteArrayOutputStream buf = new ByteArrayOutputStream(2048);
        Writer writer = new OutputStreamWriter(buf, StringUtil.__ISO_8859_1);
        writer.write("<html><h1>" + label + "</h1>");
        writer.write("<pre>\npathInfo=" + request.getPathInfo() + "\n</pre>\n");
        writer.write("<pre>\ncontentType=" + request.getContentType() + "\n</pre>\n");
        writer.write("<pre>\nencoding=" + request.getCharacterEncoding() + "\n</pre>\n");
        writer.write("<h3>Header:</h3><pre>");
        writer.write(request.toString());
        writer.write("</pre>\n<h3>Parameters:</h3>\n<pre>");
        Enumeration names = request.getParameterNames();
        while (names.hasMoreElements()) {
            String name = names.nextElement().toString();
            String[] values = request.getParameterValues(name);
            if (values == null || values.length == 0) {
                writer.write(name);
                writer.write("=\n");
            } else if (values.length == 1) {
                writer.write(name);
                writer.write("=");
                writer.write(values[0]);
                writer.write("\n");
            } else {
                for (int i = 0; i < values.length; i++) {
                    writer.write(name);
                    writer.write("[" + i + "]=");
                    writer.write(values[i]);
                    writer.write("\n");
                }
            }
        }
        String cookie_name = request.getParameter("CookieName");
        if (cookie_name != null && cookie_name.trim().length() > 0) {
            String cookie_action = request.getParameter("Button");
            try {
                Cookie cookie = new Cookie(cookie_name.trim(), request.getParameter("CookieVal"));
                if ("Clear Cookie".equals(cookie_action)) cookie.setMaxAge(0);
                response.addCookie(cookie);
            } catch (IllegalArgumentException e) {
                writer.write("</pre>\n<h3>BAD Set-Cookie:</h3>\n<pre>");
                writer.write(e.toString());
            }
        }
        writer.write("</pre>\n<h3>Cookies:</h3>\n<pre>");
        Cookie[] cookies = request.getCookies();
        if (cookies != null && cookies.length > 0) {
            for (int c = 0; c < cookies.length; c++) {
                Cookie cookie = cookies[c];
                writer.write(cookie.getName());
                writer.write("=");
                writer.write(cookie.getValue());
                writer.write("\n");
            }
        }
        writer.write("</pre>\n<h3>Attributes:</h3>\n<pre>");
        Enumeration attributes = request.getAttributeNames();
        if (attributes != null && attributes.hasMoreElements()) {
            while (attributes.hasMoreElements()) {
                String attr = attributes.nextElement().toString();
                writer.write(attr);
                writer.write("=");
                writer.write(request.getAttribute(attr).toString());
                writer.write("\n");
            }
        }
        writer.write("</pre>\n<h3>Content:</h3>\n<pre>");
        byte[] content = new byte[4096];
        int len;
        try {
            InputStream in = request.getInputStream();
            while ((len = in.read(content)) >= 0) writer.write(new String(content, 0, len));
        } catch (IOException e) {
            writer.write(e.toString());
        }
        writer.write("</pre>");
        writer.write("</html>");
        writer.flush();
        response.setContentLength(buf.size() + 1000);
        buf.writeTo(out);
        buf.reset();
        writer.flush();
        for (int pad = 998 - buf.size(); pad-- > 0; ) writer.write(" ");
        writer.write("\015\012");
        writer.flush();
        buf.writeTo(out);
        response.setHeader("IgnoreMe", "ignored");
    }
