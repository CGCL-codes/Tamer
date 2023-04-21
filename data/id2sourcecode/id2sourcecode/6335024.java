    public void handle(String pathInContext, String pathParams, HttpRequest request, HttpResponse response) throws HttpException, IOException {
        if (!isStarted()) return;
        if (!HttpRequest.__GET.equals(request.getMethod()) && !HttpRequest.__HEAD.equals(request.getMethod()) && !HttpRequest.__POST.equals(request.getMethod())) return;
        log.debug("Dump");
        response.setField(HttpFields.__ContentType, HttpFields.__TextHtml);
        OutputStream out = response.getOutputStream();
        ByteArrayOutputStream buf = new ByteArrayOutputStream(2048);
        Writer writer = new OutputStreamWriter(buf, StringUtil.__ISO_8859_1);
        writer.write("<HTML><H1>Dump HttpHandler</H1>");
        writer.write("<PRE>\npath=" + request.getPath() + "\ncontextPath=" + getHttpContext().getContextPath() + "\npathInContext=" + pathInContext + "\n</PRE>\n");
        writer.write("<H3>Header:</H3><PRE>");
        writer.write(request.toString());
        writer.write("</PRE>\n<H3>Parameters:</H3>\n<PRE>");
        Set names = request.getParameterNames();
        Iterator iter = names.iterator();
        while (iter.hasNext()) {
            String name = iter.next().toString();
            List values = request.getParameterValues(name);
            if (values == null || values.size() == 0) {
                writer.write(name);
                writer.write("=\n");
            } else if (values.size() == 1) {
                writer.write(name);
                writer.write("=");
                writer.write((String) values.get(0));
                writer.write("\n");
            } else {
                for (int i = 0; i < values.size(); i++) {
                    writer.write(name);
                    writer.write("[" + i + "]=");
                    writer.write((String) values.get(i));
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
                response.addSetCookie(cookie);
            } catch (IllegalArgumentException e) {
                writer.write("</PRE>\n<H3>BAD Set-Cookie:</H3>\n<PRE>");
                writer.write(e.toString());
                LogSupport.ignore(log, e);
            }
        }
        writer.write("</PRE>\n<H3>Cookies:</H3>\n<PRE>");
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
        writer.write("</PRE>\n<H3>Attributes:</H3>\n<PRE>");
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
        writer.write("</PRE>\n<H3>Content:</H3>\n<PRE>");
        byte[] content = new byte[4096];
        int len;
        try {
            InputStream in = request.getInputStream();
            while ((len = in.read(content)) >= 0) writer.write(new String(content, 0, len));
        } catch (IOException e) {
            LogSupport.ignore(log, e);
            writer.write(e.toString());
        }
        request.getAcceptableTransferCodings();
        writer.flush();
        response.setIntField(HttpFields.__ContentLength, buf.size() + 1000);
        buf.writeTo(out);
        out.flush();
        buf.reset();
        writer.write("</PRE>\n<H3>Response:</H3>\n<PRE>");
        writer.write(response.toString());
        writer.write("</PRE></HTML>");
        writer.flush();
        for (int pad = 998 - buf.size(); pad-- > 0; ) writer.write(" ");
        writer.write("\015\012");
        writer.flush();
        buf.writeTo(out);
        request.setHandled(true);
    }
